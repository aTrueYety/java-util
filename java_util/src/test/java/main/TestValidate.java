package main;


import static main.Validate.makeCheck;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import main.Validate.CheckArgument;
import main.Validate.CheckArgumentSimple;
import org.junit.Test;

/**
 * Test class for Validate.
 */
public class TestValidate {
  // Arrange
  Integer argument = 5;
  List<Integer> arguments = Arrays.asList(1, 2, 3, 4, 5);
  List<Integer> argumentsNegative = Arrays.asList(1, 2, -3, 4, 5);
  String name = "argument";
  CheckArgumentSimple<Integer> check = arg -> arg > 0;
  CheckArgument<Integer> check2 = makeCheck(arg -> arg > 0);
  String message = "Number must be positive";

  @Test
  public void testThat_1_expected() {
    // Act
    Integer result = Validate.that(argument, check, message);

    // Assert
    assertEquals(argument, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_1_negative() {
    // Act
    Validate.that(-argument, check, message);
  }

  @Test
  public void testThat_2_expected() {
    // Act
    Integer result = Validate.that(argument, check);

    // Assert
    assertEquals(argument, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_2_negative() {
    // Act
    Validate.that(-argument, check);
  }

  @Test
  public void testThat_3_expected() {
    // Act
    Integer result = Validate.that(argument, name, check);

    // Assert
    assertEquals(argument, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_3_negative() {
    // Act
    Validate.that(-argument, name, check);
  }

  @Test
  public void testThat_4_expected() {
    // Act
    Integer result = Validate.that(argument, name, check, message);

    // Assert
    assertEquals(argument, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_4_negative() {
    // Act
    Validate.that(-argument, name, check, message);
  }

  @Test
  public void testThat_5_expected() {
    // Act
    Integer result = Validate.that(check2, message, arguments);

    // Assert
    assertEquals(arguments.get(0), result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_5_negative() {
    // Act
    Validate.that(check2, message, argumentsNegative);
  }

  @Test
  public void testThat_6_expected() {
    // Act
    Integer result = Validate.that(check2, arguments);

    // Assert
    assertEquals(arguments.get(0), result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_6_negative() {
    // Act
    Validate.that(check2, argumentsNegative);
  }

  @Test
  public void testThat_7_expected() {
    // Act
    Integer result = Validate.that(name, check2, arguments);

    // Assert
    assertEquals(arguments.get(0), result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_7_negative() {
    // Act
    Validate.that(name, check2, argumentsNegative);
  }

  @Test
  public void testThat_8_expected() {
    // Act
    Integer result = Validate.that(name, check2, message, arguments);

    // Assert
    assertEquals(arguments.get(0), result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_8_negative() {
    // Act
    Validate.that(name, check2, message, argumentsNegative);
  }
  
}