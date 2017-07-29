package controllers

import javax.inject._

import models.{BirthData, BirthDateObjects}
import play.api._
import play.api.data.Form
import play.api.mvc._
import play.api.data.Form
import play.api.data._
import play.api.data.Forms._
import org.joda.time._
import play.i18n.MessagesApi

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BirthDateInputController @Inject()(cc: ControllerComponents /*, messages: MessagesApi*/) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.birthDateInput(BirthDateObjects.birthDateForm))
  }
}

