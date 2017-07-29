package models

import views.html.helper.FieldConstructor

object CustomConstructors {
  implicit val myFields = FieldConstructor(views.html.customConstructor.f)
}
