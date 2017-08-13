package models

import org.joda.time.format.DateTimeFormat
import play.api.data.FormError

object ErrorHandler {

  // date and time validation
  val dateFormat = DateTimeFormat forPattern  "dd/MM/yyyy"
  def checkDate(date: String): Boolean =
    try {
      dateFormat parseDateTime date
      true
    } catch {
      case ex: IllegalArgumentException => false
    }

  def checkDateFormat(dateInput: InputBirthDate): Boolean = {
    val dateString = dateInput.birthDate.toString + "/" + dateInput.birthMonth.toString + "/" + dateInput.birthYear
    checkDate(dateString)
  }

  def translateErrorMessage(errorText: String): String = errorText match {
    case "error.number" => "Error: Invalid values! Select Year, Month and Date values from the dropdown. Optionally, enter Hour and Minute."
    case "error.min" | "error.max" => "Error: Invalid values! Hour must be between 0 and 23 and Minute must be between 0 and 59."
    case "error.required" => "Error: Fill in all required fields!"
    case _ => errorText
  }

  def showFormErrors(errors: Seq[FormError]): String = {
    errors
      .map{case FormError(_,message,_) => message.mkString(", ")}
      .toSet[String]
      .map(models.ErrorHandler.translateErrorMessage(_))
      .mkString("<ul class=\"errors-list\"><li>","</li>\n<li>","</li></ul>")
  }


}
