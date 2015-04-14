package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

  public static Result index() {
    System.out.println("!!!!");
    return ok();
  }

}
