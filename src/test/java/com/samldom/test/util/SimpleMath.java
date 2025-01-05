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
package com.samldom.test.util;

/**
 * Simple/naive implementations of Math operations for the purposes of aiding in writing auto tests.
 */
public class SimpleMath {
  // suppress utility class instantiation.
  private SimpleMath() {}

  /**
   * Calculates the number of combinations for the specified {@code k} and {@code n}.
   *
   * @param n number of elements to combine; must be {@code >= k}.
   * @param k number of elements in each combination; must be non-negative.
   * @return number of combinations as described above.
   * @throws ArithmeticException if numeric overflow occurs
   * @throws IllegalArgumentException if {@code k < 0 || n < 0 || k > n}
   */
  public static long numComb(long n, long k) {
    if (k < 0 || n < 0 || k > n) {
      throw new IllegalArgumentException();
    }
    if (k == 0) {
      return 0;
    }
    return multiplyAll(n, n - k + 1) / factorial(k);
  }

  /**
   * Multiplies all numbers in the specified range {@code [n2; n1], n2 >= n1}.
   *
   * @param n2 range end
   * @param n1 range start
   * @return product as described above
   * @throws ArithmeticException if numeric overflow occurs
   */
  public static long multiplyAll(long n2, long n1) {
    long r = n2;
    while (--n2 >= n1) {
      r = Math.multiplyExact(r, n2);
    }
    return r;
  }

  /**
   * @param n positive integer.
   * @throws ArithmeticException if numeric overflow occurs
   * @throws IllegalArgumentException if {@code n<0}
   */
  public static long factorial(long n) {
    if (n < 0) {
      throw new IllegalArgumentException();
    }
    return n == 0 ? 1 : multiplyAll(n, 1);
  }
}
