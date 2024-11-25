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

import java.util.Arrays;
import java.util.Iterator;
import java.util.PrimitiveIterator;

import org.junit.jupiter.api.Test;

public class CoollexLinkedListTest {

  @Test
  public void testLinkedList_49_6() {
    testLinkedList(49, 6);
  }

  @Test
  public void testLinkedList_9_9() {
    testLinkedList(9, 9);
  }

  @Test
  public void testLinkedList_15_7() {
    testLinkedList(15, 7);
  }

  @Test
  public void testLinkedList_15_6() {
    testLinkedList(15, 6);
  }

  @Test
  public void testLinkedList_10_4() {
    testLinkedList(10, 4);
  }

  @Test
  public void testElementIterator() {
    testElementsIterator(3, 2);
  }

  @Test
  public void testCombinationsIterator() {
    testCombinationsIterator(3, 2);
  }

  //
  // Test methods (avoid @ParameterizedTest for the time being)
  //

  private static void testLinkedList(int n, int k) {
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

  private static void testElementsIterator(int n, int k) {
    for (Iterator<PrimitiveIterator.OfInt> combIter = CoollexLinkedList.combinations(n, k);
        combIter.hasNext(); ) {

      PrimitiveIterator.OfInt elemIter = combIter.next();
      for (int i = 0; i < k; ++i, elemIter.next())
        ;
      assertFalse(elemIter.hasNext(), "hasNext() after all elements yielded");
    }
  }

  private static void testCombinationsIterator(int n, int k) {
    Iterator<PrimitiveIterator.OfInt> combIter = CoollexLinkedList.combinations(n, k);
    long j = numComb(n, k);

    while (--j >= 0) {
      combIter.next();
    }
    assertFalse(combIter.hasNext(), "hasNext() after all combinations yielded");
  }
}
