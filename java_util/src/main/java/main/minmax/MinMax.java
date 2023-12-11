package main.minmax;

import java.util.List;
import java.util.HashMap;

/**
 * Holds the min max algorithm and the state interface.
 */
public class MinMax {

  /**
   * The state to run the min max algorithm on.
   */
  public interface State<T> {
    float evaluate();

    boolean isLeaf();

    boolean isMaxTurn();

    List<T> getChildren();
  }

  /**
   * Runns the min max algorithm.
   *
   * @param state the state to run the min max algorithm on
   * @param depth the depth of the min max algorithm
   * @return the state with the highest evaluation
   */
  public static <T extends State<T>> float evaluate(T state, int depth) {
    return evaluate(
      state, 
      depth, 
      Float.NEGATIVE_INFINITY,
      Float.POSITIVE_INFINITY
    );
  }

  /**
   * Runns the min max algorithm with alpha beta pruning.
   *
   * @param state the state to run the min max algorithm on
   * @param depth the depth of the min max algorithm
   * @param alpha the alpha value
   * @param beta the beta value
   * @return the state with the highest evaluation
   */
  public static <T extends State<T>> float evaluate(
      T state, int depth, float alpha, float beta
  ) {
    // end of branch
    if (state.isLeaf() || depth <= 0) {
      return state.evaluate();
    }
    int turn = state.isMaxTurn() ? 1 : -1;

    float best = state.isMaxTurn() ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
    for (T child : state.getChildren()) {
      //System.out.println(depth + "  " + child.evaluate() + "\n" + child + "\n");

      // run min max on the child
      float result = evaluate(child, depth - 1, alpha, beta);

      // update best
      if (turn * result > turn * best) {
        // max trun -> result >  
        // | 3 > 3 -> false | 3 > 2 -> true | 3 > 4 -> false
        // min turn -> -result > -best -> result < best 
        // | 3 < 3 -> false | 3 < 2 -> false | 3 < 4 -> true
        // This means that if we multiply by the turn value we can use the same 
        // comparison for both max and min turns.
        best = result;
      }

      // alpha beta pruning
      if (state.isMaxTurn()) {
        alpha = Math.max(alpha, best);
      } else {
        beta = Math.min(beta, best);
      }
      if (beta <= alpha) {
        break;
      }

    }
    return best;
  }

  /**
   * Returns a map of the moves from a given state and their values.
   *
   * @param state the state to run the min max algorithm on
   * @param depth the depth of the min max algorithm
   * @return a map of the moves and their values or null if the state is a leaf or the depth is 
   *      reached
   */
  public static <T extends State<T>> HashMap<T, Float> evaluateChildValue(T state, int depth) {
    if (state.isLeaf() || depth <= 0) {
      return null;
    }
    HashMap<T, Float> moveValues = new HashMap<T, Float>();
    for (T child : state.getChildren()) {
      float result = evaluate(child, depth - 1);
      moveValues.put(child, result);
    }
    return moveValues;
  }
}
