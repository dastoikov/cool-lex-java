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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ItersTest {
  @ParameterizedTest
  @MethodSource
  public void testCmp(IntStream a, IntStream b, int expected) {
    int actual = Iters.cmp(a.iterator(), b.iterator());
    assertEquals(expected, actual);
  }

  static Stream<Arguments> testCmp() {
    return Stream.of(
        Arguments.of(IntStream.of(1, 2), IntStream.of(1, 2), 0),
        Arguments.of(IntStream.of(1, 2), IntStream.of(2, 1), -1),
        Arguments.of(IntStream.of(), IntStream.of(), 0),
        Arguments.of(IntStream.of(2, 1), IntStream.of(1, 2), 1),
        Arguments.of(IntStream.of(2, 1, 1), IntStream.of(2, 1, 1, 2), -1),
        Arguments.of(IntStream.of(2, 1, 1, 2), IntStream.of(2, 1, 1), 1),
        Arguments.of(IntStream.of(2, 1, 1, 2), IntStream.of(2, 1, 2), -1)
        /**/ );
  }

  @ParameterizedTest
  @MethodSource
  public void testSorted(IntStream input, IntStream expected) {
    assertEquals(0, Iters.cmp(expected.iterator(), Iters.sorted(input.iterator())));
  }

  static Stream<Arguments> testSorted() {
    return Stream.of(
        Arguments.of(IntStream.of(1, 2), IntStream.of(1, 2)),
        Arguments.of(IntStream.of(2, 1), IntStream.of(1, 2)),
        Arguments.of(IntStream.of(), IntStream.of())
        /**/ );
  }
}
