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
 * Test oracle for Cool-lex algorithm implementations.
 *
 * @param <R> the type used by a specific Cool-lex algorithm to expose the generated combinations.
 */
interface TestOracle<R> {

  /**
   * Tests the specified algorithm with the specified n and k.
   *
   * @param n to pass to the algorithm under test (number of elements to choose from)
   * @param k to pass to the algorithm under test (number of elements in each combination)
   * @param alg under test
   * @throws AssertionError if the algorithm is found faulty by this oracle
   */
  void test(int n, int k, TestedAlgorithm<R> alg) throws AssertionError;
}
