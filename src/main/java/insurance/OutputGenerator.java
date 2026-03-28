package insurance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Generates output files by combining CSV data with a template.
 * For each row in the CSV, it creates one output file with all
 * placeholders replaced by the row's values.
 */
public class OutputGenerator {

  private final String outputDirPath;
  private final TemplateProcessor templateProcessor;

  /**
   * Creates an OutputGenerator.
   *
   * @param outputDirPath path to the folder where files will be saved
   * @param templateProcessor the template processor object to use
   */
  public OutputGenerator(String outputDirPath, TemplateProcessor templateProcessor) {
    this.outputDirPath = outputDirPath;
    this.templateProcessor = templateProcessor;
  }

  /**
   * Generates one output file for each row in the CSV data.
   * Files are named using the prefix and a number, e.g. "email_1.txt", "email_2.txt".
   *
   * @param csvDataList list of maps from CSVParser, each map is one customer
   * @param filePrefix prefix for the output file name, e.g. "email" or "letter"
   * @throws IOException if a file cannot be written
   */
  public void generate(List<Map<String, String>> csvDataList, String filePrefix) throws IOException {
    // creates the output folder if it does not exist
    File dir = new File(outputDirPath);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    // for each customer, replace placeholders and write to a file
    for (int i = 0; i < csvDataList.size(); i++) {
      Map<String, String> customerData = csvDataList.get(i);
      String content = templateProcessor.process(customerData);

      // builds file path, e.g. "output/email_1.txt"
      String fileName = filePrefix + "_" + (i + 1) + ".txt";
      File outputFile = new File(dir, fileName);

      // writes the content to the file
      try (FileWriter writer = new FileWriter(outputFile)) {
        writer.write(content);
      }
    }
  }
}