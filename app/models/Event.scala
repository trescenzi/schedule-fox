package models

import play.api.Play.current
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import mongoContext._
import java.util.Date


case class Event( id: ObjectId = new ObjectId(),
                  date: Option[Date] = None,
                  location: String,
                  user: User,
                  tags: Option[List[String]] = None)

object Event extends ModelCompanion[Event, ObjectId] {
  val dao = new SalatDAO[Event, ObjectId](collection = mongoCollection("events")) {}

  def getByDate(date: Date): List[Event] = {
    dao.find(MongoDBObject("date" -> date))
      .sort(orderBy = MongoDBObject("_id" -> -1))
      .toList
  }

  def getByLocation(location: String): List[Event] = {
    dao.find(MongoDBObject("location" -> location))
      .sort(orderBy = MongoDBObject("_id" -> -1))
      .toList
  }

  def getByTag(tag: String): List[Event] = {
    dao.find(MongoDBObject("tags" -> (MongoDBObject -> tag)))
      .sort(orderBy = MongoDBObject("_id" -> -1))
      .toList
  }

}