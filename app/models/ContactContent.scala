package models

import play.api.data.Form
import play.api.data.Forms._
import views.html.helper



object ContactContent {

  case class ContactFormInput(fromSender: String, senderEmail: Option[String], messageSubject: String, messageContent: String)

  val contactForm = Form[ContactFormInput] (
    mapping (
      "fromSender" -> nonEmptyText(maxLength = 100),
      "senderEmail" -> optional(text(maxLength = 100)),
      "messageSubject" -> nonEmptyText(maxLength = 50),
      "messageContent" -> nonEmptyText(maxLength = 2000)
    )
    (ContactFormInput.apply)
    (ContactFormInput.unapply)
  )


  val xxx = helper.CSRF


}
