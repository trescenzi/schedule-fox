package models

import play.api.Play.current
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import mongoContext._
import java.util.Date
import play.api.data._
import play.api.data.Forms._


case class Event( id: ObjectId = new ObjectId(),
                  title: String,
                  date: Date = new Date(),
                  location: String,
                  user: String,
                  tags: List[String])

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

  val dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy")

  def parseTags(tags: String): List[String] = {
    val split = tags.split(',')
    split map(s => s.trim) toList
  }

  def tagsToSting(tags: List[String]): String = {
    tags filter {_.nonEmpty} mkString ", "
  }

  val form = Form(
    mapping(
      "title" -> text,
      "date" -> text,
      "location" -> text,
      "user" -> text,
      "tags" -> text
    )((title, date, location, user, tags)
        => Event(new ObjectId , title, dateFormat.parse(date), location, user, parseTags(tags) ))
      ((event: Event) 
        => Some(event.title, Event.dateFormat.format(event.date), event.location, event.user, tagsToSting(event.tags)))
  )

}