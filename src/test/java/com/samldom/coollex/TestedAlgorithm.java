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
package com.samldom.coollex;

/**
 * A Cool-lex algorithm implementation. A unified API to aid in <em>testing</em> the different
 * algorithm implementations in a uniform way.
 *
 * @param <R> the type used by this algorithm implementation to represent the generated
 *     combinations.
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
}
