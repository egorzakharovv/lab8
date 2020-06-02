package component

import data.Lesson
import hoc.withDisplayName
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import kotlin.browser.document

interface AddLessonProps : RProps {
    var onClick: (Event) -> Unit
}

val fAddLesson =
        functionalComponent<AddLessonProps> {
            h3 { +"Введите название урока урока"}
            div {
                li {
                    input(type = InputType.text) {
                        attrs {
                            id = "lesson"
                            placeholder = "Название"
                        }
                    }
                }
                button {
                    +"Добавить"
                    attrs.onClickFunction = it.onClick
                }
            }
        }



fun RBuilder.addLesson(
        onClick: (Event) -> Unit
) = child(
        withDisplayName("addLesson", fAddLesson)
) {
    attrs.onClick = onClick
}