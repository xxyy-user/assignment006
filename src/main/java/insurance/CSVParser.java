package insurance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVParser {
  private final String filePath;

  public CSVParser(String filePath) {this.filePath = filePath;}

  /**
   * Parses the CSV file and uses map to store info of each line
   *
   * @return list of maps representing each line
   * @throws IOException if the file cannot be read
   */
  public List<Map<String, String>> parseCSV() throws IOException {
    List<Map<String, String>> list = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

      // 1. read and store header tokens in list
      String headerLine = reader.readLine();
      if (headerLine == null) return list;
      List<String> headers = splitCSVLine(headerLine);

      // 2. read information of each line
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.trim().isEmpty()) continue;
        List<String> values = splitCSVLine(line);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
          String value = i < values.size() ? values.get(i) : "";
          map.put(headers.get(i), value);
        }
        list.add(map);
      }
    }
    return list;
  }

  /**
   * Splits a single CSV line into a list of field values.
   *
   * @param line a single line from the CSV file
   * @return list of field values
   */

  List<String> splitCSVLine(String line) {
    List<String> fields = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    boolean in = false;

    // 1. read each char in line
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      // 1.1 if it is start or end sign
      if (c == '"') {
        // end
        if (in && i + 1 < line.length() && line.charAt(i + 1) == '"') {
          sb.append('"');
          // skip '"', prepare to next char
          i++;
        // change state
        } else {
          in = !in;
        }

      // 1.2 if it is split sign ','
      } else if (c == ',' && !in) {
        fields.add(sb.toString());
        // after append value, reset stringbuilder
        sb.setLength(0);

      // 1.3 if it is value
      } else {
        sb.append(c);
      }
    }
    // append last value
    fields.add(sb.toString());
    return fields;
  }
}
