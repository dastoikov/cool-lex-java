/**
 * Copyright 2021-2025 The Cool-lex-Java Contributors, see the CONTRIBUTORS file.
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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.samldom.test.util.Iters;
import com.samldom.test.util.SimpleMath;
import com.samldom.util.iter.IntSeq;
import com.samldom.util.iter.Seq;

class CoollexLinkedListTest {

  @ParameterizedTest
  @MethodSource
  <R> void linkedList(int n, int k, TestOracle<R> oracle, TestedAlgorithm<R> alg) {
    oracle.test(n, k, alg);
  }

  static Stream<Arguments> linkedList() {
    TestedAlgorithm<Seq<IntSeq>> seqAlg = CoollexLinkedList::sequence;
    TestedAlgorithm<Seq<IntSeq>> iterAlg =
        (n, k) ->
            yield ->
                Iters.asSeq(CoollexLinkedList.combinations(n, k))
                    .doWhile(ofInt -> yield.test(Iters.asIntSeq(ofInt)));

    List<TestedAlgorithm<?>> algs = Arrays.asList(seqAlg, iterAlg);

    return Stream.concat(
        Stream.concat(
            llTestArgs(
                new int[][] {{1, 1}, {9, 9}, {10, 4}, {15, 8}, {15, 7}, {25, 13}},
                singletonMap(TestOracles.sequence(), algs)),
            llTestArgs(
                new int[][] {{1, 0}, {2, 0}, {3, 0}, {0, 0}, {Integer.MAX_VALUE, 0}},
                singletonMap(TestOracles.emptySequence(), algs))),
        llTestArgs(
            new int[][] {{1, 2}, {-1, -2}, {-1, 0}},
            singletonMap(TestOracles.throwing(IllegalArgumentException.class), algs)));
  }

  private static Stream<Arguments> llTestArgs(
      int[][] tc, Map<TestOracle<?>, List<TestedAlgorithm<?>>> oracles) {
    return Arrays.stream(tc)
        .flatMap(
            (nk) ->
                oracles.entrySet().stream()
                    .flatMap(
                        entry ->
                            entry.getValue().stream()
                                .map(
                                    algorithm ->
                                        Arguments.of(nk[0], nk[1], entry.getKey(), algorithm))));
  }

  @Test
  void elementIterator() {
    testElementsIterator(3, 2);
  }

  private static void testElementsIterator(int n, int k) {
    for (Iterator<PrimitiveIterator.OfInt> combIter = CoollexLinkedList.combinations(n, k);
        combIter.hasNext(); ) {

      PrimitiveIterator.OfInt elemIter = combIter.next();
      for (int i = 0; i < k; ++i, elemIter.next())
        ;
      assertFalse(elemIter.hasNext(), "hasNext() after all elements yielded");
    }
  }

  @Test
  void combinationsIterator() {
    testCombinationsIterator(3, 2);
  }

  private static void testCombinationsIterator(int n, int k) {
    Iterator<PrimitiveIterator.OfInt> combIter = CoollexLinkedList.combinations(n, k);
    long j = numComb(n, k);

    while (--j >= 0) {
      combIter.next();
    }
    assertFalse(combIter.hasNext(), "hasNext() after all combinations yielded");
  }

  @ParameterizedTest
  @MethodSource
  void sameThreadMultipleInstances(int n, int k) {
    // verify test preconditions
    assertTrue(SimpleMath.numComb(n, k) > 1);

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

  static Stream<Arguments> sameThreadMultipleInstances() {
    return Stream.of(Arguments.of(3, 2));
  }
}
