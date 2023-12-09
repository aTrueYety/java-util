package main.input;

import java.util.Scanner;

/**
 * Responsible for getting input from the user. With the <code>getInput</code> method, the user is
 * prompted to enter a value. The input is parsed if possible and checked if it is in the provided
 * range. If the input is invalid, the user is prompted to enter a new input. This class currently
 * supports predefined profiles for getting input of the following classes: <code>Integer</code>, 
 * <code>Long</code>, <code>Double</code>, but custome profiles can be created as well.
 */
public class Input {
  public static final Profile<Integer> INTEGER = new Profile<>(
      Integer.class, "[0-9]+", Integer::parseInt, (i, a, b) -> i >= a && i <= b);
  public static final Profile<Long> LONG = new Profile<>(
      Long.class, "[0-9]+", Long::parseLong, (i, a, b) -> i >= a && i <= b);
  public static final Profile<Double> DOUBLE = new Profile<>(
      Double.class, "[0-9]+(\\.[0-9]+)?", Double::parseDouble, (i, a, b) -> i >= a && i <= b);
  public static final Profile<Float> FLOAT = new Profile<>(
      Float.class, "[0-9]+(\\.[0-9]+)?", Float::parseFloat, (i, a, b) -> i >= a && i <= b);
  public static final Profile<String> STRING = new Profile<>(
      String.class, ".*", String::toString, (i, a, b) -> i.length() >= a && i.length() <= b);

  /**
   * Gets input from the user. The input is parsed if possible and checked if it is in the provided
   * range. If the input is invalid, the user is prompted to enter a new input. This method uses
   * the provided profile to get the input. The input is checked if it is in the provided range. 
   * 
   * <p>Calling this method can look like this:
   * <pre>
   * {@code
   * int input = Input.getInput(Input.INTEGER, new Scanner(System.in), 0, 10, "Enter a number");
   * }
   * </pre>
   * The above code will prompt the user to enter a number between 0 and 10. If the user enters a
   * number that is not in the range, the user will be prompted to enter a new number. The
   * console output will look like this:
   * <p>
   * Enter a number ( Integer in range [0, 10] )
   * </p></p>
   * 
   *
   * @param <T> the type of input requested
   * @param profile the profile to use of type T
   * @param input the scanner to use for getting input
   * @param min the minimum value of the input according to the profile range check
   * @param max the maximum value of the input according to the profile range check
   * @param message the message to print before requesting input
   */
  public static <T> T getInput(
      Profile<T> profile, Scanner input, int min, int max, String message
  ) {
    while (true) {
      // Print message
      System.out.println(
          message + " ( " + profile.getTarget().getSimpleName() + " in range [" + min + ", " + max
              + "] )");

      T parsed = fetchInput(profile, input);

      // Check if input is in range
      if (profile.isInRange(parsed, min, max)) {
        // return parsed input
        return parsed;
      }
    }
  }

  /**
   * Gets input from the user. The input is parsed if possible and checked if it is in the provided
   * range. If the input is invalid, the user is prompted to enter a new input. This method uses
   * the provided profile to get the input.
   * 
   * <p>Calling this method can look like this:
   * <pre>
   * {@code
   * int input = Input.getInput(Input.INTEGER, new Scanner(System.in), 0, 10, "Enter a number");
   * }
   * </pre>
   * The above code will prompt the user to enter a number between 0 and 10. If the user enters a
   * number that is not in the range, the user will be prompted to enter a new number. The
   * console output will look like this:
   * <p>
   * Enter a number ( Integer )
   * </p></p>
   *
   * @param <T> the type of input requested
   * @param profile the profile to use of type T
   * @param input the scanner to use for getting input
   * @param message the message to print before requesting input
   */
  public static <T> T getInput(
      Profile<T> profile, Scanner input, String message
  ) {
    // Print message
    System.out.println(
        message + " ( " + profile.getTarget().getSimpleName() + " )");

    return fetchInput(profile, input);
  }

  /**
   * A sub method of <code>getInput</code> that fetches the actual input from the user. The input is
   * parsed if possible. If the input is invalid, the user is prompted to enter a new input. This
   * method uses the provided profile to get the input.
   */
  private static <T> T fetchInput(Profile<T> profile, Scanner input) {
    String rawIn;
    System.out.println("");
    while (true) {
      System.out.print("\033[1A"); // Move up in console
      System.out.print("\033[2K"); // Erase line content in console

      // Request input
      rawIn = input.nextLine();

      // Check if input can be parsed
      if (profile.canParse(rawIn)) {
        // Parse input
        T parsed = profile.parse(rawIn);
        return parsed;
      }
    }
  }
}