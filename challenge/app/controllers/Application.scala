package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import models.Purchaser

object Application extends Controller {
  
	def index = Action {
		Redirect(routes.Application.purchase_orders)
	}
	
  def purchase_orders = Action {
	
		val purchaserForm = Form(
			"name" -> nonEmptyText
		)
		
		Ok(views.html.index(Purchaser.all(), purchaserForm))
	}
	
	def deletePurchaser(id: Long) = TODO
	def newPurchaser = TODO
	
	def upload = Action(parse.multipartFormData) { request =>
		request.body.file("picture").map { picture =>
			import java.io.File
			val filename = picture.filename 
			val contentType = picture.contentType
			picture.ref.moveTo(new File("/tmp/picture"))
			Ok("File uploaded")
		}.getOrElse {
			Redirect(routes.Application.index).flashing(
				"error" -> "Missing file"
			)
		}
	}
	
}
