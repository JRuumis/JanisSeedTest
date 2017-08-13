// agecalc.info
// ramavasoftware.com

//import java.math.MathContext




/*
def pow(i: BigDecimal): BigDecimal = i*i

def sqrt(a: BigDecimal, scale: Int): BigDecimal = {
  var x = new BigDecimal( Math.sqrt(a.doubleValue()), MathContext.DECIMAL64 )

  if (scale < 17) {
    return x
  }

  val b2 = new BigDecimal(2)
  var tempScale = 16
  while(tempScale < scale){
    //x = x - (x * x - a) / (2 * x);
    x = x.subtract(x.multiply(x).subtract(a).divide(x.multiply(b2), scale, BigDecimal.ROUND_HALF_EVEN))
    tempScale *= 2
  }
  return x
}

val xxxxx = 1/sqrt(1-pow(0.9999999999999999999999951))
*/

Set(1,2,3,4,5) map (i => i + 1)

List(1,2,3,4,5,6).toSet[Int] map (i => i+1)



"-5".toInt


7l / 5

import java.math.MathContext
import java.util.Calendar

import org.joda.time._



val cal = Calendar.getInstance()
val Year = cal.get(Calendar.YEAR )

Calendar.getInstance()get(Calendar.YEAR)


(38.toDouble / 4543000000l) * 100


"0" * 40


BigInt("18550000000000000000000000000000000000000000")


val aaa = BigInt("100000000000000000000000000000000000000000000000000000")


val now = DateTime.now

val janisBD = new org.joda.time.DateTime(1979, 5, 28, 15, 0) // , DateTimeZone zone)

//(year=1979, 5, 28, 15, 0)
//val janisBD3 = new DateTime(year=1979, monthOfYear=5, dayOfMonth=28, hourOfDay=15, minuteOfHour=0)

val janisTimeZone = DateTimeZone.forID("GMT")
val janisBD2 = new org.joda.time.DateTime(1979, 5, 28, 15, 0, janisTimeZone)

val allTimeZones = DateTimeZone.getAvailableIDs()


//val fom = DateTimeFormatter

val daysBetween = Days.daysBetween(janisBD, now).toStandardDuration //.getStandardDays

val xxx = new org.joda.time.Duration(janisBD, now).getStandardHours


//val nextMonth = DateTime.parse("28/05/1907", DateTimeFormatter( "DD/MM/YYYY")

//Hours.hours(1)
//Hours.hours(3)

//Hours.hoursBetween(now)


//(1900 to 2017) map (i => i->i)




