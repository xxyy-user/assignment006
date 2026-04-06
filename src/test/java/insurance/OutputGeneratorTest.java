package insurance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for OutputGenerator.
 */
public class OutputGeneratorTest {

  private final String template = "Hello [[name]]";
  private final List<Map<String, String>> csvTestData = new ArrayList<>();

  // creates 3 test customers with names Customer1, Customer2, Customer3
  {
    for (int i = 1; i <= 3; i++) {
      Map<String, String> row = new HashMap<>();
      row.put("name", "Customer" + i);
      csvTestData.add(row);
    }
  }

  /**
   * Writes the template string "Hello [[name]]" to a temp file and returns a TemplateProcessor.
   *
   * @param tempDir the temporary directory to write the file in
   * @return a TemplateProcessor loaded with the template content
   * @throws IOException if the file cannot be written
   */
  private TemplateProcessor createProcessor(Path tempDir) throws IOException {
    File templateFile = new File(tempDir.toFile(), "template.txt");
    try (FileWriter writer = new FileWriter(templateFile)) {
      writer.write(template);
    }
    return new TemplateProcessor(templateFile.getPath());
  }

  /**
   * Tests file count, file naming, and file content.
   */
  @Test
  public void testGenerateCreatesFilesWithCorrectContent(@TempDir Path tempDir) throws IOException {
    TemplateProcessor processor = createProcessor(tempDir);
    OutputGenerator generator = new OutputGenerator(tempDir.toString(), processor);
    generator.generate(csvTestData, "email");

    // check that 3 files were created (plus template.txt = 4 files)
    assertEquals(4, tempDir.toFile().listFiles().length);

    // check file names
    assertTrue(new File(tempDir.toFile(), "email_1.txt").exists());
    assertTrue(new File(tempDir.toFile(), "email_2.txt").exists());
    assertTrue(new File(tempDir.toFile(), "email_3.txt").exists());

    // check file content
    String content = new String(Files.readAllBytes(
        new File(tempDir.toFile(), "email_1.txt").toPath()));
    assertEquals("Hello Customer1", content);
  }

  /**
   * Tests that output directory is created if it does not exist.
   */
  @Test
  public void testCreatesOutputDir(@TempDir Path tempDir) throws IOException {
    TemplateProcessor processor = createProcessor(tempDir);
    String newDirPath = tempDir.resolve("newFolder").toString();

    OutputGenerator generator = new OutputGenerator(newDirPath, processor);
    generator.generate(csvTestData, "email");

    assertTrue(new File(newDirPath).exists());
  }

  /**
   * Tests with empty CSV data, no files should be generated.
   */
  @Test
  public void testEmptyCsvData(@TempDir Path tempDir) throws IOException {
    TemplateProcessor processor = createProcessor(tempDir);
    List<Map<String, String>> emptyData = new ArrayList<>();

    String outputPath = tempDir.resolve("output").toString();
    OutputGenerator generator = new OutputGenerator(outputPath, processor);
    generator.generate(emptyData, "email");

    assertEquals(0, new File(outputPath).listFiles().length);
  }

  /**
   * Tests that different prefixes create different file names.
   */
  @Test
  public void testDifferentPrefixes(@TempDir Path tempDir) throws IOException {
    TemplateProcessor processor = createProcessor(tempDir);
    OutputGenerator generator = new OutputGenerator(tempDir.toString(), processor);
    generator.generate(csvTestData, "email");
    generator.generate(csvTestData, "letter");

    assertTrue(new File(tempDir.toFile(), "email_1.txt").exists());
    assertTrue(new File(tempDir.toFile(), "letter_1.txt").exists());
  }
}