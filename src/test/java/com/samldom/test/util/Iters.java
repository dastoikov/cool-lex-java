/**
 * Copyright 2025 The Cool-lex-Java Contributors, see the CONTRIBUTORS file.
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
package com.samldom.test.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfInt;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import com.samldom.util.iter.IntSeq;
import com.samldom.util.iter.Seq;

/**
 * Simple/naive implementations of utility operations on iterators, for the purposes of aiding in
 * writing auto tests.
 */
public class Iters {
  // suppress utility class instantiation.
  private Iters() {}

  /**
   * Compares {@code a} and {@code b} lexicographically.
   *
   * @param a non-null
   * @param b non-null
   * @return
   *     <ul>
   *       <li>-1 if {@code a} is lexicographically less than {@code b}
   *       <li>1 if {@code b} is lexicographically less than {@code a}
   *       <li>0 if {@code a} and {@code b} are equal
   *     </ul>
   */
  public static int cmp(PrimitiveIterator.OfInt a, PrimitiveIterator.OfInt b) {
    while (a.hasNext()) {
      if (!b.hasNext()) {
        return 1;
      }
      int cmp = Integer.compare(a.nextInt(), b.nextInt());
      if (cmp != 0) {
        return cmp;
      }
    }
    if (b.hasNext()) {
      return -1;
    }
    return 0;
  }

  /**
   * Returns and iterator reporting the same elements as {@code iter}, with encounter order changed
   * to reflect the lexicographical order of integers.
   *
   * @param iter non-null
   * @return as described above.
   */
  public static PrimitiveIterator.OfInt sorted(PrimitiveIterator.OfInt iter) {
    return StreamSupport.intStream(
            Spliterators.spliteratorUnknownSize(iter, Spliterator.ORDERED), false)
        .sorted()
        .iterator();
  }

  /**
   * Returns a sequence backed by the specified iterator.
   *
   * @param <E> the type of the elements produced by the iterator
   * @param iter the backing iterator
   * @return a lazy sequence: the iterator is traversed as sequence elements are consumed.
   * @throws NullPointerException if {@code iter} is null.
   */
  public static <T> Seq<T> asSeq(Iterator<? extends T> iter) {
    return new IterToSeq<>(Objects.requireNonNull(iter));
  }

  static class IterToSeq<T> implements Seq<T> {
    final Iterator<? extends T> iter;

    IterToSeq(Iterator<? extends T> iter) {
      this.iter = iter;
    }

    @Override
    public void doWhile(Predicate<? super T> yield) {
      while (iter.hasNext() && yield.test(iter.next()))
        ;
    }
  }

  /**
   * Returns a sequence of primitive integers, backed by the specified iterator over primitive
   * integers.
   *
   * @param iter the backing iterator
   * @return a lazy sequence: the iterator is traversed as sequence elements are consumed.
   * @throws NullPointerException if {@code iter} is null.
   */
  public static IntSeq asIntSeq(PrimitiveIterator.OfInt iter) {
    return new OfIntToIntSeq(Objects.requireNonNull(iter));
  }

  static class OfIntToIntSeq implements IntSeq {
    final OfInt iter;

    OfIntToIntSeq(OfInt iter) {
      this.iter = iter;
    }

    @Override
    public void doWhile(IntPredicate yield) {
      while (iter.hasNext() && yield.test(iter.next()))
        ;
    }
  }
}
