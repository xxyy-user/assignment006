package insurance;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CSVParserTest {

  private String resourcePath(String name) {
    return getClass().getResource("/insurance/" + name).getPath();
  }

  @Test
  void testParseRealCSV() throws IOException {
    List<Map<String, String>> result = new CSVParser(resourcePath("sample.csv")).parseCSV();
    assertEquals(3, result.size());
    assertEquals("\"Jack\"", result.get(0).get("first_name"));
    assertEquals("Chen", result.get(0).get("last_name"));
    assertEquals("jack@example.com", result.get(0).get("email"));
  }

  @Test
  void testFieldWithComma() throws IOException {
    List<Map<String, String>> result = new CSVParser(resourcePath("sample.csv")).parseCSV();
    // "Benton, John B Jr"
    assertEquals("ABC, Co Ltd", result.get(0).get("company_name"));
  }

  @Test
  void testHeader() throws IOException {
    List<Map<String, String>> result = new CSVParser(resourcePath("sample.csv")).parseCSV();
    Map<String, String> first = result.get(0);
    assertTrue(first.containsKey("first_name"));
    assertTrue(first.containsKey("last_name"));
    assertTrue(first.containsKey("company_name"));
    assertTrue(first.containsKey("address"));
    assertTrue(first.containsKey("city"));
    assertTrue(first.containsKey("state"));
    assertTrue(first.containsKey("zip"));
    assertTrue(first.containsKey("email"));
  }

  @Test
  void testMultipleRows() throws IOException {
    List<Map<String, String>> result = new CSVParser(resourcePath("sample.csv")).parseCSV();
    assertEquals("Tom", result.get(2).get("first_name"));
    assertEquals("Brown", result.get(2).get("last_name"));
    assertEquals("tom@example.com", result.get(2).get("email"));
  }

  @Test
  void testSplitCSVLine() {
    CSVParser parser = new CSVParser("");
    List<String> fields = parser.splitCSVLine("\"foo\",\"bar\",\"baz\"");
    assertEquals(List.of("foo", "bar", "baz"), fields);
  }

  @Test
  void testSplitCSVLineWithCommaAndQuotes() {
    CSVParser parser = new CSVParser("");
    List<String> fields = parser.splitCSVLine("\"\"\"Art\"\"\",\"Chemel, James L Cpa\"");
    assertEquals(List.of("\"Art\"", "Chemel, James L Cpa"), fields);
  }
}
