package com.samldom.test.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SimpleMathTest {

  @Test
  public void testFactorialOfZero() {
    assertEquals(1, SimpleMath.factorial(0));
  }

  @Test
  public void testFactorialOfNegativeInteger() {
    assertThrows(IllegalArgumentException.class, () -> SimpleMath.factorial(Integer.MIN_VALUE));
  }

  @Test
  public void testFactorialOfPositiveInteger() {
    assertEquals(6, SimpleMath.factorial(3));
  }

  @Test
  public void testFactorialOfLargePositiveInteger() {
    assertThrows(ArithmeticException.class, () -> SimpleMath.factorial(Integer.MAX_VALUE));
  }
}
