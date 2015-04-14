package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.json.JSONException;
import org.json.JSONObject;

import play.db.ebean.Model;

/**
 * @author dewey
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
@Entity
@Table(name = "USER")
public class User extends Model {

  private static final long serialVersionUID = 1L;

  public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

  @Id
  @Column(name = "user_email")
  public String user_email;
  @Column(name = "created_at")
  public String created_at;

  public String toJson() {
    JSONObject user = new JSONObject();
    try {
      user.put("user_email", this.user_email);
      user.put("created_at", this.created_at);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return user.toString();
  }
}
