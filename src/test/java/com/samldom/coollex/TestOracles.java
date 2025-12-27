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

import static com.samldom.test.util.SimpleMath.numComb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import com.samldom.util.iter.IntSeq;
import com.samldom.util.iter.Seq;

/** Static methods that create {@link TestOracle}-s. */
class TestOracles {

  // suppressed instantiation for utility classes
  private TestOracles() {}

  /**
   * Returns a test oracle that expects the algorithm to throw the specified exception.
   *
   * @param type the type of the exception expected to be thrown.
   * @return a non-null test oracle asserting that the specified exception is thrown.
   */
  static TestOracle<Seq<IntSeq>> throwing(Class<? extends Throwable> type) {
    return (n, k, alg) -> {
      try {
        alg.combinations(n, k);
      } catch (Throwable t) {
        if (!type.isInstance(t)) {
          throw new RuntimeException(t);
        }
      }
    };
  }

  /**
   * Returns a test oracle that expects the algorithm to expose the combinations as sequences.
   *
   * @return a non-null test oracle asserting the correctness of the generated combinations and
   *     their elements.
   */
  static TestOracle<Seq<IntSeq>> sequence() {
    return (n, k, alg) -> {
      // array index denotes an element
      // value at given index denotes how many times this element appeared in a combination
      int[] hits = new int[n];
      Arrays.fill(hits, 0);

      class IntVar {
        int val = 0;
      }

      // total number of combinations yielded by the algorithm
      IntVar numComb = new IntVar();

      // number of elements in the current combination
      IntVar numElem = new IntVar();

      Seq<IntSeq> sequence = alg.combinations(n, k);
      sequence.forEach(
          combination -> {
            ++numComb.val;
            combination.forEach(
                element -> {
                  ++numElem.val;
                  ++hits[element];
                });

            assertEquals(k, numElem.val, "number of elements in a combination");
            numElem.val = 0;
          });

      assertEquals(numComb(n, k), numComb.val, "number of combinations");

      long occur = numComb(n - 1, k - 1);
      for (int hit : hits) {
        assertEquals(occur, hit, "number of combinations where each element appears");
      }
    };
  }

  /**
   * Returns a test oracle that expects the algorithm to generate an empty sequence.
   *
   * @return a non-null test oracle asserting that the algorithm does not generate any combinations.
   */
  static TestOracle<Seq<IntSeq>> emptySequence() {
    return (n, k, alg) -> {
      Seq<IntSeq> sequence = alg.combinations(n, k);
      sequence.forEach(
          combination -> {
            combination.forEach(
                element -> fail(String.format("elements found for n=%d, k=%d", n, k)));
            fail(String.format("combinations found for n=%d, k=%d", n, k));
          });
    };
  }
}
