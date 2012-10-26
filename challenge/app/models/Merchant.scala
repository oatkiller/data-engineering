package models
import anorm._
import anorm.SqlParser._
import play.api.db._

import play.api.Play.current

case class Merchant(id: Long, address: String, name: String)

object Merchant {
	
	val merchant = {
		get[Long]("id") ~
		get[String]("address") ~
		get[String]("name") map {
			case id~address~name => Merchant(id, address, name)
		}
	}
	
	def all(): List[Merchant] = DB.withConnection { implicit c =>
		SQL("select * from merchant").as(merchant *)
	}
	
	def create(address: String, name: String) {
		DB.withConnection { implicit c =>
			SQL("insert into merchant (address,name) values ({address}, {name})").on(
				'address -> address,
				'name -> name
			).executeUpdate()
		}
	}
	
	def delete(id: Long) {
		DB.withConnection { implicit c =>
			SQL("delete from merchant where id = {id}").on(
				'id -> id
			).executeUpdate()
		}
	}
}
