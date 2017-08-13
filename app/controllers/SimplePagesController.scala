package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc._

@Singleton
class SimplePagesController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def about() = Action { implicit request: Request[AnyContent] => Ok(views.html.about()) }

  def calculations() = Action { implicit request: Request[AnyContent] => Ok(views.html.calculations())}


}
