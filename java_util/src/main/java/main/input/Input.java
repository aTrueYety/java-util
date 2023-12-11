package main.input;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Responsible for getting input from the user. With the <code>getInput</code>
 * method, the user is
 * prompted to enter a value. The input is parsed if possible and checked if it
 * is in the provided
 * range. If the input is invalid, the user is prompted to enter a new input.
 * This class currently
 * supports predefined profiles for getting input of the following classes:
 * <code>Integer</code>,
 * <code>Long</code>, <code>Double</code>, but custome profiles can be created
 * as well.
 */
public class Input {
  private Input() {
  }

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
   * Gets input from the user. The input is parsed if possible and checked if it
   * is in the provided
   * range. If the input is invalid, the user is prompted to enter a new input.
   * This method uses
   * the provided profile to get the input. The input is checked if it is in the
   * provided range.
   * 
   * <p>
   * Calling this method can look like this:
   * 
   * <pre>
   * {@code
   * int input = Input.getInput(Input.INTEGER, System.in, 0, 10, "Enter a number");
   * }
   * </pre>
   * 
   * The above code will prompt the user to enter a number between 0 and 10. If
   * the user enters a
   * number that is not in the range, the user will be prompted to enter a new
   * number. The
   * console output will look like this:
   * <p>
   * Enter a number ( Integer in range [0, 10] )
   * </p>
   * </p>
   * 
   *
   * @param <T>     the type of input requested
   * @param profile the profile to use of type T
   * @param in      The inputstream to read input from
   * @param min     the minimum value of the input according to the profile range
   *                check
   * @param max     the maximum value of the input according to the profile range
   *                check
   * @param message the message to print before requesting input
   */
  public static <T> T getInput(
      Profile<T> profile, InputStream in, int min, int max, String message) {
    while (true) {
      // Print message
      System.out.println(
          // TODO: include simple name in profile
          message + " ( " + profile.getTarget().getSimpleName() + " in range [" + min + ", " + max
              + "] )");

      T parsed = fetchInput(profile, in);

      // Check if input is in range
      if (profile.isInRange(parsed, min, max)) {
        // return parsed input
        return parsed;
      }
    }
  }

  /**
   * Gets input from the user. The input is parsed if possible and checked if it
   * is in the provided
   * range. If the input is invalid, the user is prompted to enter a new input.
   * This method uses
   * the provided profile to get the input. The input is checked if it is in the
   * provided range.
   * 
   * <p>
   * Calling this method can look like this:
   * 
   * <pre>
   * {@code
   * int input = Input.getInput(Input.INTEGER, 0, 10, "Enter a number");
   * }
   * </pre>
   * 
   * The above code will prompt the user to enter a number between 0 and 10. If
   * the user enters a
   * number that is not in the range, the user will be prompted to enter a new
   * number. The
   * console output will look like this:
   * <p>
   * Enter a number ( Integer in range [0, 10] )
   * </p>
   * </p>
   * 
   *
   * @param <T>     the type of input requested
   * @param profile the profile to use of type T
   * @param min     the minimum value of the input according to the profile range
   *                check
   * @param max     the maximum value of the input according to the profile range
   *                check
   * @param message the message to print before requesting input
   */

  public static <T> T getInput(
      Profile<T> profile, int min, int max, String message) {
    return getInput(profile, System.in, min, max, message);
  }

  /**
   * Gets input from the user. The input is parsed if possible and checked if it
   * is in the provided range. If the input is invalid, the user is prompted to
   * enter a new input.
   * This method uses
   * the provided profile and System.in to get the input.
   * 
   * <p>
   * Calling this method can look like this:
   * 
   * <pre>
   * {@code
   * int input = Input.getInput(Input.INTEGER, new Scanner(System.in), 0, 10, "Enter a number");
   * }
   * </pre>
   * 
   * The above code will prompt the user to enter a number between 0 and 10. If
   * the user enters a
   * number that is not in the range, the user will be prompted to enter a new
   * number. The
   * console output will look like this:
   * <p>
   * Enter a number ( Integer )
   * </p>
   * </p>
   *
   * @param <T>     the type of input requested
   * @param profile the profile to use of type T
   * @param input   the scanner to use for getting input
   * @param message the message to print before requesting input
   */
  public static <T> T getInput(
      Profile<T> profile, InputStream in, String message) {
    // Print message
    System.out.println(
        message + " ( " + profile.getTarget().getSimpleName() + " )");

    return fetchInput(profile, in);
  }

  /**
   * A sub method of <code>getInput</code> that fetches the actual input from the
   * user. The input is
   * parsed if possible. If the input is invalid, the user is prompted to enter a
   * new input. This
   * method uses the provided profile to get the input.
   */
  private static <T> T fetchInput(Profile<T> profile, InputStream input) {
    String rawIn;
    System.out.println("");
    while (true) {
      System.out.print("\033[1A"); // Move up in console
      System.out.print("\033[2K"); // Erase line content in console

      // Request input
      rawIn = readNextLine(input);

      // Check if input can be parsed
      if (profile.canParse(rawIn)) {
        // Parse input
        T parsed = profile.parse(rawIn);
        return parsed;
      }
    }
  }

  /**
   * Reads the next line from the input stream.
   * 
   * 'next line' is defined as a sequence of characters followed by a newline
   * character ('\n')
   * 
   * @param in The input stream to read from
   * @return Line read from the input stream
   */
  private static final String readNextLine(InputStream in) {
    try {
      char[] cArr = readNext(in);

      String s = new String(cArr);
      return s.substring(0, s.length() - 1);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * Reads the next line from the input stream.
   * 
   * 'next line' is defined as a sequence of characters followed by a newline
   * character ('\n')
   * 
   * @param in The input stream to read from
   * @return Char[] containing the line read from the input stream. Includes the
   */
  private static final char[] readNext(InputStream inputStream) {
    return readNext(inputStream, '\n');
  }

  /**
   * Reads from the imput stream until it either reaches the end of the stream or
   * finds the delimiter.
   * 
   * 
   * @param in        The input stream to read from
   * @param delimiter The delimiter to stop reading at
   * @return Char[] containing the line read from the input stream. Includes the
   *         delimiter.
   */
  private static final char[] readNext(InputStream inputStream, char delimiter) {
    try {
      Readable in = new InputStreamReader(inputStream, StandardCharsets.UTF_8); // create a readable stream from the
                                                                                // input stream
      int readAmount = 1;
      int read = 0;
      CharBuffer b = CharBuffer.allocate(1024);
      b.limit(readAmount);
      boolean readAll = false;

      while (!readAll) {
        int v = in.read(b);

        // if the last byte is the delimiter, then we have read the whole line
        if (b.get(read + readAmount - 1) == delimiter) {
          readAll = true;
        } else if (v < readAmount) {
          readAll = true;
        } else if (b.get(read + readAmount - 1) == 0) {
          readAll = true;
        } else {
          // if not, then increase the size of the the buffer
          read += readAmount;
          b.limit(read + readAmount);
          b.position(read);
        }
      }

      // return the buffer as an array. The array is resized to remove unused space
      return Arrays.copyOfRange(b.array(), 0, b.limit() - 1);
    } catch (Exception e) {
      e.printStackTrace();
      return new char[0];
    }
  }
}