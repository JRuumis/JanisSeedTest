package controllers

import javax.inject._

import models.{InputBirthDate, AgeCalculator}
import play.api._
import play.api.data.Form
import play.api.mvc._
import play.api.data.Form
import play.api.data._
import play.api.data.Forms._
import org.joda.time._
import play.i18n.MessagesApi


@Singleton
class BirthDateInputController @Inject()(cc: ControllerComponents /*, messages: MessagesApi*/) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.birthDateInput(AgeCalculator.birthDateForm))
  }
}

