package models

import play.api.Play.current
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import mongoContext._
import play.api.data._
import play.api.data.Forms._
import org.joda.time.DateTime
import org.joda.time.Interval



case class User(id: ObjectId = new ObjectId(), 
                fname: String,
                lname: String,
                username: String,
                password: String,
                email: String)

object User extends ModelCompanion[User, ObjectId]{
  //because I'm so savy I had to look up dao it stands for Data Access Object
  val dao = new SalatDAO[User, ObjectId](collection = mongoCollection("users")) {}

  def getByUsername(username: String): Option[User] = {
    dao.findOne(MongoDBObject("username" -> username))
  }

  def getByfname(fname: String): Option[User] = {
    dao.findOne(MongoDBObject("fname" -> fname))
  }

  def getByLname(lname: String): Option[User] = {
    dao.findOne(MongoDBObject("lname" -> lname))
  }

  def getUserEvents(username: String) = {
    // Joda.dateTimes.sorted
    val eventDao = new SalatDAO[Event, ObjectId](collection = mongoCollection("events")) {}
    eventDao.find(MongoDBObject("user" -> username))
      .toList.sortWith(_.startDate < _.startDate)
  }

  def login(username: String, password: String): Option[User] ={
    dao.findOne(MongoDBObject("username" -> username, "password" -> password))
  }

  def freeTime(username: String, start: DateTime, end: DateTime, 
              latestTime: DateTime, earliestTime: DateTime, duration: Interval)={
    //all events of the user
    val events = getUserEvents(username)



  }

  val form = Form(
    mapping(
      "fname" -> text,
      "lname" -> text,
      "username" -> text,
      "password" -> text,
      "email" -> email
    )((fname, lname, username, password, email)
        => User(new ObjectId, fname, lname, username, password, email))
      ((user: User) 
        => Some(user.fname, user.lname, user.username, user.password, user.email))
  )

  val loginForm = Form(
    tuple(
      "username" -> text,
      "password" -> text
    )
  )

  // val free
}

