package main;

/**
 * <h2>Validate</h2>
 * The <code>Validate</code> class provides static methods for validating arguments. The validation 
 * is performed by passing a custom check to the <code>that</code> method or by passing in a 
 * predefined check. Note that the predefined ckecks cast the object to a predefined type and the 
 * result may therefore need to be cast back to the original type. If the argument is invalid, an 
 * IllegalArgumentException is thrown, the message will be formatted as follows:
 * <p>
 * <code>Illegal argument 'name': value</code>
 * </p>
 * The message may be proceeded by a custom message if the check fails. The message will then be
 * formatted as follows:
 * <p>
 * <code>Illegal argument 'name': value , message</code>
 * </p>
 * <h3>Responsibilities:</h3>
 * <ul>
 *    <li>Validate arguments.</li>
 *    <li>Throw a comprehencsive exception if the argument is invalid.</li>
 * </ul>
 * <h1></h1>
 *
 * @author Erik Hoff
 * @version 1.0
 * @since 1.0
 */
public class Validate {
  public static final CheckArgument<Object> isNotNull = arg -> arg != null;
  public static final CheckArgument<Integer> isPositive = arg -> arg > 0;
  public static final CheckArgument<Integer> isNegative = arg -> arg < 0;
  public static final CheckArgument<Integer> isNotPositive = arg -> arg <= 0;
  public static final CheckArgument<Integer> isNotNegative = arg -> arg >= 0;
  public static final CheckArgument<Integer> isZero = arg -> arg == 0;
  public static final CheckArgument<String> isEmpty = arg -> arg.isEmpty();
  public static final CheckArgument<String> isBlank = arg -> arg.isBlank();

  /**
   * The <code>CheckArgument</code> interface represents a function that accepts an object and 
   * returns a boolean.
   */
  public interface CheckArgument<T> {
    public boolean test(T object);
  }

  /**
   * Checks if the argument is valid according to the given check. If the argument is invalid, an 
   * IllegalArgumentException is thrown.
   *
   * @param argument the argument to check
   * @param name the name of the argument
   * @param check the check to perform
   * @return the argument if it is valid
   * @throws IllegalArgumentException if the argument is invalid
   */
  public static <T> T that(T argument, String name, CheckArgument<T> check) {
    if (!check.test(argument)) {
      throw new IllegalArgumentException("Illegal argument '" + name + "': " + argument);
    }
    return argument;
  }

  /**
   * Checks if the argument is valid according to the given check. If the argument is invalid, an 
   * IllegalArgumentException is thrown.
   *
   * @param argument the argument to check
   * @param name the name of the argument
   * @param check the check to perform
   * @param message the message to display if the argument is invalid
   * @return the argument if it is valid
   * @throws IllegalArgumentException if the argument is invalid
   */
  public static <T> T that(T argument, String name, CheckArgument<T> check, String message) {
    if (!check.test(argument)) {
      throw new IllegalArgumentException(
        "Illegal argument '" + name + "': " + argument + " , " + message
      );
    }
    return argument;
  }
}
