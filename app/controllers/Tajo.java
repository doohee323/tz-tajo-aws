package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import play.mvc.Controller;
import play.mvc.Result;
import utils.CmdUtil;
import utils.ConfigUtil;
import utils.DateUtil;
import utils.Ec2Util;
import utils.SSHUtil;
import utils.StringUtil;

public class Tajo extends Controller {

  private org.slf4j.Logger Logger = org.slf4j.LoggerFactory.getLogger(Tajo.class);

  private String accesskey = "";
  private String secretkey = "";
  private String key_file = "";
  private String username = "";

  private int nInstanceNum = 0;
  private String common_spec = "";
  private String spot_spec = "";

  private String ami_id = "";
  private String spot_price = "";

  private String master_ip = "";
  private String master_external_ip = "";

  private String key = "";
  private String security_group = "";
  private String bucket = "";
  private String region = "";
  private String s3_conf = "";

  private List instanceIds = new ArrayList();

  // 0) create master
  // ec2-run-instances -O AKIAJLRKPW4QI -W
  // fuuockdwyFM0cAOdGjieSNya/b --region us-east-1 ami-4a930222 -b
  // "/dev/sdb=ephemeral0" -g taas -n 1-1 -k awskey1 -t m3.medium -d
  // taas-bucket,AKIAJLRKPW4QI,fuuockdwyFM0cAOdGjieSNya/b,s3n://taas-bucket/conf/catalog-site.xml
  //
  public void main(String[] arg) {
    get();
  }

  public static Result get() {
    try {
      String instanceType = "spot"; // common / spot

      Tajo tajo = new Tajo();
      tajo.init();
      tajo.getInstance(instanceType);
      // tajo.runQuery("COHORTS"); // test / COHORTS / IMPRESSION
      tajo.runQuery("IMPRESSION");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ok();
  }

  public void init() {
    List<Map<String, Object>> mConf = ConfigUtil.getConfig("tajo.xml", "config");

    accesskey = ConfigUtil.getProperty(mConf, "accesskey");
    secretkey = ConfigUtil.getProperty(mConf, "secretkey");
    key_file = ConfigUtil.getProperty(mConf, "key_file");
    username = ConfigUtil.getProperty(mConf, "username");

    String instanceNum = ConfigUtil.getProperty(mConf, "instanceNum");
    nInstanceNum = Integer.parseInt(instanceNum);
    common_spec = ConfigUtil.getProperty(mConf, "common_spec");
    spot_spec = ConfigUtil.getProperty(mConf, "spot_spec");

    ami_id = ConfigUtil.getProperty(mConf, "ami_id");
    spot_price = ConfigUtil.getProperty(mConf, "spot_price");

    master_ip = ConfigUtil.getProperty(mConf, "master_ip");
    master_external_ip = ConfigUtil.getProperty(mConf, "master_external_ip");

    key = ConfigUtil.getProperty(mConf, "key");
    security_group = ConfigUtil.getProperty(mConf, "security_group");
    bucket = ConfigUtil.getProperty(mConf, "bucket");
    region = ConfigUtil.getProperty(mConf, "region");
    s3_conf = ConfigUtil.getProperty(mConf, "s3_conf");
  }

  // 1) create common instance
  // ec2-run-instances -O AKIAJLRKPW4QIRT -W
  // fuuockdwyFM0cAOdGjieSNya/bckMg0wHKuENw6
  // --region us-east-1 ami-6acb7202
  // -b "/dev/sdb=ephemeral0" -g taas -n 2-2
  // -k awskey1
  // -t m3.medium -d
  // taas-bucket,AKIAJLRKPW4QIRT,fuuockdwyFM0cAOdGjieSNya/bckMg0wHKuENw6,s3n://taas-bucket/conf/catalog-site.xml
  //
  // 1-1) create spot instance
  // > ec2-request-spot-instances -O AKIAJLRKPW4QI -W
  // fuuockdwyFM0cAOdGjieSNya/b --price 0.2 --region us-east-1
  // ami-263eb84e -b "/dev/sdb=ephemeral0" -b "/dev/sdc=ephemeral1" -g taas -n 1
  // -k awskey1 -t c3.2xlarge -d
  // ADD,10.95.166.81,taas-bucket,AKIAJLRKPW4QI,fuuockdwyFM0cAOdGjieSNya/b
  //
  // SPOTINSTANCEREQUEST {sir-02ged956} 0.200000 one-time Linux/UNIX open
  // 2014-11-05T09:22:58-0800 ami-263eb84e c3.2xlarge awskey1 sg-c85d1da2
  // monitoring-disabled
  // SPOTINSTANCESTATUS pending-evaluation 2014-11-05T09:22:58-0800 Your Spot
  // request has been submitted for review, and is pending evaluation.
  //
  // 2) check request or http call
  // > ec2-describe-spot-instance-requests sir-02ged956
  //
  // SPOTINSTANCEREQUEST sir-02ged956 0.200000 one-time Linux/UNIX active
  // 2014-11-05T09:22:59-0800 {i-ff03cc1e} ami-263eb84e c3.2xlarge awskey1
  // sg-c85d1da2 monitoring-disabled us-east-1a
  // SPOTINSTANCESTATUS fulfilled 2014-11-05T09:24:49-0800 Your Spot request is
  // fulfilled.
  //
  // 3) check instance
  // > ec2-describe-instances i-ff03cc1e
  //
  // RESERVATION r-03c8972f 721150439457 taas
  // INSTANCE i-ff03cc1e ami-263eb84e ec2-54-161-191-224.compute-1.amazonaws.com
  // ip-10-142-168-165.ec2.internal running awskey1 0c3.2xlarge
  // 2014-11-05T17:24:49+0000 us-east-1a monitoring-disabled 54.161.191.224
  // 10.142.168.165 ebs spot sir-02ged956 hvm xen
  // a1279f69-7d8d-4ef1-9381-fc2587090388 sg-c85d1da2 default false
  // BLOCKDEVICE /dev/xvda vol-7cc50964 2014-11-05T17:24:52.000Z true
  // TAG instance i-ff03cc1e taas_node_type worker
  // TAG instance i-ff03cc1e tajo_status running
  public int getInstance(String instanceType) {
    try {
      // 0. instance exist check
      boolean nReady = false;
      int nCode = checkInstance(instanceType);
      if (nCode < nInstanceNum) {
        // 1. create instance
        int cnt = nInstanceNum - nCode;
        String cmd = "";
        if (instanceType.equals("common")) {
          cmd = "ec2-run-instances -O " + accesskey + " -W " + secretkey;
          cmd +=
              " --region " + region + " " + ami_id + " -b \"/dev/sdb=ephemeral0\" -g "
                  + security_group + " -n " + cnt + " -k " + key;
          cmd +=
              " -t " + common_spec + " -d ADD," + master_ip + "," + bucket + "," + accesskey + ","
                  + secretkey;
        } else if (instanceType.equals("spot")) {
          cmd =
              "ec2-request-spot-instances -O " + accesskey + " -W " + secretkey + " --price "
                  + spot_price;
          cmd +=
              " --region " + region + " " + ami_id
                  + " -b \"/dev/sdb=ephemeral0\" -b \"/dev/sdc=ephemeral1\" -g " + security_group
                  + " -n " + cnt + " -k " + key;
          cmd +=
              " -t " + spot_spec + " -d ADD," + master_ip + "," + bucket + "," + accesskey + ","
                  + secretkey + "," + s3_conf;
        }

        CmdUtil.execUnixCommand(cmd);
        Thread.sleep(180000); // 3 minutes
        // 2. checking
        nCode = 0;
        int nMaxCheck = 3, nCnt = 0;
        while (nCode < nInstanceNum && nCnt < nMaxCheck) {
          nCode = checkInstance(instanceType);
          nCnt++;
          if (nCode == -1) {
            return nCode; // error!
          }
          if (nCode == nInstanceNum) {
            nReady = true;
          } else {
            Thread.sleep(60000); // 1 minutes
          }
        }
      } else if (nCode == nInstanceNum) {
        nReady = true;
      }
      if (nReady) {
        return nInstanceNum;
      } else {
        return nCode;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }

  // 3. execute query
  public void runQuery(String arg) throws Exception {
    Map<String, String> hostInfo = new HashMap<String, String>();
    List<String> commands = new ArrayList<String>();
    String preHandle = "";
    String postHandle = "";

    hostInfo.put("username", username);
    hostInfo.put("host", master_external_ip);
    hostInfo.put("key_file", key_file);

    Map<String, Object> var = new HashMap<String, Object>();
    var.put("year_month", DateUtil.getCurrentDateString("yyyyMM"));
    var.put("year", DateUtil.getCurrentDateString("yyyy"));
    var.put("month", DateUtil.getCurrentDateString("MM"));
    var.put("date", DateUtil.getCurrentDateString("dd"));

    List<Map<String, Object>> mCommand = ConfigUtil.getConfig("tajo.xml", "command");
    System.out.println("mCommand.size():" + mCommand.size());
    for (int j = 0; j < mCommand.size(); j++) {
      String name = StringUtil.getText(mCommand.get(j).get("name"));
      System.out.println("name1:" + name + "/arg:" + arg);
      if (arg.equals(name)) {
        System.out.println("name2:" + name);
        preHandle = StringUtil.getText(mCommand.get(j).get("preHandle"));
        if (!preHandle.equals("")) {
          runQuery(preHandle);
        }
        postHandle = StringUtil.getText(mCommand.get(j).get("postHandle"));
        String commandstr = StringUtil.getText(mCommand.get(j).get("commands"));
        commandstr = commandstr.replaceAll(";;", "^p;");
        String command[] = commandstr.split(";");
        for (int i = 0; i < command.length; i++) {
          String cmd = command[i].replaceAll("\\^p", ";");
          cmd = ConfigUtil.decode(cmd);
          cmd = replaceVariables(cmd, var);
          commands.add(cmd);
        }
      }
    }

    SSHUtil util = new SSHUtil();
    String rslt = util.shell(hostInfo, commands);

    if (postHandle.equals("terminate")) {
      // 5. delete instances
      for (int i = 0; i < instanceIds.size(); i++) {
        String str = instanceIds.get(i).toString();
        if (str.indexOf("\tfulfilled\t") > -1) {
          String instanceId = str.split("\t")[11];
          if (instanceId.trim().startsWith("i-")) {
            String cmd = "ec2-terminate-instances " + instanceId;
            System.out.println(cmd);
            CmdUtil.execUnixCommand(cmd);
          }
        }
      }
    }
  }

  public String replaceVariables(String orgStr, Map<String, Object> var) {
    if (var != null) {
      Object[] key = var.keySet().toArray();
      for (int i = 0; i < key.length; i++) {
        if (!((var.get((String) key[i])) instanceof String))
          continue;
        String value = (String) var.get((String) key[i]);
        value = value == null ? "" : value;
        orgStr =
            orgStr.replaceAll("\\$\\{" + ((String) key[i]) + "\\}",
                StringUtil.quoteReplacement(value));
      }
    }
    return orgStr;
  }

  public int checkInstance(String instanceType) {
    // 1. checking exist of request
    try {
      String cmd = "", splitFlag = "";
      if (instanceType.equals("common")) {
        cmd = "ec2-describe-instances --filter \"tag:Name=TAJO_WORKER*\"";
        splitFlag = "RESERVATION";
      } else if (instanceType.equals("spot")) {
        cmd = "ec2-describe-spot-instance-requests";
        splitFlag = "SPOTINSTANCEREQUEST";
      }
      String rslt = CmdUtil.execUnixCommand(cmd);
      String arry[] = rslt.split(splitFlag);
      for (int i = 0; i < arry.length; i++) {
        if (!arry[i].equals("") && arry[i].indexOf("terminated") == -1) {
          if (instanceType.equals("common")) {
            arry[i] =
                arry[i].substring(arry[i].indexOf("INSTANCE") + "INSTANCE ".length(),
                    arry[i].length());
          } else if (instanceType.equals("spot")) {
            arry[i] = arry[i].substring(0, arry[i].indexOf(" "));
          }
          instanceIds.add(arry[i]);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (instanceIds.size() == 0) {
      return 0;
    } else {
      int openCnt = 0;
      for (int i = 0; i < instanceIds.size(); i++) {
        if (instanceIds.get(i).toString().indexOf("   open    ") > -1
            || instanceIds.get(i).toString().indexOf("\trunning\t") > -1) {
          openCnt++;
        }
      }
      if (openCnt == nInstanceNum) {
        return nInstanceNum;
      }
    }

    // 1.1 checking
    DefaultHttpClient httpClient = new DefaultHttpClient();
    StringBuilder result = new StringBuilder();
    try {
      String url = "http://" + master_external_ip + ":26080/cluster.jsp";
      HttpGet postRequest = new HttpGet(url);
      postRequest.addHeader("Content-Type", "application/json");
      HttpResponse response = httpClient.execute(postRequest);
      if (response.getStatusLine().getStatusCode() != 200) {
        System.out.println("fail sending url => " + url);
        throw new RuntimeException("Failed : HTTP error code : "
            + response.getStatusLine().getStatusCode());
      }
      BufferedReader br =
          new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
      String output;
      while ((output = br.readLine()) != null) {
        result.append(output);
      }
      String tmp = result.toString();
      if (tmp.indexOf("<h2>Worker</h2>") > -1) {
        tmp = tmp.substring(tmp.indexOf("<h2>Worker</h2>"), tmp.length());
        tmp = tmp.substring(tmp.indexOf(":") + 1, tmp.length());
        tmp = tmp.substring(0, tmp.indexOf(","));
      }
      if (tmp.length() > 0) {
        return Integer.parseInt(tmp);
      }
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return -1;
  }
}
