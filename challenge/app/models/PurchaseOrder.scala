package models
import anorm._
import anorm.SqlParser._
import play.api.db._

import play.api.Play.current

case class PurchaseOrder(id: Long, purchaser_id: Long, merchant_id: Long, purchase_count: Long)

object PurchaseOrder {
	
	val purchase_order = {
		get[Long]("id") ~
		get[Long]("purchaser_id") ~
		get[Long]("merchant_id") ~
		get[String]("purchase_count") map {
			case id~purchaser_id~merchant_id~purchase_count => PurchaseOrder(id, purchaser_id, merchant_id, purchase_count)
		}
	}
	
	def all(): List[PurchaseOrder] = DB.withConnection { implicit c =>
		SQL("select * from purchase_order").as(purchase_order *)
	}
	
	def create(id: Long, purchaser_id: Long, merchant_id: Long, purchase_count: Long) {
		
		DB.withConnection { implicit c =>
			SQL("insert into purchase_order (id, purchaser_id, merchant_id, purchase_count) values ({id}, {purchaser_id}, {merchant_id}, {purchase_count})").on(
				'id -> id,
				'purchaser_id -> purchaser_id,
				'merchant_id -> merchant_id,
				'purchase_count -> purchase_count
			).executeUpdate()
		}
	}
	
	def delete(id: Long) {
		DB.withConnection { implicit c =>
			SQL("delete from purchase_order where id = {id}").on(
				'id -> id
			).executeUpdate()
		}
	}
}
