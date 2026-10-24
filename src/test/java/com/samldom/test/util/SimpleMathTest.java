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
package com.samldom.test.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SimpleMathTest {

  @Test
  void factorialOfZero() {
    assertEquals(1, SimpleMath.factorial(0));
  }

  @Test
  void factorialOfNegativeInteger() {
    assertThrows(IllegalArgumentException.class, () -> SimpleMath.factorial(Integer.MIN_VALUE));
  }

  @Test
  void factorialOfPositiveInteger() {
    assertEquals(6, SimpleMath.factorial(3));
  }

  @Test
  void factorialOfLargePositiveInteger() {
    assertThrows(ArithmeticException.class, () -> SimpleMath.factorial(Integer.MAX_VALUE));
  }

  @Test
  void numCombK0() {
    assertEquals(1, SimpleMath.numComb(2, 0));
  }
}
