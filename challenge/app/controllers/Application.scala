package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Purchaser
import play.api.libs.iteratee.{Iteratee, Input}
import play.api.libs.iteratee.Parsing
import play.api.libs.iteratee.Enumeratee
import play.api.libs.Files.TemporaryFile
import java.io.FileOutputStream

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
	
	def upload = Action(parse.multipartFormData(handleFilePartAsTemporaryFile) ) { request =>
		request.body.file("picture").map { picture =>
			import java.io.File
			val filename = picture.filename 
			val contentType = picture.contentType
			picture.ref.moveTo(new File("/tmp/picture2"))
			Ok("File uploaded")
		}.getOrElse {
			Redirect(routes.Application.index).flashing(
				"error" -> "Missing file"
			)
		}
	}
	

	def handleFilePartAsTemporaryFile: parse.Multipart.PartHandler[MultipartFormData.FilePart[TemporaryFile]] = {
		parse.Multipart.handleFilePart {
			case parse.Multipart.FileInfo(partName, filename, contentType) =>
				val tempFile = TemporaryFile("multipartBody", "asTemporaryFile")
				Iteratee.fold[Array[Byte], FileOutputStream](new java.io.FileOutputStream(tempFile.file)) { (os, data) =>
					os.write(data)
						os
				}.mapDone { os =>
					os.close()
						tempFile
				}
		}
	}
	
}
