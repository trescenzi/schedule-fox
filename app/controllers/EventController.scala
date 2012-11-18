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

  def postEvent = Action{implicit request =>

    Event.form.bindFromRequest.fold(
      errors =>
                session.get("username").map { user =>
                  BadRequest(views.html.newevent(errors,user, "errors"))
                }.getOrElse{
                  Ok(views.html.index("not logged in"))
                },
      event => {
        Event.save(event)
        Ok(views.html.index(event.user+"hey sexy"))
      }
    )
  }

  def events = Action{implicit request =>
    session.get("username").map { user =>
      Ok(views.html.events(user))
    }.getOrElse{
      Ok(views.html.login(User.loginForm))
    }
  }

}