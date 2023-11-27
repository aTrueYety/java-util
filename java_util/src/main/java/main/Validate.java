package main;

import java.util.Collection;

/**
 * <h2>Validate</h2>
 * The <code>Validate</code> class provides static methods for validating arguments. The validation 
 * is performed by passing a custom check to the <code>that</code> method or by passing in a 
 * predefined check. Note that the predefined ckecks cast the object to a predefined type and the 
 * result may therefore need to be cast back to the original type. The <code>that</code> method can
 * be used to validate a single argument or a collection of arguments. The <code>that</code> method
 * will return the first argument if all arguments are valid. If the arguments are invalid, an
 * IllegalArgumentException is thrown and the message will be formatted as described in the JavaDoc
 * of the method. When passing in colections, the arguments can be of a unknown quantity, but the 
 * first argument will be used in the exception message and returned if all arguments are valid. 
 * This means that all the arguments are checked and complex checks can be performed on the 
 * arguments. If the same check is performed on all arguments, the <code>makeCheck</code> method can
 * be used to simplify the creation of the collection checks. The arguments can be passed in as any 
 * type of collection, but the since the first argument is used in the exception message and return,
 * the collection should idealy be ordered.
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
  // ------------------------------------
  // -        Predefined checks         -
  // ------------------------------------
  public static final CheckArgumentSimple<Object> isNotNull = 
      (Object arg) -> arg != null;
  public static final CheckArgumentSimple<Integer> isPositive = 
      (Integer arg) -> arg > 0;
  public static final CheckArgumentSimple<Integer> isNegative = 
      (Integer arg) -> arg < 0;
  public static final CheckArgumentSimple<Integer> isNotPositive = 
      (Integer arg) -> arg <= 0;
  public static final CheckArgumentSimple<Integer> isNotNegative = 
      (Integer arg) -> arg >= 0;
  public static final CheckArgumentSimple<Integer> isZero = 
      (Integer arg) -> arg == 0;
  public static final CheckArgumentSimple<String> isEmpty = 
      (String arg) -> arg != null && arg.isEmpty();
  public static final CheckArgumentSimple<String> isBlank = 
      (String arg) -> arg != null && arg.isBlank();
  public static final CheckArgumentSimple<String> hasContent = 
      (String arg) -> !arg.isEmpty() && !arg.isBlank();
  public static final CheckArgumentSimple<String> isNullEmptyOrBlank = 
      (String arg) -> arg == null || arg.isEmpty() || arg.isBlank();
  

  // ------------------------------------
  // -            Intefaces             -
  // ------------------------------------
  /**
   * The <code>CheckArgument</code> interface represents a function that accepts objects and 
   * returns a boolean.
   */
  public interface CheckArgument<T> {
    public boolean test(Collection<T> objects);
  }

  /**
   * The <code>CompareArgument</code> interface represents a function that accepts a object and 
   * returns a boolean.
   */
  public interface CheckArgumentSimple<T> {
    public boolean test(T object1);
  }

  /**
   * Constructs a new CheckArgument of type T from a CheckArgumentSimple of type T. This simplifies
   * the creation of new CheckArgument objects when the check is the same for all arguments.
   *
   * @param <T> the type of the argument
   * @param check the check to perform on each argument
   * @return a CheckArgument of type T with the given check
   */
  public static <T> CheckArgument<T> makeCheck(CheckArgumentSimple<T> check) {
    return (Collection<T> args) ->  {
      for (T arg : args) {
        if (!check.test(arg)) {
          return false;
        }
      }
      return true;
    };
  }

  // ------------------------------------
  // -        Single arguments          -
  // ------------------------------------
  /**
   * Checks if the argument is valid according to the given check. If the argument is valid,
   * then it is returned. If the argument is invalid, an IllegalArgumentException is thrown
   * with the provided message.
   *
   * @param <T> the type of the argument
   * @param argument the argument to check
   * @param check the check to perform on the argument
   * @param message the message to display if the argument is invalid
   * @return the argument if it is valid
   * @throws IllegalArgumentException if the argument is invalid
   */
  public static <T> T that(T argument, CheckArgumentSimple<T> check, String message)
      throws IllegalArgumentException {
    if (!check.test(argument)) {
      throw new IllegalArgumentException(message);
    }
    return argument;
  }

  /**
   * Checks if the argument is valid according to the given check. If the argument is valid,
   * then it is returned. If the argument is invalid, an IllegalArgumentException is thrown
   * with the provided message.
   *
   * @param <T> the type of the argument
   * @param argument the argument to check
   * @param check the check to perform on the argument
   * @return the argument if it is valid
   * @throws IllegalArgumentException if the argument is invalid
   */
  public static <T> T that(T argument, CheckArgumentSimple<T> check) 
      throws IllegalArgumentException {
    return that(argument, check, "Illegal argument: " + argument);
  }

  /**
   * Checks if the argument is valid according to the given check. If the argument is valid,
   * then it is returned. If the argument is invalid, an IllegalArgumentException is thrown
   * with the provided message.
   *
   * @param <T> the type of the argument
   * @param argument the argument to check
   * @param name the name of the argument, used in the exception message
   * @param check the check to perform on the argument
   * @return the argument if it is valid
   * @throws IllegalArgumentException if the argument is invalid
   */
  public static <T> T that(T argument, String name, CheckArgumentSimple<T> check)
      throws IllegalArgumentException {
    return that(argument, check, "Illegal argument '" + name + "': " + argument);
  }

  /**
   * Checks if the argument is valid according to the given check. If the argument is valid,
   * then it is returned. If the argument is invalid, an IllegalArgumentException is thrown
   * with the provided message.
   *
   * @param <T> the type of the argument
   * @param argument the argument to check
   * @param name the name of the argument, used in the exception message
   * @param check the check to perform on the argument
   * @param message the message to display if the argument is invalid
   * @return the argument if it is valid
   * @throws IllegalArgumentException if the argument is invalid
   */
  public static <T> T that(
      T argument, String name, CheckArgumentSimple<T> check, String message)
      throws IllegalArgumentException {
    return that(argument, check, "Illegal argument '" + name + "': " + argument + " , " + message);
  }

  // ------------------------------------
  // -       Multiple arguments         -
  // ------------------------------------
  /**
   * Checks if the arguments is valid according to the given check. If the arguments are valid
   * then the first argument is returned. If the arguments are invalid, an IllegalArgumentException 
   * is thrown with the provided message. The exception will look like this:
   * <p>
   * <code>'message'</code>
   * </p>
   *
   * @param <T> the type of the argument
   * @param arguments the arguments to check
   * @param check the check to perform on the arguments
   * @return the first argument if all arguemnts are valid
   * @throws IllegalArgumentException if any of the arguments are invalid
   */
  public static <T> T that(CheckArgument<T> check, String message, Collection<T> arguments) 
      throws IllegalArgumentException {
    if (!check.test(arguments)) {
      throw new IllegalArgumentException(message);
    }
    return arguments.iterator().next();
  }

  /**
   * Checks if the arguments is valid according to the given check. If the arguments are valid
   * then the first argument is returned. If the arguments are invalid, an IllegalArgumentException 
   * is thrown with the provided message. The exception will look like this:
   * <p>
   * <code>Illegal argument: 'arguments[0]''</code>
   * </p>
   *
   * @param <T> the type of the arguments
   * @param arguments the arguments to check
   * @param check the check to perform on the arguments
   * @return the first argument if all arguments are all valid
   * @throws IllegalArgumentException if any of the arguments are invalid
   */
  public static <T> T that(CheckArgument<T> check, Collection<T> arguments) 
      throws IllegalArgumentException {
    return that(check, "Illegal argument: " + arguments.iterator().next(), arguments);
  }

  /**
   * Checks if the arguments is valid according to the given check. If the arguments are valid
   * then the first argument is returned. If the arguments are invalid, an IllegalArgumentException 
   * is thrown with the provided message. The exception will look like this:
   * <p>
   * <code>Illegal argument 'name': 'arguments[0]'</code>
   * </p>
   *
   * @param <T> the type of the arguments
   * @param arguments the arguments to check
   * @param name the name of the arguments, used in the exception message
   * @param check the check to perform on the arguments
   * @return the first argument if all arguments are all valid
   * @throws IllegalArgumentException if any of the arguments are invalid
   */
  public static <T> T that(String name, CheckArgument<T> check, Collection<T> arguments) 
      throws IllegalArgumentException {
    return that(
      check, "Illegal argument '" + name + "': " + arguments.iterator().next(), arguments);
  }

  /**
   * Checks if the arguments is valid according to the given check. If the arguments are valid
   * then the first argument is returned. If the arguments are invalid, an IllegalArgumentException 
   * is thrown with the provided message. The exception will look like this:
   * <p>
   * <code>Illegal argument 'name': 'arguments[0]' , 'message'</code>
   * </p>
   *
   * @param <T> the type of the arguments
   * @param arguments the arguments to check
   * @param name the name of the arguments, used in the exception message
   * @param check the check to perform on the arguments
   * @param message the following message to display if the argument is invalid
   * @return the first argument if all arguments are all valid
   * @throws IllegalArgumentException if any of the arguments are invalid
   */
  public static <T> T that(
      String name, CheckArgument<T> check, String message, Collection<T> arguments)
      throws IllegalArgumentException {
    return 
      that(
        check, 
        "Illegal argument '" + name + "': " + arguments.iterator().next() + " , " + message, 
        arguments
      );
  }
}
