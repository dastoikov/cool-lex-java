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
package com.samldom.util.iter;

import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * A Go-style iterator that yields {@code int} values to a yield function and avoids auto-boxing.
 * The yield function signals the iterator back whether to continue producing values.
 */
public interface IntSeq {

  /**
   * Yields the next int value to the specified yield function.
   *
   * @param yield signals the iterator whether to continue producing int values.
   */
  void doWhile(IntPredicate yield);

  /**
   * Yields the integers in this sequence to the specified consumer.
   *
   * @param consumer of each integer.
   */
  default void forEach(IntConsumer consumer) {
    doWhile(
        i -> {
          consumer.accept(i);
          return true;
        });
  }
}
