package models

import java.text.{DecimalFormat, NumberFormat}
import java.util.{Calendar, Locale, TimeZone}

import play.api.data.Form
import play.api.data._
import play.api.data.Forms._
import org.joda.time._

import scala.collection.mutable

/**
  * Created by janis on 10/07/2017.
  */
case class BirthData(birthDate: Int, birthMonth: Int, birthYear: Int, birthHour: Option[Int], birthMinute: Option[Int])

case class AgeCalculationResults(
                               yearsOld: String,
                               monthsOld: String,
                               daysOld: String,
                               hoursOld: String,
                               minutesOld: String,
                               secondsOld: String,
                               millisecondsOld: String,
                               planckTimesOld: String,

                               percentageOfEarthAge: String,
                               percentageOfUniverseAge: String,

                               voyagerKm: String,
                               voyagerLightYear: String,
                               voyagerLightMinute: String,
                               voyagerToNearestStar: String,
                               voyagerSolarSystem: String,

                               speed90cEarthYears: String,
                               speed99cEarthYears: String,
                               speedLHCEarthYears: String
                             )

object BirthDateObjects  {


  //     number(min = 1, max = 12)

  val birthDateForm = Form[BirthData](
    mapping (
      "birthDate" -> number(min = 1, max = 31),
      "birthMonth" -> number.verifying("Select month from dropdown", f => f >= 1 && f <= 12),
      "birthYear" -> number(min = 1700, max = 2500),
      "birthHour" -> optional( number(min = 0, max = 23) ),
      "birthMinute" -> optional( number(min = 0, max = 59) )
    )(BirthData.apply)(BirthData.unapply)
  )


  /*
  def ageInYears(birthData: BirthData) = {
    val calendar = Calendar.getInstance( TimeZone.getTimeZone("GMT") )
    val currentYear = calendar.get( Calendar.YEAR )

    val calculatedAgeInYears = currentYear - birthData.birthYear

    calculatedAgeInYears
  }
  */

  def calculateResults(birthInput: BirthData): AgeCalculationResults = {

    // base dates
    val adjustedHour = birthInput.birthHour.getOrElse(0)
    val adjustedMinute = birthInput.birthMinute.getOrElse(0)

    val birthDate = new DateTime(birthInput.birthYear, birthInput.birthMonth, birthInput.birthDate, adjustedHour, adjustedMinute)
    val nowDate = DateTime.now()

    // calculations

    // time units
    val yearsOld = Years.yearsBetween(birthDate, nowDate).getYears
    val monthsOld = Months.monthsBetween(birthDate, nowDate).getMonths
    val duration = new Duration(birthDate, nowDate)

    val daysOld = duration.getStandardDays
    val hoursOld = duration.getStandardHours
    val minutesOld = duration.getStandardMinutes
    val secondsOld = duration.getStandardSeconds
    val millisecondsOld = duration.getMillis

    def millisecondToPlanck(millisecond: Long): BigInt = {
      BigInt(millisecond) * BigInt("18550000000000000000000000000000000000000")
    }

    //val planckTimesOld: BigInt = secondToPlanck(secondsOld)

    // --- percentage of age ---
    def percentageOfEarthAge(year: Int): Double = {
      100.toDouble * (year.toDouble / 4543000000l)
    }

    def percentageOfUniverseAge(year: Int): Double = {
      100.toDouble * (year.toDouble / 13799000000l)
    }

    // --- interstellar journey ---
    def voyagerJourney(millisecond: Long): Long = {
      //val voyagerOneSpeedKmh = 62140
      val voyagerOneSpeedKmMin = 1035.6666
      val voyagerOneSpeedKmMillisec = 0.01726111111

      val kilometres = millisecond * voyagerOneSpeedKmMillisec

      kilometres.round
    }

    def kmToLightMinutes(kilometre: Long): Double = kilometre * 5.5594 / 100000000l
    def kmToLightYears(kilometre: Long): Double = kilometre * 1.0570008340247 / 10000000000000l

    def percentageToProximaCentauri(kilometre: Long): Double = {
      // Proxima Centauri 4.243
      def distanceToProximaKm = 40141900000000l
      100.toDouble * kilometre.toDouble / distanceToProximaKm
    }


    // --- time slowdown on Earth ---
    def slowdown90c(time: Long): Long = (time.toDouble * 2.294157338706).round
    def slowdown99c(time: Long): Long = (time.toDouble * 7.088812050083).round
    def slowdownLHC(time: Long): Long = (time.toDouble * 7460.523).round



    // -----------------

    def formatNumber(num: Long): String = NumberFormat.getNumberInstance(Locale.US).format(num)
    def formatBigInt(num: BigInt): String = NumberFormat.getNumberInstance(Locale.US).format(num)
    def formatDecimal(dec: Double, round: Int): String = {
      val formatter = new DecimalFormat("#" + (if(round>0) "." else "") + "#" * round)
      formatter.format(dec)
    }

    AgeCalculationResults(
      yearsOld = formatNumber(yearsOld),
      monthsOld = formatNumber(monthsOld),
      daysOld = formatNumber(daysOld),
      hoursOld = formatNumber(hoursOld),
      minutesOld = formatNumber(minutesOld),
      secondsOld = formatNumber(secondsOld),
      millisecondsOld = formatNumber(millisecondsOld),
      planckTimesOld =  formatBigInt( millisecondToPlanck(millisecondsOld) ),

      percentageOfEarthAge = formatDecimal(percentageOfEarthAge(yearsOld), 12),
      percentageOfUniverseAge = formatDecimal(percentageOfUniverseAge(yearsOld), 12),

      voyagerKm = formatNumber(voyagerJourney(millisecondsOld)),
      voyagerLightYear = formatDecimal(kmToLightYears(voyagerJourney(millisecondsOld)), 12),
      voyagerLightMinute = formatDecimal( kmToLightMinutes(voyagerJourney(millisecondsOld)), 2),
      voyagerSolarSystem = "TODO: solar system",
      voyagerToNearestStar = formatDecimal( percentageToProximaCentauri(voyagerJourney(millisecondsOld)), 12),

      speed90cEarthYears = formatNumber( slowdown90c(yearsOld) ),
      speed99cEarthYears = formatNumber( slowdown99c(yearsOld) ),
      speedLHCEarthYears = formatNumber( slowdownLHC(yearsOld) )
    )

  }

  /*
  def calculateResults2(birthYear: Int, birthMonthOfYear: Int, birthDateOfMonth: Int, birthHour: Option[Int], birthMinute: Option[Int]): AgeCalculationResults = {

    // base dates
    val adjustedHour: Int = birthHour.getOrElse(0)
    val adjustedMinute: Int = if (birthHour.isEmpty) 0 else birthMinute.getOrElse(0)

    val birthDate = new DateTime(birthYear, birthMonthOfYear, birthDateOfMonth, adjustedHour, adjustedMinute)
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

    AgeCalculationResults(
      yearsOld = formatNumber(yearsOld),
      monthsOld = formatNumber(monthsOld),
      daysOld = formatNumber(daysOld),
      hoursOld = formatNumber(hoursOld),
      minutesOld = formatNumber(minutesOld),
      secondsOld = formatNumber(secondsOld),
      millisecondsOld = formatNumber(millisecondsOld)
    )

  }
*/



}