package controllers;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import utils.CmdUtil;

public class UserApi extends Controller {

  private static org.slf4j.Logger Logger = org.slf4j.LoggerFactory.getLogger(UserApi.class);

  public static Result makeUserJson() {
    System.out.println("makeUserJson called");
    FileOutputStream fos = null;
    BufferedWriter out = null;
    try {
      List<User> user = User.find.where().findList();
      if (user == null || user.size() == 0) {
        return null;
      }

      String fileNm = "/Users/mac/users/User.json";
      fos = new FileOutputStream(fileNm);
      out = new BufferedWriter(new OutputStreamWriter(fos, "UTF8"));
      PrintWriter appender = new PrintWriter(out);

      System.out.println("user.size():" + user.size());
      for (int i = 0; i < user.size(); i++) {
        appender.write(user.get(i).toJson());
        appender.write("\r\n");
      }
      appender.close();
      // s3cmd sync /Users/mac/users s3://tz-tajo/users
      String baseLogPath = "/Users/mac/user";
      String s3Bucket = "tz-user/";
      String cmd = "s3cmd sync " + baseLogPath + " s3://" + s3Bucket + "/";
      Logger.error("========================== " + cmd);
      CmdUtil.execUnixCommand(cmd);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        fos.close();
        out.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return ok();
  }
}
