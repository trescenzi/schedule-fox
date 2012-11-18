package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._

object EventController extends Controller {

  def newEvent = Action{implicit request =>
    session.get("username").map { user =>
      Ok(views.html.newevent(Event.form, user))
    }.getOrElse {
      Ok(views.html.index("not logged in"))
    }
  }

  def postEvent = Action{
    Ok("dummy")
  }

  def events = Action{
    Ok("dummy")
  }

}