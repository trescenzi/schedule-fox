package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._

object Application extends Controller {

  def index = Action {implicit request =>
    session.get("username").map { user =>
      Ok(views.html.index("Hello" + user))
    }.getOrElse {
      Ok(views.html.index("not logged in"))
    }
  }
  
}