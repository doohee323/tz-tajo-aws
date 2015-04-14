package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * <pre>
 * </pre>
 * 
 * @version 1.0
 */
public class FileUtil {

  /**
   * <pre>
   * </pre>
   */
  public static final Pattern dosSeperator = Pattern.compile("\\\\");

  /**
   * <pre>
   * </pre>
   */
  public static StringBuffer getFromFile(String fileName, String strChar) throws IOException {
    if (strChar == null) {
      Scanner scanner = new Scanner(new File(fileName)).useDelimiter("\\Z");
      String contents = scanner.next();
      scanner.close();
      return new StringBuffer(contents);
    }

    if (strChar.equals(""))
      strChar = null;

    StringBuffer sb = new StringBuffer(1000);
    InputStreamReader is = null;
    BufferedReader in = null;
    String lineSep = System.getProperty("line.separator");

    try {
      File f = new File(fileName);
      if (f.exists()) {
        if (strChar != null)
          is = new InputStreamReader(new FileInputStream(f), strChar);
        else
          is = new InputStreamReader(new FileInputStream(f));
        in = new BufferedReader(is);
        String str = "";

        int readed = 0;
        while ((str = in.readLine()) != null) {
          if (strChar != null)
            readed += (str.getBytes(strChar).length);
          else
            readed += (str.getBytes().length);
          sb.append(str + lineSep);
        }
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    } finally {
      if (is != null)
        is.close();
      if (in != null)
        in.close();
    }
    return sb;
  }
}
