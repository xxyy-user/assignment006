package insurance;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TemplateProcessorTest {

  private String resourcePath(String name) {
    return getClass().getResource("/insurance/" + name).getPath();
  }

  @Test
  void testLoadEmail() throws IOException {
    TemplateProcessor tp = new TemplateProcessor(resourcePath("email-template.txt"));
    String result = tp.process(Map.of(
        "email", "jack@example.com",
        "first_name", "Jack",
        "last_name", "Chen"
    ));
    assertTrue(result.contains("To: jack@example.com"));
    assertTrue(result.contains("Dear Jack Chen,"));
  }

  @Test
  void testLetterTemplate() throws IOException {
    TemplateProcessor tp = new TemplateProcessor(resourcePath("letter-template.txt"));
    String result = tp.process(Map.of(
        "first_name", "Jack",
        "last_name", "Chen",
        "company_name", "ABC, Co Ltd",
        "address", "123 Main St",
        "city", "New York",
        "county", "New York",
        "state", "NY",
        "zip", "10001",
        "email", "jack@example.com"
    ));
    assertTrue(result.contains("ABC, Co Ltd."));
    assertTrue(result.contains("Dear Jack Chen,"));
    assertTrue(result.contains("123 Main St, New York,"));
  }

  @Test
  void testUnknownPlaceholder() throws IOException {
    TemplateProcessor tp = new TemplateProcessor(resourcePath("email-template.txt"));
    String result = tp.process(Map.of());
    assertTrue(result.contains("[[ email ]]") || result.contains("[[email]]"));
  }

  @Test
  void testPlaceholder() throws IOException {
    TemplateProcessor tp = new TemplateProcessor(resourcePath("letter-template.txt"));
    String result = tp.process(Map.of(
        "first_name", "Mary",
        "last_name", "Smith",
        "company_name", "Smith, John Esq",
        "address", "456 Oak Ave",
        "city", "Los Angeles",
        "county", "Los Angeles",
        "state", "CA",
        "zip", "90001",
        "email", "mary@example.com"
    ));
    assertTrue(result.contains("Mary Smith"));
    assertTrue(result.contains("mary@example.com"));
    assertTrue(result.contains("CA"));
  }
}
