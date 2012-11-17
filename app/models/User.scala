package models

import play.api.Play.current
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import mongoContext._

case class User(id: ObjectId = new ObjectId(), 
                firstName: String,
                lastName: String,
                username: String,
                email: String,
                password: String)

object User extends ModelCompanion[User, ObjectId]{
  //because I'm so savy I had to look up dao it stands for Data Access Object
  val dao = new SalatDAO[User, ObjectId](collection = mongoCollection("users")) {}

    def getByUsername(username: String): Option[User] = {
      dao.findOne(MongoDBObject("username" -> username))
    }

    def getUserEvents(username: String): List[Event] = {
      val eventDao = new SalatDAO[Event, ObjectId](collection = mongoCollection("events")) {}
      eventDao.find(MongoDBObject("user" -> MongoDBObject("username" -> username)))
        .sort(orderBy = MongoDBObject("_id" -> -1))
        .skip(1)
        .limit(1)
        .toList
    }

}