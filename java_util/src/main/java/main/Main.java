package main;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import main.minmax.FourInaRowState;
import main.minmax.MinMax;
import main.minmax.ThreeInaRowState;


/**
 * Main class for testing. 
 */
public class Main {
  /**
   * Main method.
   *
   * @param a command line arguments
   */
  public static void main(String[] a) {
    System.out.println("Hello world!");
      // Test Validate with a complex custom test
      {
      Integer[] temp = { 2, 3, 4, 5, 6, 7, 5, 9 };
      final List<Integer> nums = Arrays.asList(temp);
      Validate.CheckArgument<Integer> noDuplicates = (args) -> {
        return args.stream().distinct().count() == args.size();
      };
      try {
        Validate.that("nums", noDuplicates, "contains duplicates", nums);
      } catch (Exception e) {
        System.out.println("Success!");
      }
      }

      // Test Validate with a simple custom test
      {
      Integer[] temp = { 2, 3, 4, 5, 6, 7, 8, 9 };
      final List<Integer> nums = Arrays.asList(temp);
      Validate.CheckArgumentSimple<Integer> aboveTwo = (arg) -> {
        return arg > 2;
      };
      try {
        Validate.that("nums", Validate.makeCheck(aboveTwo), "contains duplicates", nums);
      } catch (Exception e) {
        System.out.println("Success!");
      }
      }

      // Test minmax with three in a row
      {
      // Arrange
      int[][] board = {
        { 1, 0, 0 },
        { 0, 2, 0 },
        { 0, 0, 1 }
      };
      int pieces = 0;
      for (int[] row : board) {
        for (int piece : row) {
          if (piece != 0) {
            pieces++;
          }
        }
      }
      boolean isMaxTurn = pieces % 2 == 0;
      ThreeInaRowState state = new ThreeInaRowState(board, isMaxTurn);
      System.out.println(state);
      // Act
      float start = System.nanoTime();
      float result = MinMax.evaluate(state, 9);
      float end = System.nanoTime();
      // Assert
      System.out.println("Best result (" + (end - start) + "):");
      System.out.println(result);

      // Act
      start = System.nanoTime();
      HashMap<ThreeInaRowState, Float> results = MinMax.evaluateChildValue(state, 9);
      end = System.nanoTime();
      // Assert
      System.out.println("Result (" + (end - start) + "):");
      for (ThreeInaRowState key : results.keySet()) {
        System.out.println(key);
        System.out.println(results.get(key) + "\n");
      }
      }

      // Test minmax with four in a row
      {
        // Arrange
        FourInaRowState test = new FourInaRowState(0L, 0L, true);
        System.out.println(test);
        // Act
        float start = System.nanoTime();
        float result = MinMax.evaluate(test, 12);
        float end = System.nanoTime();
        // Assert
        System.out.println("Best result (" + (end - start) + "):");
        System.out.println(result);

        // Act
        start = System.nanoTime();
        HashMap<FourInaRowState, Float> results = MinMax.evaluateChildValue(test, 11);
        end = System.nanoTime();
        // Assert
        System.out.println("Result (" + (end - start) + "):");
        for (FourInaRowState key : results.keySet()) {
          System.out.println(key);
          System.out.println(results.get(key) + "\n");
        }
      }
  }
}