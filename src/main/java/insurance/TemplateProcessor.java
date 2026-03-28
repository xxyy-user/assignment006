package insurance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Processes a template file by replacing all placeholders of the form
 */
public class TemplateProcessor {
  private final String templateContent;
  public TemplateProcessor(String templatePath) throws IOException {
    this.templateContent = new String(Files.readAllBytes(Paths.get(templatePath)));
  }

  public TemplateProcessor(String templateContent, boolean hasContent) {
    this.templateContent = templateContent;
  }

  /**
   * Processes the template by replacing all [[placeholder]] tokens with values
   *
   * @param map map of column header names to field values each line
   * @return the processed string with all known placeholders replaced
   */
  public String process(Map<String, String> map) {
    StringBuilder sb = new StringBuilder(templateContent);
    int start = sb.indexOf("[[");
    while (start != -1) {
      int end = sb.indexOf("]]", start);
      if (end == -1) break;
      String key = sb.substring(start + 2, end);
      String value = map.getOrDefault(key, "[[ " + key + " ]]");
      sb.replace(start, end + 2, value);
      // start from next one "[["
      start = sb.indexOf("[[");
    }
    return sb.toString();
  }
}
