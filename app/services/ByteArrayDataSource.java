package services;

/**
 * <pre>
 * ---------------------------------------------------------------
 * System Class :
 * Program Name : ByteArrayDataSource
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.activation.DataSource;

public class ByteArrayDataSource implements DataSource {

  private byte[] data; // data

  private String type; // content-type

  /**
     */
  public ByteArrayDataSource(String data, String type, String charSet) {
    try {
      this.data = data.getBytes(charSet); // 한글로 Encoding

    } catch (UnsupportedEncodingException uex) {
    }
    this.type = type;
  }

  /**
     */
  public InputStream getInputStream() throws IOException {
    if (data == null)
      throw new IOException("no data");
    return new ByteArrayInputStream(data);
  }

  /**
     */
  public OutputStream getOutputStream() throws IOException {
    throw new IOException("cannot do this");
  }

  /**
     */
  public String getContentType() {
    return type;
  }

  /**
     */
  public String getName() {
    return "dummy";
  }

}
