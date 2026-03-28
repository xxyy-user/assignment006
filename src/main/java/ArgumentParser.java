/**
 * Parses and validates command line arguments.
 *
 * Supported options:
 * --email, --email-template, --letter, --letter-template,
 * --output-dir, --csv-file
 */
public class ArgumentParser {

  private boolean generateEmail;
  private boolean generateLetter;
  private String emailTemplatePath;
  private String letterTemplatePath;
  private String outputDirPath;
  private String csvFilePath;

  /**
   * Creates an ArgumentParser and parses the given args.
   * If args are invalid, prints an error message and exits.
   *
   * @param args command line arguments
   */
  public ArgumentParser(String[] args) {
    this.generateEmail = false;
    this.generateLetter = false;
    this.emailTemplatePath = null;
    this.letterTemplatePath = null;
    this.outputDirPath = null;
    this.csvFilePath = null;

    // fills the fields
    parseArgs(args);

    // checks validation
    validate();
  }

  /**
   * Parses the command line arguments and stores the values.
   * Example valid command:
   * --email --email-template email-template.txt
   * --letter --letter-template letter-template.txt
   * --output-dir foldername --csv-file customer.csv
   *
   * @param args command line arguments
   */
  private void parseArgs(String[] args) {
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "--email":
          this.generateEmail = true;
          break;
        case "--email-template":
          // checks if there is any arg after and uses the next one as emailTemplatePath
          if (i + 1 < args.length) {
            this.emailTemplatePath = args[++i];
          } else {
            printErrorAndExit("Error: --email-template requires a file path.");
          }
          break;
        case "--letter":
          this.generateLetter = true;
          break;
        case "--letter-template":
          // checks if there is any arg after and uses the next one as letterTemplatePath
          if (i + 1 < args.length) {
            this.letterTemplatePath = args[++i];
          } else {
            printErrorAndExit("Error: --letter-template requires a file path.");
          }
          break;
        case "--output-dir":
          // checks if there is any arg after, and uses the next one as outputDirPath
          if (i + 1 < args.length) {
            this.outputDirPath = args[++i];
          } else {
            printErrorAndExit("Error: --output-dir requires a folder path.");
          }
          break;
        case "--csv-file":
          // checks if there is any arg after, and uses the next one as csvFilePath
          if (i + 1 < args.length) {
            this.csvFilePath = args[++i];
          } else {
            printErrorAndExit("Error: --csv-file requires a file path.");
          }
          break;
        default:
          printErrorAndExit("Error: unknown option '" + args[i] + "'.");
          break;
      }
    }
  }

  /**
   * Checks if the parsed args are valid.
   * For example, --email requires --email-template.
   * --output-dir and --csv-file are always required.
   */
  private void validate() {
    if (this.outputDirPath == null) {
      printErrorAndExit("Error: missing --output-dir.");
    }
    if (this.csvFilePath == null) {
      printErrorAndExit("Error: missing --csv-file.");
    }
    if (!this.generateEmail && !this.generateLetter) {
      printErrorAndExit("Error: missing --email or --letter.");
    }
    if (this.generateEmail && this.emailTemplatePath == null) {
      printErrorAndExit("Error: --email provided but missing --email-template.");
    }
    if (this.generateLetter && this.letterTemplatePath == null) {
      printErrorAndExit("Error: --letter provided but missing --letter-template.");
    }
  }

  /**
   * Prints the error message, usage info, and examples, then exits.
   *
   * @param errorMsg the error message to show
   */
  private void printErrorAndExit(String errorMsg) {
    System.out.println(errorMsg);
    System.out.println();
    printUsage();
    System.exit(1);
  }

  /**
   * Prints how to use this program, with examples.
   */
  private void printUsage() {
    System.out.println("Usage:");
    System.out.println("  --email                         Generate email messages. If this option is");
    System.out.println("                                  provided, then --email-template must also be");
    System.out.println("                                  provided.");
    System.out.println("  --email-template <path/to/file> A filename for the email template.");
    System.out.println("  --letter                        Generate letters. If this option is provided,");
    System.out.println("                                  then --letter-template must also be provided.");
    System.out.println("  --letter-template <path/to/file> A filename for the letter template.");
    System.out.println("  --output-dir <path/to/folder>   The folder to store all generated files.");
    System.out.println("                                  This option is required.");
    System.out.println("  --csv-file <path/to/file>       The CSV file to process. This option is");
    System.out.println("                                  required.");
    System.out.println();
    System.out.println("Examples:");
    System.out.println("  --email --email-template email-template.txt --output-dir emails --csv-file customer.csv");
    System.out.println("  --letter --letter-template letter-template.txt --output-dir letters --csv-file customer.csv");
  }

  /**
   * Returns true if the user wants to generate emails.
   *
   * @return true if --email was provided
   */
  public boolean isGenerateEmail() {
    return generateEmail;
  }

  /**
   * Returns true if the user wants to generate letters.
   *
   * @return true if --letter was provided
   */
  public boolean isGenerateLetter() {
    return generateLetter;
  }

  /**
   * Returns the path to the email template file.
   *
   * @return email template path, or null if not provided
   */
  public String getEmailTemplatePath() {
    return emailTemplatePath;
  }

  /**
   * Returns the path to the letter template file.
   *
   * @return letter template path, or null if not provided
   */
  public String getLetterTemplatePath() {
    return letterTemplatePath;
  }

  /**
   * Returns the path to the output folder.
   *
   * @return output directory path
   */
  public String getOutputDirPath() {
    return outputDirPath;
  }

  /**
   * Returns the path to the CSV file.
   *
   * @return CSV file path
   */
  public String getCsvFilePath() {
    return csvFilePath;
  }
}