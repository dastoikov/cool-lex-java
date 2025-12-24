/**
 * Copyright 2021-2024 The Cool-lex-Java Contributors, see the CONTRIBUTORS file.
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
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

public class CoollexLinkedListTest {

  @ParameterizedTest
  @MethodSource
  public void testLinkedList(int n, int k) {
    // array index denotes an element
    // value at given index denotes how many times this element appeared in a combination
    int[] hits = new int[n];
    Arrays.fill(hits, 0);

    // total number of combinations yielded by the algorithm
    int numComb = 0;
    for (Iterator<PrimitiveIterator.OfInt> combIter = CoollexLinkedList.combinations(n, k);
        combIter.hasNext();
        ++numComb) {

      // number of elements in this combination
      int numElem = 0;
      for (PrimitiveIterator.OfInt elemIter = combIter.next();
          elemIter.hasNext();
          ++numElem, ++hits[elemIter.nextInt()])
        ;

      assertEquals(k, numElem, "number of elements in a combination");
    }

    assertEquals(numComb(n, k), numComb, "number of combinations");

    long occur = numComb(n - 1, k - 1);
    for (int hit : hits) {
      assertEquals(occur, hit, "number of combinations where each element appears");
    }
  }

  static Stream<Arguments> testLinkedList() {
    return Stream.of(
        Arguments.of(1, 1),
        Arguments.of(10, 4),
        Arguments.of(15, 6),
        Arguments.of(15, 7),
        Arguments.of(49, 6),
        Arguments.of(9, 9));
  }

  @Test
  public void testElementIterator() {
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
  public void testCombinationsIterator() {
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

  @Test
  public void testLinkedListSequence() {
    class Tester {
      void test(int n, int k) {
        // array index denotes an element
        // value at given index denotes how many times this element appeared in a combination
        int[] hits = new int[n];
        Arrays.fill(hits, 0);

        class IntVar {
          int val = 0;
        }

        // total number of combinations yielded by the algorithm
        IntVar numComb = new IntVar();

        // number of elements in the current combination
        IntVar numElem = new IntVar();

        Seq<IntSeq> sequence = CoollexLinkedList.sequence(n, k);
        sequence.forEach(
            combination -> {
              ++numComb.val;
              combination.forEach(
                  element -> {
                    ++numElem.val;
                    ++hits[element];
                  });

              assertEquals(k, numElem.val, "number of elements in a combination");
              numElem.val = 0;
            });

        assertEquals(numComb(n, k), numComb.val, "number of combinations");

        long occur = numComb(n - 1, k - 1);
        for (int hit : hits) {
          assertEquals(occur, hit, "number of combinations where each element appears");
        }
      }
    }
    Tester tester = new Tester();

    tester.test(1, 1);
    tester.test(9, 9);
    tester.test(10, 4);
    tester.test(15, 6);
    tester.test(15, 7);
  }

  @ParameterizedTest
  @MethodSource
  public void testSameThreadMultipleInstances(int n, int k) {
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

  static Stream<Arguments> testSameThreadMultipleInstances() {
    return Stream.of(Arguments.of(3, 2));
  }
}
