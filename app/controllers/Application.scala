package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._

object Application extends Controller {
    
  val userForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def user = Action{
    Ok(views.html.user(userForm))
  }

  def newUser = Action{ implicit request =>

    userForm.bindFromRequest.fold(
      errors => BadRequest(views.html.user(errors)),
      user => {
        User.save(
          User(
            firstName = "hey",
            lastName = "bob",
            username = user._1,
            password = user._2,
            email = "hello@sex.com"
          )
        )
        Ok(views.html.index(user._1))
      }
    )
  }
}