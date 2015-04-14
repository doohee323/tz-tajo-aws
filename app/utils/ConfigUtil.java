package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;

public class ConfigUtil {

  @SuppressWarnings("unchecked")
  public static List<Map<String, Object>> getConfig(String config, String scope) {
    List<Map<String, Object>> mConf = new ArrayList<Map<String, Object>>();
    try {
      Map<String, Object> input = new HashMap<String, Object>();
      String xmlFile = "./conf/" + config;
      System.out.println("getAbsolutePath 1:" + new File(xmlFile).getAbsolutePath());
      System.out.println("xmlFile:" + xmlFile);
      if (!new File(xmlFile).exists()) {
        xmlFile = config;
        System.out.println("getAbsolutePath 2:" + new File(xmlFile).getAbsolutePath());
      }
      if (new File(xmlFile).exists()) {
        String str = FileUtil.getFromFile(xmlFile, null).toString();
        str = encode(str);
        input.put("xmlStr", str);
        input.put("schemaPath", "/batch/" + scope + "/SCHEMA/COLUNM");
        input.put("dataPath", "/batch/" + scope + "/DATASET/DATA");
        Map<String, Map<String, Object>> lHolder = (Map) XmlUtil.XmlToMulti(input, null);
        mConf = (List<Map<String, Object>>) lHolder.get("BODY");
      } else {
        System.out.println("xmlFile not exist!:" + xmlFile);
      }
    } catch (Exception e) {
      System.out.println("getConfig error!: " + e.getMessage());
    }
    return mConf;
  }

  public static String getProperty(List<Map<String, Object>> mConfig, String key) {
    for (int i = 0; i < mConfig.size(); i++) {
      if (mConfig.get(i).get("key").toString().equals(key)) {
        return mConfig.get(i).get("value").toString();
      }
    }
    return null;
  }

  public static String encode(String str) {
    str =
        str.replaceAll(" < ", " µ ").replaceAll(" <= ", " µ= ").replaceAll(" > ", " ± ")
            .replaceAll(" >= ", " ±= ").replaceAll("&", "¶");
    str = StringUtil.Trim(str);
    return str;
  }

  public static String decode(String str) {
    str =
        str.replaceAll(" µ ", " < ").replaceAll(" µ= ", " <= ").replaceAll(" ± ", " > ")
            .replaceAll(" ±= ", " >= ").replaceAll("¶", "&").replaceAll("``", "\"");
    str = StringUtil.Trim(str);
    return str;
  }

}