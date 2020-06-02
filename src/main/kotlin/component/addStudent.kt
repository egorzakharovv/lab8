package component

import data.Student
import hoc.withDisplayName
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.onClick
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child
import react.dom.*
import react.functionalComponent
import kotlin.browser.document

interface AddStudentProps : RProps {
    var onClick: (Event) -> Unit
}

val fAddStudent =
        functionalComponent<AddStudentProps> {
            h3 { +"Введите имя и фамилию студента" }
            div {
                li {
                    input(type = InputType.text) {
                        attrs {
                            id = "name"
                            placeholder = "Имя"
                        }
                    }
                    input(type = InputType.text) {
                        attrs {
                            id = "surname"
                            placeholder = "Фамилия"
                        }
                    }
                }
                button {
                    +"Добавить"
                    attrs.onClickFunction = it.onClick
                }
            }
        }

fun RBuilder.addStudent(
        onClick: (Event) -> Unit
) = child(
        withDisplayName("studentsAdd", fAddStudent)
) {
    attrs.onClick = onClick
}