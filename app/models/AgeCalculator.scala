package models

import java.text.{DecimalFormat, NumberFormat}
import java.util.Locale

import play.api.data.Form
import play.api.data._
import play.api.data.Forms._
import org.joda.time._
import org.joda.time.format._


/**
  * Created by janis on 10/07/2017.
  */

// input interface
case class InputBirthDate(
                      birthDate: Int,
                      birthMonth: Int,
                      birthYear: Int,
                      birthHour: Option[Int],
                      birthMinute: Option[Int]
                    )

// calculated output, formatted as string
case class AgeCalculationResult(
                                // birth date
                                birthDateTime: String,

                                // raw values
                                daysOldNum: Long,

                                // age in time units
                                yearsOld: String,
                                monthsOld: String,
                                weeksOld: String,
                                daysOld: String,
                                hoursOld: String,
                                minutesOld: String,
                                secondsOld: String,
                                millisecondsOld: String,
                                planckTimesOld: String,

                                // age as percentage
                                percentageOfEarthAge: String,
                                earthYourLifetimes: String,
                                percentageOfUniverseAge: String,
                                universeYourLifetimes: String,
                                homoSapiensYourLifetimes: String,

                                // age in terms of imaginary Voyager 1 flight
                                voyagerKm: String,
                                voyagerLightYear: String,
                                voyagerLightMinute: String,
                                voyagerPing: String,
                                voyagerToNearestStar: String,
                                voyagerSolarSystem: String,
                                //voyagerTimeSlowerRelativisticSeconds: String,

                                // near light speed - time on Earth
                                earthYears90c: String,
                                earthYears99c: String,
                                earthYearsLHC: String,
                                earthYearsLEP: String,
                                earthYearsOMG: String,

                                // near light speed distance covered
                                lyTravel90c: String,
                                lyTravel99c: String,
                                lyTravelLHC: String,
                                lyTravelLEP: String,
                                lyTravelOMG: String,

                                // your body
                                salivaProducedLitres: String,
                                salivaProducedUSGallons: String,
                                salivaProducedImperialTeaspoon: String,
                                salivaOlympicPoolPercent: String,
                                nailsGrownMetres: String,
                                nailsGrownFeet: String,
                                hairGrownMetres: String,
                                hairGrownFeet: String,
                                breaths: String,
                                heartbeats: String,
                                kilocalories: String,
                                olympicSwimmingPoolCaloriesNum: Double,
                                olympicSwimmingPoolCalories: String
                             )

object AgeCalculator  {

  // ===== constants =====
  val planckTimesInMillisecond: BigInt = BigInt("18550000000000000000000000000000000000000")

  val earhAgeInYears: Long = 4543000000l
  val universeAgeInYears: Long = 13799000000l
  val homoSapiensAgeInYears: Long = 200000

  val voyagerOneSpeedKmh = 62140
  val voyagerOneSpeedKmMin = 1035.6666
  val voyagerOneSpeedKmMillisec = 0.01726111111

  val distanceToProximaCentauriKm = 40170300000000l
  val neptuneFromEarth: Long = 4300000000l

  val lorentzTerm: Map[String, Double] = Map(
    "voyager" -> 1.00000000165754792604277,
    "90c" -> 2.294157338706,
    "99c" -> 7.088812050083,
    "LHC" -> 7453.559941769,
    "LEP" -> 144337.5672982,
    "OMG" -> 319438282499.9
  )

  val speeds: Map[String, Double] = Map (
    "voyager" -> 0.000057576869,
    "90c" -> 0.9,
    "99c" -> 0.99,
    "LHC" -> 0.999999991,
    "LEP" -> 0.999999999976,
    "OMG" -> 0.9999999999999999999999951
  )

  val olympicSwimmingPoolSizeLitres = 2500000

  val breathsPerSecond = 0.26666666666
  val heartbeatsPerMillisecond = 0.0013333333333333333
  val kilocaloriesPerSecond = 0.02604166666



  // --- near speed of light, time slowdown on Earth ---
  // 0.9c
  // 0.99c
  // LHC collider: 0.999999991c (7 TeV, proton)
  // LEP collider: 0.999999999976c (104.5 GeV, electron)
  // OMG particle: 0.9999999999999999999999951c




  val birthDateForm = Form[InputBirthDate](
    mapping (
      "birthDate" -> number(min = 1, max = 31),
      "birthMonth" -> number(min = 1, max = 12),
      "birthYear" -> number(min = 1700, max = 2500),
      "birthHour" -> optional( number(min = 0, max = 23) ),
      "birthMinute" -> optional( number(min = 0, max = 59) )
    )
    (InputBirthDate.apply)
    (InputBirthDate.unapply)
    verifying("Error: Invalid date! Please note that April, June, September and November have 30 days. February has 28 days, except for leap years.", ErrorHandler.checkDateFormat(_) )

  )


  def calculateResults(birthDateInput: InputBirthDate): AgeCalculationResult = {

    // -- base dates --
    val adjustedHour = birthDateInput.birthHour.getOrElse(0)
    val adjustedMinute = birthDateInput.birthMinute.getOrElse(0)

    val birthDate: DateTime = new DateTime(birthDateInput.birthYear, birthDateInput.birthMonth, birthDateInput.birthDate, adjustedHour, adjustedMinute)
    val currentDate: DateTime = DateTime.now()

    //val xxx = birthDateInput.birthDate

    def numToMonthName(monthNum: Int): String = monthNum match {
      case 1 => "January"
      case 2 => "February"
      case 3 => "March"
      case 4 => "April"
      case 5 => "May"
      case 6 => "June"
      case 7 => "July"
      case 8 => "August"
      case 9 => "September"
      case 10 => "October"
      case 11 => "November"
      case 12 => "December"
      case _ => "Undefined"
    }

    val birthDateTime: String = s"${birthDateInput.birthDate} ${numToMonthName(birthDateInput.birthMonth)} ${birthDateInput.birthYear}" +
      s"${if(birthDateInput.birthHour.isEmpty && birthDateInput.birthMinute.isEmpty) "" else ", " + adjustedHour.toString + ":" + (if(adjustedMinute < 10) "0" else "") + adjustedMinute.toString}"

    // -- calculations --

    // time units
    val yearsOld = Years.yearsBetween(birthDate, currentDate).getYears
    val monthsOld = Months.monthsBetween(birthDate, currentDate).getMonths

    val duration = new Duration(birthDate, currentDate)
    val daysOld = duration.getStandardDays
    val weeksOld = daysOld / 7
    val hoursOld = duration.getStandardHours
    val minutesOld = duration.getStandardMinutes
    val secondsOld = duration.getStandardSeconds
    val millisecondsOld = duration.getMillis
    def millisecondToPlanck(millisecond: Long): BigInt = BigInt(millisecond) * planckTimesInMillisecond

    // --- percentage of age ---
    val percentageOfEarthAge: Double = 100.toDouble * (yearsOld.toDouble / earhAgeInYears)
    val percentageOfUniverseAge: Double = 100.toDouble * (yearsOld.toDouble / universeAgeInYears)
    val earthYourLifetimes: Double = earhAgeInYears.toDouble / yearsOld
    val universeYourLifetimes: Double = universeAgeInYears.toDouble / yearsOld
    val homoSapiensYourLifetimes: Double = homoSapiensAgeInYears.toDouble / yearsOld

    // --- interstellar journey ---
    val voyagerJourneyKm = (millisecondsOld * voyagerOneSpeedKmMillisec).round

    def kmToLightYears(kilometre: Long): Double = kilometre * 1.0570008340247 / 10000000000000l
    def kmToLightMinutes(kilometre: Long): Double = kilometre * 5.5594 / 100000000l
    def kmToLightSeconds(kilometre: Long): Double = kilometre * 3.33564 / 1000000l

    def percentageToProximaCentauri(kilometre: Long): Double = 100.toDouble * kilometre.toDouble / distanceToProximaCentauriKm

    //val voyagerTimeSlowerRelativisticSeconds: BigDecimal = secondsOld - timeSlowdown(secondsOld, "voyager")


    // near light speed travel - relativistic effects
    def timeSlowdown(time: Long, speedC: String): BigDecimal = BigDecimal(time) * BigDecimal(lorentzTerm(speedC))

    def lightTraveled(time: Long, speedC: String): BigDecimal = timeSlowdown(time, speedC) * BigDecimal(speeds(speedC))
    def lightSecondToLightYear(lightSecond: BigDecimal): BigDecimal = lightSecond / 31560000
    def lightYearsTraveled(seconds: Long, speedC: String): BigDecimal = lightSecondToLightYear( lightTraveled(seconds, speedC) )


    // voyager: https://www.wolframalpha.com/input/?i=1%2Fsqrt(1-0.000057576869%5E2)
    //def slowdown90c(time: Long): Long = (time.toDouble * 2.294157338706).round // https://www.wolframalpha.com/input/?i=1%2Fsqrt(1-0.9%5E2)
    //def slowdown99c(time: Long): Long = (time.toDouble * 7.088812050083).round // https://www.wolframalpha.com/input/?i=1%2Fsqrt(1-0.99%5E2)
    //def slowdownLHC(time: Long): Long = (time.toDouble * 7453.559941769).round // https://www.wolframalpha.com/input/?i=1%2Fsqrt(1-0.999999991%5E2)
    //def slowdownLEP(time: Long): Long = (time.toDouble * 144337.5672982).round // https://www.wolframalpha.com/input/?i=1%2Fsqrt(1-0.999999999976%5E2)
    //def slowdownOMG(time: Long): Long = (time.toDouble * 319438282499.9).round // https://www.wolframalpha.com/input/?i=1%2Fsqrt(1-0.9999999999999999999999951%5E2)

    // distance: 4.36 light years
    // Similarly. at 0.99 c, the travel time would be 0.62 light years / 0.99 c = 0.63 years

    val saliveProducedLitres: Double = 0.00000001736111 * millisecondsOld // assumed 1.5l a day
    def litreToUSGallon(litre: Double): Double = litre * 0.264172
    def litreToImperialTeaspon(litre: Double): Double = 168.936 * litre

    val nailsGrownMetres: Double = 0.00000000000112007168 * millisecondsOld // assumed 3mm per month
    val hairGrownMetres: Double =  0.00000000000466696535 * millisecondsOld // assumed 1.25cm per month
    def metreToFoot(metre: Double): Double = 3.28084 * metre




    // --- Results calculation ---
    AgeCalculationResult(
      birthDateTime = birthDateTime,

      daysOldNum = daysOld,

      yearsOld = formatNumber(yearsOld),
      monthsOld = formatNumber(monthsOld),

      daysOld = formatNumber(daysOld),
      weeksOld = formatNumber(weeksOld),
      hoursOld = formatNumber(hoursOld),
      minutesOld = formatNumber(minutesOld),
      secondsOld = formatNumber(secondsOld),
      millisecondsOld = formatNumber(millisecondsOld),
      planckTimesOld =  formatBigInt( millisecondToPlanck(millisecondsOld) ),

      percentageOfEarthAge = formatDecimal(percentageOfEarthAge, 12),
      percentageOfUniverseAge = formatDecimal(percentageOfUniverseAge, 12),
      earthYourLifetimes = formatDecimal(earthYourLifetimes, 2),
      universeYourLifetimes = formatDecimal(universeYourLifetimes, 2),
      homoSapiensYourLifetimes = formatDecimal(homoSapiensYourLifetimes, 2),

      voyagerKm = formatNumber(voyagerJourneyKm),
      voyagerLightYear = formatDecimal(kmToLightYears(voyagerJourneyKm), 12),
      voyagerLightMinute = formatDecimal( kmToLightMinutes(voyagerJourneyKm), 2),
      voyagerPing = (2000.0 * kmToLightSeconds(voyagerJourneyKm)).toLong.toString,
      voyagerSolarSystem = if (neptuneFromEarth > voyagerJourneyKm) "You would not have reached Neptune yet, the last planet of our Solar System" else "By now, you would be past the Neptune's orbit, the last planet of our Solar System",
      voyagerToNearestStar = formatDecimal( percentageToProximaCentauri(voyagerJourneyKm), 12),



      //voyagerTimeSlowerRelativisticSeconds =  (timeSlowdown(secondsOld, "voyager") - secondsOld).toString,

      earthYears90c = formatDecimal(lightSecondToLightYear(timeSlowdown(secondsOld, "90c")).toDouble, 8),
      earthYears99c = formatDecimal(lightSecondToLightYear(timeSlowdown(secondsOld, "99c")).toDouble, 7),
      earthYearsLHC = formatDecimal(lightSecondToLightYear(timeSlowdown(secondsOld, "LHC")).toDouble, 4),
      earthYearsLEP = formatDecimal(lightSecondToLightYear(timeSlowdown(secondsOld, "LEP")).toDouble, 3),
      earthYearsOMG = formatDecimal(lightSecondToLightYear(timeSlowdown(secondsOld, "OMG")).toDouble, 1),

      lyTravel90c = formatDecimal(lightYearsTraveled(secondsOld, "90c").toDouble, 8),
      lyTravel99c = formatDecimal(lightYearsTraveled(secondsOld, "99c").toDouble, 8),
      lyTravelLHC = formatDecimal(lightYearsTraveled(secondsOld, "LHC").toDouble, 8),
      lyTravelLEP = formatDecimal(lightYearsTraveled(secondsOld, "LEP").toDouble, 8),
      lyTravelOMG = formatDecimal(lightYearsTraveled(secondsOld, "OMG").toDouble, 8),

      salivaProducedLitres = formatDecimal(saliveProducedLitres, 6),
      salivaProducedUSGallons = formatDecimal(litreToUSGallon(saliveProducedLitres), 6),
      salivaProducedImperialTeaspoon = formatDecimal(litreToImperialTeaspon(saliveProducedLitres), 2),
      salivaOlympicPoolPercent = formatDecimal(100.0 * saliveProducedLitres / olympicSwimmingPoolSizeLitres, 3),
      nailsGrownMetres = formatDecimal(nailsGrownMetres, 10),
      nailsGrownFeet = formatDecimal(metreToFoot(nailsGrownMetres), 10),
      hairGrownMetres = formatDecimal(hairGrownMetres, 10),
      hairGrownFeet = formatDecimal(metreToFoot(hairGrownMetres), 10),
      breaths = formatNumber((breathsPerSecond * secondsOld).toLong),
      heartbeats = formatNumber((heartbeatsPerMillisecond * millisecondsOld).toLong),
      kilocalories = formatNumber((kilocaloriesPerSecond * secondsOld).toLong),
      olympicSwimmingPoolCaloriesNum = olympicSwimmingPoolSizeLitres.toDouble / (kilocaloriesPerSecond * secondsOld),
      olympicSwimmingPoolCalories = formatDecimal(olympicSwimmingPoolSizeLitres.toDouble / (kilocaloriesPerSecond * secondsOld), 6)
    )

  }


  // -- formatters --
  def formatNumber(num: Long): String = NumberFormat.getNumberInstance(Locale.US).format(num)
  def formatBigInt(num: BigInt): String = NumberFormat.getNumberInstance(Locale.US).format(num)
  def formatDecimal(dec: Double, round: Int): String = {

    val iPart: Long = dec.toLong
    val fPart: Double = dec - iPart

    val iPartString: String = formatNumber(iPart)

    val fPartFormatter = new DecimalFormat("#" + "." + "#" * (if(round < 1) 1 else round))

    val fPartString = fPartFormatter.format(fPart).tail

    iPartString + fPartString
  }

}