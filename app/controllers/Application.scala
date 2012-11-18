package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def user = Action{
    Ok(views.html.user())
  }

  def newUser = Action{ implicit request =>

    User.form.bindFromRequest.fold(
      errors => BadRequest(views.html.user()),
      user => {
        User.save(
          User(
            fname = user.fname,
            lname = user.lname,
            username = user.username,
            password = user.password,
            email = user.email
          )
        )
        User.getByUsername(user.username) match {
          case Some(user:User) => Ok(views.html.index("lets see if it worked!" + user.fname ))
          case None => Ok(views.html.index("it's borked"))
        }
        
      }
    )
  }
}