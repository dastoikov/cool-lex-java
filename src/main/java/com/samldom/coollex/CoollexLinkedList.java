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

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfInt;

/**
 * The <em>cool-lex</em> order and algorithms have been invented by Frank Ruskey and Aaron Williams.
 * Hats off.
 *
 * <p>See <a href= "http://webhome.cs.uvic.ca/~ruskey/Publications/Coollex/CoolComb.html">
 * http://webhome.cs.uvic.ca/~ruskey/Publications/Coollex/CoolComb.html</a>.
 *
 * <p>See section<b> 3.2. Iterative Algorithms.</b>
 */
public class CoollexLinkedList {

  // suppress utility class instantiation.
  private CoollexLinkedList() {}

  /**
   * Returns an iterator over the generated combinations. A combination is represented with the
   * indices of the selected elements.
   *
   * @param n number of elements to combine; must be {@code >= k}.
   * @param k number of elements in each combination; must be non-negative.
   * @return an empty iterator if {@code 0} was specified as the number of elements in a
   *     combination; the generated combinations otherwise.
   * @throws IllegalArgumentException if {@code k < 0 || n < k}
   */
  public static Iterator<PrimitiveIterator.OfInt> combinations(int n, int k) {
    if (k < 0) {
      throw new IllegalArgumentException(
          "negative value specified for the number of elements in a combination");
    }
    if (n < k) {
      throw new IllegalArgumentException(
          "number of elements to combine is less than the number of elements in a combination");
    }
    return (k == 0)
        ? Collections.emptyIterator()
        : new CombinationsIterator(new Algorithm(n - k, k));
  }

  static class Algorithm {

    // b -- the head of the list; this is the node with the greatest "index"
    // x -- the first node, right-to-left, whose value is 1 and whose predecessor's value is 0
    Node b, x;

    /**
     * @param s the number of {@code 0}-bits.
     * @param t the number of {@code 1}-bits. Must be {@code >0}.
     */
    Algorithm(int s, int t) {

      b = new Node(true);
      x = b;

      while (--t > 0) {
        x = x.createNext(true);
      }

      Node last = x;
      while (--s >= 0) {
        last = last.createNext(false);
      }
    }

    /** Advances to the next combination. */
    void next() {
      Node y = x.next;
      x.next = x.next.next;
      y.next = b;
      b = y;

      if (!b.value && b.next.value) {
        x = b.next;
      }
    }

    /** End of algorithm? */
    boolean hasNext() {
      return x.next != null;
    }

    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(); // can be sized: (s+t)
      Node tmp = b;
      do {
        buf.append(tmp.value ? '1' : '0');
      } while ((tmp = tmp.next) != null);
      return buf.toString();
    }

    static class Node {
      boolean value;
      Node next;

      Node(boolean value) {
        this.value = value;
      }

      Node createNext(boolean value) {
        Node next = new Node(value);
        this.next = next;
        return next;
      }
    }
  }

  private static class CombinationsIterator implements Iterator<OfInt> {
    private final Iterator<OfInt> onward;
    private Iterator<OfInt> gen;

    CombinationsIterator(Algorithm coolLex) {
      onward =
          new Iterator<OfInt>() {

            @Override
            public boolean hasNext() {
              return coolLex.hasNext();
            }

            @Override
            public OfInt next() {
              if (!coolLex.hasNext()) {
                throw new NoSuchElementException();
              }
              coolLex.next();
              return SelectedIndicesIterator.SERIAL.reset(coolLex);
            }
          };
      gen =
          new Iterator<OfInt>() {

            @Override
            public OfInt next() {
              // the algorithm is initially positioned at the first combination
              gen = onward;
              return SelectedIndicesIterator.SERIAL.reset(coolLex);
            }

            @Override
            public boolean hasNext() {
              return true;
            }
          };
    }

    @Override
    public boolean hasNext() {
      return gen != onward || onward.hasNext();
    }

    @Override
    public OfInt next() {
      return gen.next();
    }

    /**
     * Example:
     *
     * <pre>
     * combination:      1101001
     *                   ^^ ^  ^
     * iterator yields:  01 3  6
     * </pre>
     */
    private enum SelectedIndicesIterator implements OfInt {
      SERIAL; // use a singleton instance for serial processing

      private Algorithm.Node currNode;
      private int i;

      SelectedIndicesIterator reset(Algorithm alg) {
        i = 0;
        nextValueTrueNode(alg.b);
        return this;
      }

      @Override
      public boolean hasNext() {
        return currNode != null;
      }

      @Override
      public int nextInt() {
        if (currNode == null) {
          throw new NoSuchElementException();
        }
        int retval = i++;
        nextValueTrueNode(currNode.next);

        return retval;
      }

      private void nextValueTrueNode(Algorithm.Node from) {
        for (currNode = from; currNode != null && !currNode.value; currNode = currNode.next, i++)
          ;
      }
    }
  }
}
