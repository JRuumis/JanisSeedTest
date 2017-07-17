// agecalc.info
// ramavasoftware.com

import org.joda.time._



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


(1900 to 2017) map (i => i->i)




