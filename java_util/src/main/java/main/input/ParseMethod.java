package main.input;

/**
 * Represents a parse method for the type T.
 *
 * @param <T> the type of the parse method
 * @see main.input.Input
 * @see main.input.Profile
 */
public interface ParseMethod<T> {
  T run(String s);
}
