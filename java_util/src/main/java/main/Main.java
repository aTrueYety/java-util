package main;

import java.util.Arrays;
import java.util.List;

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
  }
}