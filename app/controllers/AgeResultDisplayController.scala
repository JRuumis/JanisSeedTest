package controllers

import javax.inject._

import models._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class AgeResultDisplayController @Inject()(cc: ControllerComponents) extends AbstractController(cc)  with play.api.i18n.I18nSupport {

  def calculationResult() = Action { implicit request: Request[AnyContent] =>

    AgeCalculator.birthDateForm.bindFromRequest.fold(

      // error
      formWithErrors => { BadRequest(views.html.birthDateInput(formWithErrors)) },

      // validation passed
      birthDate => {
        val result = AgeCalculator.calculateResults(birthDate)
        Ok(views.html.ageResultDisplay(result))
      }
    )

  }
}
