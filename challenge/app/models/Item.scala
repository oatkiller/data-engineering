package models
import anorm._
import anorm.SqlParser._
import play.api.db._

import play.api.Play.current

case class Item(id: Long, price: Long, description: String)

object Item {
	
	val item = {
		get[Long]("id") ~
		get[Long]("price") ~
		get[String]("description") map {
			case id~price~description => Item(id, price, description)
		}
	}
	
	def all(): List[Item] = DB.withConnection { implicit c =>
		SQL("select * from item").as(item *)
	}
	
	def create(description: String, price: Long) {
		println("creating?")
		DB.withConnection { implicit c =>
			SQL("insert into item (description,price) values ({description}, {price})").on(
				'description -> description,
				'price -> price
			).executeUpdate()
		}
	}
	
	def delete(id: Long) {
		DB.withConnection { implicit c =>
			SQL("delete from item where id = {id}").on(
				'id -> id
			).executeUpdate()
		}
	}
}
