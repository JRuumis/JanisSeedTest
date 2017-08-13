package controllers

import javax.inject.Inject

import play.api.libs.mailer._
import org.apache.commons.mail.EmailAttachment

import models.{AgeCalculator, ContactContent}
import play.api.mvc._

class ContactFormController @Inject()(cc: ControllerComponents, mailerClient: MailerClient) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def showContactForm() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.contactForm(ContactContent.contactForm))
  }

  def submitForm() = Action {

    implicit request: Request[AnyContent] =>

      ContactContent.contactForm.bindFromRequest.fold(

        // error
        formWithErrors => { BadRequest(views.html.contactForm(formWithErrors)) },

        // validation passed
        contactFormContent => {

          val email = Email(
            subject = s"Your Age Calculator: ${contactFormContent.messageSubject}",
            from = "Your Age Calculator <ramava.software@gmail.com>",
            to = Seq("Ramava Software <ramava.software@gmail.com>"),
            bodyText = Some(s"Sender: ${contactFormContent.fromSender}\n" +
                            s"Email address:${contactFormContent.senderEmail.getOrElse("N/A")}\n\n" +
                            s"Message:\n" +
                            s"${contactFormContent.messageContent}")
          )
          mailerClient.send(email)

          // Ok(views.html.confirmation(""))
          Redirect(routes.BirthDateInputController.index)
        }
      )


    //Ok("TODO: Submit")


  }

}
