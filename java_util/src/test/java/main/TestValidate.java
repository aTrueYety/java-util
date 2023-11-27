package main;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test class for Validate.
 */
public class TestValidate {
  @Test
  public void testThat_1_expected() {
    assertEquals((int) 1, (int) Validate.that(1, "argument", Validate.isPositive));
    assertEquals((int) 2, (int) Validate.that(2, "argument", arg -> arg > 0));
  }

  @Test
  public void testThat_2_expected() {
    assertEquals((int) 1, (int) Validate.that(1, "argument", Validate.isPositive, "message"));
    assertEquals((int) 2, (int) Validate.that(2, "argument", arg -> arg > 0, "message"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_1_negative_1() {
    Validate.that(0, "argument", Validate.isPositive);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_1_negative_2() {
    Validate.that(0, "argument", arg -> arg > 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_2_negative_1() {
    Validate.that(0, "argument", Validate.isPositive, "message");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThat_2_negative_2() {
    Validate.that(0, "argument", arg -> arg > 0, "message");
  }
}