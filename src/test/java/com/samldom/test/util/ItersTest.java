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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ItersTest {
  @ParameterizedTest
  @MethodSource
  void cmp(IntStream a, IntStream b, int expected) {
    int actual = Iters.cmp(a.iterator(), b.iterator());
    assertEquals(expected, actual);
  }

  static Stream<Arguments> cmp() {
    return Stream.of(
        Arguments.of(IntStream.of(1, 2), IntStream.of(1, 2), 0),
        Arguments.of(IntStream.of(1, 2), IntStream.of(2, 1), -1),
        Arguments.of(IntStream.of(), IntStream.of(), 0),
        Arguments.of(IntStream.of(2, 1), IntStream.of(1, 2), 1),
        Arguments.of(IntStream.of(2, 1, 1), IntStream.of(2, 1, 1, 2), -1),
        Arguments.of(IntStream.of(2, 1, 1, 2), IntStream.of(2, 1, 1), 1),
        Arguments.of(IntStream.of(2, 1, 1, 2), IntStream.of(2, 1, 2), -1));
  }

  @ParameterizedTest
  @MethodSource
  void sorted(IntStream input, IntStream expected) {
    assertEquals(0, Iters.cmp(expected.iterator(), Iters.sorted(input.iterator())));
  }

  static Stream<Arguments> sorted() {
    return Stream.of(
        Arguments.of(IntStream.of(1, 2), IntStream.of(1, 2)),
        Arguments.of(IntStream.of(2, 1), IntStream.of(1, 2)),
        Arguments.of(IntStream.of(), IntStream.of()));
  }

  @ParameterizedTest
  @MethodSource
  void asSeq(List<?> testSet) {
    List<Object> seenBySeq = new LinkedList<>();
    Iters.asSeq(testSet.iterator()).forEach(seenBySeq::add);
    assertEquals(testSet, seenBySeq);
  }

  static Stream<Arguments> asSeq() {
    return Stream.of(Arguments.of(Arrays.asList()), Arguments.of(Arrays.asList('A', 65)));
  }

  @ParameterizedTest
  @MethodSource
  void asIntSeq(int[] testSet) {
    IntStream.Builder seenBySeq = IntStream.builder();
    Iters.asIntSeq(IntStream.of(testSet).iterator()).forEach(seenBySeq::add);
    assertArrayEquals(testSet, seenBySeq.build().toArray());
  }

  static Stream<Arguments> asIntSeq() {
    return Stream.of(Arguments.of(new int[] {}), Arguments.of(new int[] {1, 2, 1, 3}));
  }
}
