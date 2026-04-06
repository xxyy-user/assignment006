package insurance;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ArgumentParser.
 * Note: invalid argument tests are limited because System.exit()
 * terminates the JVM, so we only test valid args.
 */
public class ArgumentParserTest {

  /**
   * Tests parsing with all valid args (both email and letter).
   */
  @Test
  public void testAllValidArgs() {
    String[] args = {
        "--email", "--email-template", "email.txt",
        "--letter", "--letter-template", "letter.txt",
        "--output-dir", "output",
        "--csv-file", "data.csv"
    };
    ArgumentParser parser = new ArgumentParser(args);

    assertTrue(parser.isGenerateEmail());
    assertTrue(parser.isGenerateLetter());
    assertEquals("email.txt", parser.getEmailTemplatePath());
    assertEquals("letter.txt", parser.getLetterTemplatePath());
    assertEquals("output", parser.getOutputDirPath());
    assertEquals("data.csv", parser.getCsvFilePath());
  }

  /**
   * Tests parsing with only email arguments.
   */
  @Test
  public void testEmailOnly() {
    String[] args = {
        "--email", "--email-template", "email.txt",
        "--output-dir", "output",
        "--csv-file", "data.csv"
    };
    ArgumentParser parser = new ArgumentParser(args);

    assertTrue(parser.isGenerateEmail());
    assertFalse(parser.isGenerateLetter());
    assertEquals("email.txt", parser.getEmailTemplatePath());
    assertNull(parser.getLetterTemplatePath());
  }

  /**
   * Tests parsing with only letter arguments.
   */
  @Test
  public void testLetterOnly() {
    String[] args = {
        "--letter", "--letter-template", "letter.txt",
        "--output-dir", "output",
        "--csv-file", "data.csv"
    };
    ArgumentParser parser = new ArgumentParser(args);

    assertFalse(parser.isGenerateEmail());
    assertTrue(parser.isGenerateLetter());
    assertNull(parser.getEmailTemplatePath());
    assertEquals("letter.txt", parser.getLetterTemplatePath());
  }

  /**
   * Tests that arguments can be in any order.
   */
  @Test
  public void testArgsInDifferentOrder() {
    String[] args = {
        "--csv-file", "data.csv",
        "--output-dir", "output",
        "--email-template", "email.txt",
        "--email"
    };
    ArgumentParser parser = new ArgumentParser(args);

    assertTrue(parser.isGenerateEmail());
    assertEquals("data.csv", parser.getCsvFilePath());
    assertEquals("output", parser.getOutputDirPath());
    assertEquals("email.txt", parser.getEmailTemplatePath());
  }
}