/**
 * Copyright 2021-2026 The Cool-lex-Java Contributors, see the CONTRIBUTORS file.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samldom.coollex;

import static com.samldom.test.util.SimpleMath.numComb;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import com.samldom.test.util.Iters;
import com.samldom.util.iter.IntSeq;
import com.samldom.util.iter.Seq;

/** Tests for the {@link CoollexLinkedList} class. */
class CoollexLinkedListTest {

  @ParameterizedTest(name = "n={0} k={1} alg={3}")
  @MethodSource
  <R> void linkedList(int n, int k, TestOracle<R> oracle, TestedAlgorithm<R> alg) {
    oracle.test(n, k, alg);
  }

  static Stream<Arguments> linkedList() {
    TestedAlgorithm<Seq<IntSeq>> seqAlg =
        TestedAlgorithm.named("sequence(..)", CoollexLinkedList::sequence);
    TestedAlgorithm<Seq<IntSeq>> iterAlg =
        TestedAlgorithm.named(
            "combinations(..)",
            (n, k) -> {
              Iterator<OfInt> ii = CoollexLinkedList.combinations(n, k);
              return yield -> Iters.asSeq(ii).doWhile(ofInt -> yield.test(Iters.asIntSeq(ofInt)));
            });

    TestedAlgorithm<?>[] algs = {seqAlg, iterAlg};

    return Stream.of(
            args(
                new int[][] {{1, 1}, {9, 9}, {10, 4}, {15, 8}, {15, 7}, {25, 13}},
                singletonMap(TestOracles.sequence(), algs)),
            args(
                new int[][] {{1, 0}, {2, 0}, {3, 0}, {0, 0}, {Integer.MAX_VALUE, 0}},
                singletonMap(TestOracles.emptySequence(), algs)),
            args(
                new int[][] {{1, 2}, {-1, -2}, {-1, 0}},
                singletonMap(TestOracles.throwing(IllegalArgumentException.class), algs)))
        .flatMap(s -> s);
  }

  private static Stream<Arguments> args(
      int[][] testCases, Map<TestOracle<?>, TestedAlgorithm<?>[]> algorithmsByOracle) {
    return Stream.of(testCases)
        .flatMap(
            nk ->
                algorithmsByOracle.entrySet().stream()
                    .flatMap(
                        entry ->
                            Stream.of(entry.getValue())
                                .map(
                                    algorithm ->
                                        Arguments.of(
                                            nk[0] /*n*/,
                                            nk[1] /*k*/,
                                            entry.getKey() /*test oracle*/,
                                            algorithm /*tested algorithm*/))));
  }

  @ParameterizedTest
  @CsvSource({"3, 2"})
  void elementIterator(int n, int k) {
    for (Iterator<PrimitiveIterator.OfInt> combIter = CoollexLinkedList.combinations(n, k);
        combIter.hasNext(); ) {
      PrimitiveIterator.OfInt elemIter = combIter.next();
      for (int i = 0; i < k; ++i, elemIter.next())
        ;
      assertFalse(elemIter.hasNext(), "hasNext() after all elements yielded");
      assertThrows(NoSuchElementException.class, elemIter::next);
    }
  }

  @ParameterizedTest
  @CsvSource({"3, 2"})
  void combinationsIterator(int n, int k) {
    Iterator<PrimitiveIterator.OfInt> combIter = CoollexLinkedList.combinations(n, k);
    for (long c = numComb(n, k); --c >= 0; combIter.next())
      ;
    assertFalse(combIter.hasNext(), "hasNext() after all combinations yielded");
    assertThrows(NoSuchElementException.class, combIter::next);
  }

  @ParameterizedTest
  @CsvSource({"3, 2"})
  void sameThreadMultipleInstances(int n, int k) {

    // verify test preconditions
    assertTrue(numComb(n, k) > 1);

    Iterator<PrimitiveIterator.OfInt> a = CoollexLinkedList.combinations(n, k);
    Iterator<PrimitiveIterator.OfInt> b = CoollexLinkedList.combinations(n, k);

    PrimitiveIterator.OfInt elementsOfA = a.next(); // a is at c1
    b.next(); // b is at c1
    b.next(); // b is at c2

    assertEquals(
        0,
        Iters.cmp(
            // c1 always consists of elements with indices [0,k]
            IntStream.range(0, k).iterator(),
            // sort indices, as there are no guarantees of any particular element order
            Iters.sorted(elementsOfA)));
  }
}
