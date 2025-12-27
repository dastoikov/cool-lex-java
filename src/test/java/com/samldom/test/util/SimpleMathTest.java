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
