package models
import anorm._
import anorm.SqlParser._
import play.api.db._

import play.api.Play.current

case class Purchaser(id: Long, name: String)

object Purchaser {
	
	val purchaser = {
		get[Long]("id") ~
		get[String]("name") map {
			case id~name => Purchaser(id, name)
		}
	}
	
	def all(): List[Purchaser] = DB.withConnection { implicit c =>
		SQL("select * from purchaser").as(purchaser *)
	}
	
	def create(name: String) {
		DB.withConnection { implicit c =>
			SQL("insert into purchaser (name) values ({name})").on(
				'name -> name
			).executeUpdate()
		}
	}
}
