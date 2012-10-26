package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Purchaser
import play.api.libs.iteratee.{Iteratee, Input}
import play.api.libs.iteratee.Parsing
import play.api.libs.iteratee.Enumeratee
import play.api.libs.iteratee.{Iteratee, Input}
import play.api.libs.concurrent.Promise
import play.api.libs.iteratee.Input.{El, EOF, Empty}
import au.com.bytecode.opencsv.CSVReader
import java.io.StringReader
import scala.collection.JavaConversions._
import play.api.libs.json.Json._
import play.api.libs.ws.WS

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
	
	/*
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
	*/
	
  def upload = Action(BodyParser(rh => new CsvIteratee(isFirst = true))) {
    request =>
      Ok("File Processed")
  }

	
}
case class CsvIteratee(state: Symbol = 'Cont, input: Input[Array[Byte]] = Empty, lastChunk: String = "", isFirst: Boolean = false) extends Iteratee[Array[Byte], Either[Result, String]] {
  def fold[B](
               done: (Either[Result, String], Input[Array[Byte]]) => Promise[B],
               cont: (Input[Array[Byte]] => Iteratee[Array[Byte], Either[Result, String]]) => Promise[B],
               error: (String, Input[Array[Byte]]) => Promise[B]
               ): Promise[B] = state match {
    case 'Done =>
      done(Right(lastChunk), Input.Empty)

    case 'Cont => cont(in => in match {
      case in: El[Array[Byte]] => {
        // Retrieve the part that has not been processed in the previous chunk and copy it in front of the current chunk
        val content = lastChunk + new String(in.e)
        val csvBody =
          if (isFirst)
            // Skip http header if it is the first chunk
            content.drop(content.indexOf("\r\n\r\n") + 4)
          else content
        val csv = new CSVReader(new StringReader(csvBody), '\t')
        val lines = csv.readAll
        // Process all lines excepted the last one since it is cut by the chunk
        for (line <- lines.init)
          processLine(line)
        // Put forward the part that has not been processed
        val last = lines.last.toList.mkString(";")
        copy(input = in, lastChunk = last, isFirst = false)
      }
      case Empty => copy(input = in, isFirst = false)
      case EOF => copy(state = 'Done, input = in, isFirst = false)
      case _ => copy(state = 'Error, input = in, isFirst = false)
    })

    case _ =>
      error("Unexpected state", input)

  }

  def processLine(line: Array[String]) = println(line(1))
}
