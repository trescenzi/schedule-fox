package models

import play.api.Play.current
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import mongoContext._
import org.joda.time.DateTime
import org.joda.time.format._
import org.joda.time.Instant
import play.api.data._
import play.api.data.Forms._


case class Event( id: ObjectId = new ObjectId(),
                  title: String,
                  startDate: Long,
                  endDate: Long,
                  location: String,
                  user: String,
                  tags: List[String])

object Event extends ModelCompanion[Event, ObjectId] {
  val dao = new SalatDAO[Event, ObjectId](collection = mongoCollection("events")) {}

  def getByStartDate(date: DateTime): List[Event] = {
    dao.find(MongoDBObject("startDate" -> date.toInstant))
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

  val dateFormat: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")

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
      "startDate" -> text,
      "endDate" -> text,
      "location" -> text,
      "user" -> text,
      "tags" -> text
    )((title, startDate, endDate, location, user, tags)
        => Event(new ObjectId , title, startDate.toLong,
                 endDate.toLong, location, user, parseTags(tags)))
      ((event: Event) 
        => Some(event.title, event.startDate.toString, event.endDate.toString,
                event.location, event.user, tagsToSting(event.tags)))
  )
}
// event.title, event.startDate, event.endDate,
//                event.location, event.user, tagsToSting(event.tags)

