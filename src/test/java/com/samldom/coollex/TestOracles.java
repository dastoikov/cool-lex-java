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

import static com.samldom.test.util.SimpleMath.numComb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.samldom.util.iter.IntSeq;
import com.samldom.util.iter.Seq;

/** Static methods that create {@link TestOracle}-s. */
class TestOracles {

  // suppressed instantiation for utility classes
  private TestOracles() {}

  /**
   * Returns a test oracle that expects the algorithm to throw the specified throwable.
   *
   * @param error the type of the throwable expected to be thrown.
   * @return a non-null test oracle asserting that the specified throwable is thrown.
   */
  static Enricher<Seq<IntSeq>> throwing(Class<? extends Throwable> error) {
    return (n, k, alg, x) ->
        assertThrows(error, () -> alg.combinations(n, k), () -> msg(x, "error checking"));
  }

  /**
   * Returns a test oracle that expects the algorithm to expose the combinations as sequences.
   *
   * @return a non-null test oracle asserting the correctness of the generated combinations and
   *     their elements.
   */
  static Enricher<Seq<IntSeq>> sequence() {
    return (n, k, alg, x) -> {
      // how many times (map value) an element (map key) appeared in a combination
      Map<Integer, Long> hits = new HashMap<>(n);

      // total number of combinations yielded by the algorithm
      IntVar numComb = new IntVar();
      alg.combinations(n, k)
          .forEach(
              combination -> {
                ++numComb.val;
                IntVar numElem = new IntVar();
                combination.forEach(
                    element -> {
                      ++numElem.val;
                      hits.merge(element, 1L, Math::addExact);
                    });
                assertEquals(k, numElem.val, () -> msg(x, "number of elements in a combination"));
              });
      assertEquals(numComb(n, k), numComb.val, () -> msg(x, "number of combinations"));

      long elementOccur = numComb(n - 1, k - 1);
      hits.forEach(
          (element, hit) ->
              assertEquals(
                  elementOccur,
                  hit,
                  () -> msg(x, "number of combinations where an element appears (%d)", element)));
    };
  }

  /**
   * Returns a test oracle that expects the algorithm to generate an empty sequence.
   *
   * @return a non-null test oracle asserting that the algorithm does not generate any combinations.
   */
  static Enricher<Seq<IntSeq>> emptySequence() {
    return (n, k, alg, x) ->
        alg.combinations(n, k)
            .forEach(
                combination -> {
                  combination.forEach(element -> fail(() -> msg(x, "element found: %d", element)));
                  fail(() -> msg(x, "combinations found"));
                });
  }

  // keep this interface private, it does not constitute a pure contract
  // API users a TestOracle
  private interface Enricher<R> extends TestOracle<R> {

    default void test(int n, int k, TestedAlgorithm<R> alg) throws AssertionError {
      test(n, k, alg, () -> new Context(n, k, alg));
    }

    void test(int n, int k, TestedAlgorithm<R> alg, Supplier<Context> ctx) throws AssertionError;
  }

  private static class Context {
    final int n, k;
    final TestedAlgorithm<?> alg;

    Context(int n, int k, TestedAlgorithm<?> alg) {
      this.n = n;
      this.k = k;
      this.alg = alg;
    }
  }

  private static String msg(Supplier<Context> e, String fmt, Object... fmtArgs) {
    Context ctx = e.get();
    Object[] args = Arrays.copyOf(fmtArgs, fmtArgs.length + 3);
    args[args.length - 1] = ctx.alg;
    args[args.length - 2] = ctx.k;
    args[args.length - 3] = ctx.n;
    return String.format(fmt.concat("for n=%d, k=%d, alg=%s"), args);
  }
}
