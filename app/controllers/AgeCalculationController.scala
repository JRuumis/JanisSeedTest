package controllers

import javax.inject._

import models.{BirthData, BirthDataShort, BirthDateObjects}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class AgeCalculationController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def calculationResult() = Action { implicit request: Request[AnyContent] =>
    //Ok(views.html.ageCalc())
    Results.Ok {

      val birthData: Option[BirthData] = BirthDateObjects.birthDateForm.bindFromRequest.value
      val birthDataShort: Option[BirthDataShort] = BirthDateObjects.birthDataFormShort.bindFromRequest.value

      val adjustedBirthDate = (birthData, birthDataShort) match {
        case (Some(bd), _) => bd
        case (_,Some(bds)) => BirthData(birthDate = bds.birthDate, birthMonth = bds.birthMonth, birthYear = bds.birthYear, birthHour = 0, birthMinute = 0)
        case _ => BirthData(0,0,0,0,0)
      }

      val result = BirthDateObjects.calculateResults(adjustedBirthDate)


      //"77777"

      //val = yearsOld = BirthDateObjects.ageInYears(birthData).toString

      val resultString = s"You are:\n" +
        s"${result.yearsOld} years old,\n" +
        s"${result.monthsOld} months old,\n" +
        s"${result.daysOld} days old,\n" +
        s"${result.hoursOld} hours old,\n" +
        s"${result.minutesOld} minutes old,\n" +
        s"${result.secondsOld} seconds old,\n" +
        s"${result.millisecondsOld} milliseconds old."

      resultString

    }
  }
}
