package com.samldom.coollex;

import static java.lang.String.format;

import java.util.Arrays;
import java.util.Iterator;
import java.util.PrimitiveIterator;

public class Test {

  //
  // Driver.
  //
  public static void main(String[] args) {
    testLinkedList(49, 6);
    testLinkedList(9, 9);
    testLinkedList(10, 4);
    testLinkedList(15, 7);
    testLinkedList(15, 6);

    // linked-list iterator tests
    testElementIterator(3, 2);
    testCombinationsIterator(3, 0);
  }

  //
  // Test methods.
  //

  static void testLinkedList(int n, int k) {
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

      assertEq("number of elements in a combination", k, numElem);
    }

    assertEq("number of combinations", numComb(n, k), numComb);

    long occur = numComb(n - 1, k - 1);
    for (int hit : hits) {
      assertEq("number of combinations where each element appears", occur, hit);
    }
  }

  static void testElementIterator(int n, int k) {
    for (Iterator<PrimitiveIterator.OfInt> combIter = CoollexLinkedList.combinations(n, k);
        combIter.hasNext(); ) {

      PrimitiveIterator.OfInt elemIter = combIter.next();
      for (int i = 0; i < k; ++i, elemIter.next())
        ;
      assertFalse("hasNext() after all elements yielded", elemIter.hasNext());
    }
  }

  static void testCombinationsIterator(int n, int k) {
    Iterator<PrimitiveIterator.OfInt> combIter = CoollexLinkedList.combinations(n, k);
    long j = numComb(n, k);

    while (--j >= 0) {
      combIter.next();
    }
    assertFalse("hasNext() after all combinations yielded", combIter.hasNext());
  }

  //
  // Helper methods.
  //

  private static void assertFalse(String message, boolean actual) {
    if (actual) {
      throw new AssertionError(format("%s: expected %b, actual %b", message, false, actual));
    }
  }

  private static void assertEq(String message, long expected, long actual) {
    if (expected != actual) {
      throw new AssertionError(format("%s: expected %d, actual %d", message, expected, actual));
    }
  }

  private static long numComb(long n, long k) {
    if (k < 0 || n < 0 || k > n) {
      throw new IllegalArgumentException();
    }
    if (k == 0) {
      return 0;
    }
    return multiplyAll(n, n - k + 1) / factorial0(k);
  }

  private static long multiplyAll(long n2, long n1) {
    long r = n2;
    while (--n2 >= n1) {
      r = Math.multiplyExact(r, n2);
    }
    return r;
  }

  /**
   * @param n positive integer.
   */
  private static long factorial0(long n) {
    return multiplyAll(n, 1);
  }
}
