package models

import java.text.NumberFormat
import java.util.{Calendar, Locale, TimeZone}

import play.api.data.Form
import play.api.data._
import play.api.data.Forms._
import org.joda.time._

import scala.collection.mutable

/**
  * Created by janis on 10/07/2017.
  */
case class BirthData(birthDate: Int, birthMonth: Int, birthYear: Int, birthHour: Int, birthMinute: Int)
case class BirthDataShort(birthDate: Int, birthMonth: Int, birthYear: Int)

case class CalculationResults(
                               yearsOld: String,
                               monthsOld: String,
                               daysOld: String,
                               hoursOld: String,
                               minutesOld: String,
                               secondsOld: String,
                               millisecondsOld: String
                             )

/*
@Singleton
class PasswordChangeForm @Inject() (messages: MessagesApi) {
  //now use it like this messages("somekey")
}
*/



object BirthDateObjects  {


  val birthDateForm = Form[BirthData](
    mapping (
      "birthDate" -> number,
      "birthMonth" -> number,
      "birthYear" -> number,
      "birthHour" -> number,
      "birthMinute" -> number
    )(BirthData.apply)(BirthData.unapply)
  )

  val birthDataFormShort = Form[BirthDataShort](
    mapping (
      "birthDate" -> number,
      "birthMonth" -> number,
      "birthYear" -> number
    )(BirthDataShort.apply)(BirthDataShort.unapply)
  )


  /*
  def ageInYears(birthData: BirthData) = {
    val calendar = Calendar.getInstance( TimeZone.getTimeZone("GMT") )
    val currentYear = calendar.get( Calendar.YEAR )

    val calculatedAgeInYears = currentYear - birthData.birthYear

    calculatedAgeInYears
  }
  */

  def calculateResults(birthInput: BirthData): CalculationResults = {

    // base dates
    val adjustedHour = if (birthInput.birthHour == "") 0 else birthInput.birthHour
    val adjustedMinute = if (birthInput.birthMinute == "" || birthInput.birthHour == "") 0 else birthInput.birthMinute

    val birthDate = new DateTime(birthInput.birthYear, birthInput.birthMonth, birthInput.birthDate, adjustedHour, birthInput.birthMinute)
    val nowDate = DateTime.now()

    // calculations
    val yearsOld = Years.yearsBetween(birthDate, nowDate).getYears
    val monthsOld = Months.monthsBetween(birthDate, nowDate).getMonths
    val duration = new Duration(birthDate, nowDate)

    val daysOld = duration.getStandardDays
    val hoursOld = duration.getStandardHours
    val minutesOld = duration.getStandardMinutes
    val secondsOld = duration.getStandardSeconds
    val millisecondsOld = duration.getMillis

    def formatNumber(num: Long) = NumberFormat.getNumberInstance(Locale.US).format(num)

    CalculationResults(
      yearsOld = formatNumber(yearsOld),
      monthsOld = formatNumber(monthsOld),
      daysOld = formatNumber(daysOld),
      hoursOld = formatNumber(hoursOld),
      minutesOld = formatNumber(minutesOld),
      secondsOld = formatNumber(secondsOld),
      millisecondsOld = formatNumber(millisecondsOld)
    )

  }




}