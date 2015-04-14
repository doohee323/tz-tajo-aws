package utils;

import java.io.File;
import java.util.UUID;

import play.Logger;
import play.Play;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class Ec2Util {
  public final static String bucketname = Play.application().configuration()
      .getString("s3.bucketname");
  public final static String accessKey = Play.application().configuration()
      .getString("s3.accesskey");
  public final static String secretKey = Play.application().configuration()
      .getString("s3.secretkey");
  public final static String s3link = Play.application().configuration().getString("s3.link");

  public static boolean uploadS3(File file) {
    s3Upload(bucketname, file);
    return false;
  }

  public static String s3Upload(String bucketname, File attachment) {
    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);
    s3Client.createBucket(bucketname);
    String name = UUID.randomUUID().toString();

    PutObjectRequest por = new PutObjectRequest(bucketname, name, attachment);
    por.setCannedAcl(CannedAccessControlList.PublicRead);
    s3Client.putObject(por);
    return name;
  }

  public static String s3Download(String sourceFileNm, String targetFilePath) {
    return s3Download(bucketname, sourceFileNm, targetFilePath);
  }

  public static String s3Download(String bucketname, String sourceFileNm, String targetFilePath) {
    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);
    File file = new File(targetFilePath);
    s3Client.getObject(new GetObjectRequest(bucketname, sourceFileNm), file);
    return file.getAbsolutePath();
  }

  public static boolean s3Delete(String path, String key) {
    try {
      AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
      AmazonS3 s3Client = new AmazonS3Client(awsCredentials);
      s3Client.deleteObject(path, key);
      return true;
    } catch (Exception e) {
      System.out.println("could not delete from s3: exception:" + e.fillInStackTrace());
    }
    return false;
  }

}
