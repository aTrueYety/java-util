package main.input;

import java.util.regex.Pattern;

/**
 * Represents a input profile for the type T. A profile contains a regex, a parse method and a
 * range check. This class is used to store the information for the <code>getInput</code> method.
 * The regex is used to check if the input is valid. The parse method is used to parse the input
 * into the type T. The range check is used to check if the input is in the provided range. If no
 * range check is provided, the input is always considered to be in range.
 *
 * @param <T> the type of the profile
 * @see main.input.Input
 */
public class Profile<T> {
  private final Class<T> target;
  private final String regex;
  private final ParseMethod<T> parseMethod;
  private final RangeCheck<T> rangeCheck;

  Profile(
      Class<T> target, String regex, ParseMethod<T> parseMethod, RangeCheck<T> rangeCheck
  ) {
    this.target = target;
    this.regex = regex;
    this.rangeCheck = rangeCheck;
    this.parseMethod = parseMethod;
  }

  Profile(Class<T> target, String regex, ParseMethod<T> parseMethod) {
    this.target = target;
    this.regex = regex;
    this.rangeCheck = (t, a, b) -> true;
    this.parseMethod = parseMethod;
  }

  public Class<T> getTarget() {
    return target;
  }

  public String getRegex() {
    return regex;
  }

  public ParseMethod<T> getParse() {
    return parseMethod;
  }

  public RangeCheck<T> getRange() {
    return rangeCheck;
  }

  public boolean canParse(String s) {
    return Pattern.matches(regex, s);
  }

  public boolean isInRange(T t, int a, int b) {
    return rangeCheck.run(t, a, b);
  }

  public T parse(String s) {
    return parseMethod.run(s);
  }
}
