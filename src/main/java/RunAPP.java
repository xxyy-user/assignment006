import insurance.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Parses command line arguments, reads the CSV file, applies templates,
 * and writes generated email or letter files to the output directory.
 */
public class RunAPP {

  public static void main(String[] args) {
    // 1. Parse and validate command line arguments
    ArgumentParser parser = new ArgumentParser(args);

    // 2. Parse the CSV file
    CSVParser csvParser = new CSVParser(parser.getCsvFilePath());
    List<Map<String, String>> csvData;
    try {
      csvData = csvParser.parseCSV();
    } catch (IOException e) {
      System.out.println("Error: could not read CSV file: " + parser.getCsvFilePath());
      System.exit(1);
      return;
    }

    // 3. Generate emails if requested
    if (parser.isGenerateEmail()) {
      try {
        TemplateProcessor emailTemplate = new TemplateProcessor(parser.getEmailTemplatePath());
        OutputGenerator emailGenerator = new OutputGenerator(parser.getOutputDirPath(), emailTemplate);
        emailGenerator.generate(csvData, "email");
      } catch (IOException e) {
        System.out.println("Error: could not process email template: " + e.getMessage());
        System.exit(1);
      }
    }

    // 4. Generate letters if requested
    if (parser.isGenerateLetter()) {
      try {
        TemplateProcessor letterTemplate = new TemplateProcessor(parser.getLetterTemplatePath());
        OutputGenerator letterGenerator = new OutputGenerator(parser.getOutputDirPath(), letterTemplate);
        letterGenerator.generate(csvData, "letter");
      } catch (IOException e) {
        System.out.println("Error: could not process letter template: " + e.getMessage());
        System.exit(1);
      }
    }
  }
}
