package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._

object UserController extends Controller {

  def user = Action{
    Ok(views.html.user())
  }

  def login = Action{
    Ok(views.html.login(User.loginForm, ""))
  }

  def verifyLogin = Action{implicit request =>

    User.loginForm.bindFromRequest.fold(
      errors => BadRequest(views.html.login(errors, "")),
      user => {
        User.login(user._1, user._2) match{
          case Some(user:User) =>
              Ok(views.html.index("Welcome" + user.username)).withSession("username" -> user.username)
          case None => BadRequest(views.html.login(User.loginForm, "Wrong username/password"))
        }
      }
    )
  }


  def newUser = Action{ implicit request =>

    User.form.bindFromRequest.fold(
      errors => BadRequest(views.html.user()),
      user => {
        User.save(user)
          Ok(views.html.index("lets see if it worked!" + user.fname ))
      }
    )
  }

  def findFreeTime = Action{implicit request =>
    Ok("hey")
  }

}