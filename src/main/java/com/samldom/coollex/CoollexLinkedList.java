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

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfInt;

import com.samldom.util.iter.IntSeq;
import com.samldom.util.iter.Seq;

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
   * @return an empty iterator if {@code k=0}; the generated combinations otherwise.
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

  /**
   * Returns a sequence of combinations. Each combination is a sequence of integers, representing
   * the indices of the selected elements.
   *
   * @param n number of elements to combine; must be {@code >= k}.
   * @param k number of elements in each combination; must be non-negative.
   * @return an empty sequence if {@code k=0}; the generated combinations otherwise.
   * @throws IllegalArgumentException if {@code k < 0 || n < k}
   */
  public static Seq<IntSeq> sequence(int n, int k) {
    if (k < 0) {
      throw new IllegalArgumentException(
          "negative value specified for the number of elements in a combination");
    }
    if (n < k) {
      throw new IllegalArgumentException(
          "number of elements to combine is less than the number of elements in a combination");
    }
    return k == 0
        ? yield -> {} // empty sequence
        : combinationsSeq(new Algorithm(n - k, k));
  }

  private static class Algorithm {
    // b -- the head of the list; this is the node with the greatest "index"
    // x -- the first node, right-to-left, whose value is 1 and whose predecessor's value is 0
    Node x = new Node(true), b = x;

    /**
     * @param s the number of {@code 0}-bits.
     * @param t the number of {@code 1}-bits. Must be {@code >0}.
     */
    Algorithm(int s, int t) {
      for (; --t > 0; x = x.next = new Node(true))
        ;

      for (Node last = x; --s >= 0; last = last.next = new Node(false))
        ;
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

    private static class Node {
      final boolean value;
      Node next;

      Node(boolean value) {
        this.value = value;
      }
    }
  }

  private static class CombinationsIterator implements Iterator<OfInt> {
    private final Algorithm coolLex;
    private boolean first = true;

    CombinationsIterator(Algorithm coolLex) {
      this.coolLex = coolLex;
    }

    @Override
    public boolean hasNext() {
      // the algorithm is initially positioned at the first combination:
      return first || coolLex.hasNext();
    }

    @Override
    public OfInt next() {
      if (!first) {
        if (!coolLex.hasNext()) {
          throw new NoSuchElementException();
        }
        coolLex.next();
      }
      first = false; // avoid branches
      return new SelectedIndicesIterator(coolLex.b);
    }
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
  private static class SelectedIndicesIterator implements OfInt {
    private Algorithm.Node currNode;
    private int i;

    SelectedIndicesIterator(Algorithm.Node fromNode) {
      nextValueTrueNode(fromNode);
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

  private static Seq<IntSeq> combinationsSeq(Algorithm coollex) {
    return yield -> {
      for (; yield.test(elementsSeq(coollex.b)) && coollex.hasNext(); coollex.next())
        ;
    };
  }

  private static IntSeq elementsSeq(Algorithm.Node from) {
    return yield -> {
      int i = 0;
      for (Algorithm.Node curr = from;
          curr != null && (!curr.value || yield.test(i));
          curr = curr.next, i++)
        ;
    };
  }
}
