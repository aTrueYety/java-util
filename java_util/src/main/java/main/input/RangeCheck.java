package main.input;

/**
 * Represents a range check for the type T.
 *
 * @param <T> the type of the range check
 * @see main.input.Input
 * @see main.input.Profile
 */
public interface RangeCheck<T> {
  boolean run(T t, int a, int b);
}
