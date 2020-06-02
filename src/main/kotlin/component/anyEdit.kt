package component

import hoc.withDisplayName
import kotlinx.html.classes
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.style
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import kotlin.browser.document

interface AnyEditProps<S> : RProps {
    var subobjs: Array<S>
    var name: String
    var path: String
    var addFunc: (Event) -> Unit
    var delFunc: (Int) -> (Event) -> Unit
}

fun <S> fAnyEdit(
        rEdit: RBuilder.((Event) -> Unit) -> ReactElement,
        rComponent: RBuilder.(Array<S>, String, String) -> ReactElement
) =
        functionalComponent<AnyEditProps<S>> {
            div {
                h2 {
                    +"Редактирование"
                }
                rEdit(it.addFunc)
                it.subobjs.mapIndexed { index, s ->
                    li {
                        +"$s"
                        button {
                            +"Удалить"
                            attrs.onClickFunction = it.delFunc(index)
                        }
                    }
                }
                rComponent(it.subobjs, it.name, it.path)
            }
        }

fun <S> RBuilder.anyEdit(
        rEdit: RBuilder.((Event) -> Unit) -> ReactElement,
        rComponent: RBuilder.(Array<S>, String, String) -> ReactElement,
        subobjs: Array<S>,
        name: String,
        path: String,
        addFunc: (Event) -> Unit,
        delFunc: (Int) -> (Event) -> Unit
) = child(
        withDisplayName("Edit", fAnyEdit<S>(rEdit, rComponent))
) {
    attrs.subobjs = subobjs
    attrs.name = name
    attrs.path = path
    attrs.addFunc = addFunc
    attrs.delFunc = delFunc
}