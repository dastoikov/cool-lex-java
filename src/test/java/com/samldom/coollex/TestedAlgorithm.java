/**
 * Copyright 2025-2026 The Cool-lex-Java Contributors, see the CONTRIBUTORS file.
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

/**
 * A Cool-lex algorithm implementation. A unified API to aid in <em>testing</em> the different
 * algorithm implementations in a uniform way.
 *
 * @param <R> the type used by this algorithm to represent combinations.
 */
interface TestedAlgorithm<R> {
  /**
   * Triggers the combination generation process and returns a handle to the generated combinations
   * and their elements.
   *
   * @param n the number of elements to choose from;
   * @param k the number of elements selected;
   * @return a handle to the algorithm output.
   * @throws RuntimeException if the specified {@code n} and {@code k} are invalid--individually or
   *     when taken together.
   */
  R combinations(int n, int k);

  /**
   * Shorthand for creating named instances. Names are used in assertion messages.
   *
   * @param name the name
   * @param algorithm the tested algorithm to be assigned a name.
   * @param <R> the type used by {@code r} to represent the generated combinations.
   * @return a tested algorithm delegating the run to {@code r}, overriding {@code r.toString()}.
   */
  static <R> TestedAlgorithm<R> named(String name, TestedAlgorithm<? extends R> algorithm) {
    return new TestedAlgorithm<R>() {

      @Override
      public R combinations(int n, int k) {
        return algorithm.combinations(n, k);
      }

      @Override
      public String toString() {
        return name;
      }
    };
  }
}
