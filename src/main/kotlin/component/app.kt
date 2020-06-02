package component

import data.*
import hoc.withDisplayName
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.*
import kotlin.browser.document
import kotlin.reflect.KClass

interface AppProps : RProps {
//    var lessons: Array<Lesson>
//    var students: Array<Student>
}

interface AppState : RState {
    var presents: Array<Array<Boolean>>
    var lessons: Array<Lesson>
    var students: Array<Student>
}

interface RouteNumberResult : RProps {
    var number: String
}

class App : RComponent<AppProps, AppState>() {
    override fun componentWillMount() {
        state.lessons = lessonsList
        state.students = studentList
        state.presents = Array(state.lessons.size) {
            Array(state.students.size) { false }
        }

    }

    override fun RBuilder.render() {
        header {
            h1 { +"App" }
            nav {
                ul {
                    li { navLink("/lessons") { +"Lessons" } }
                    li { navLink("/students") { +"Students" } }
                    li { navLink("/editlesson") { +"Edit lessons" } }
                    li { navLink("/editstudents") { +"Edit students" } }
                }
            }
        }

        switch {
            route("/lessons",
                    exact = true,
                    render = {
                        anyList(state.lessons, "Lessons", "/lessons")
                    }
            )
            route("/students",
                    exact = true,
                    render = {
                        anyList(state.students, "Students", "/students")
                    }
            )
            route("/editlesson",
                    exact = true,
                    render = {
                        anyEdit(RBuilder::addLesson, RBuilder::anyList, state.lessons, "Edit lessons", "/lessons", addLessonFunc(),{deleteLessonFunc(it)} )
                    }
            )

            route("/editstudents",
                    exact = true,
                    render = {
                        anyEdit(RBuilder::addStudent,RBuilder::anyList,state.students,"Edit students","/students",addStudentFunc(),{deleteStudentFunc(it)})
                    }
            )
            route("/lessons/:number",
                    render = { route_props: RouteResultProps<RouteNumberResult> ->
                        val num = route_props.match.params.number.toIntOrNull() ?: -1
                        val lesson = state.lessons.getOrNull(num)
                        if (lesson != null)
                            anyFull(
                                    RBuilder::student,
                                    lesson,
                                    state.students,
                                    state.presents[num]
                            ) { onClick(num, it) }
                        else
                            p { +"No such lesson" }
                    }
            )
            route("/students/:number",
                    render = { route_props: RouteResultProps<RouteNumberResult> ->
                        val num = route_props.match.params.number.toIntOrNull() ?: -1
                        val student = state.students.getOrNull(num)
                        if (student != null)
                            anyFull(
                                    RBuilder::lesson,
                                    student,
                                    state.lessons,
                                    state.presents.map {
                                        it[num]
                                    }.toTypedArray()
                            ) { onClick(it, num) }
                        else
                            p { +"No such student" }
                    }
            )
        }
    }

    fun addLessonFunc() = { _: Event ->
        val newLesson = document.getElementById("lesson") as HTMLInputElement
        setState {
            lessons += Lesson(newLesson.value)
            presents += arrayOf(
                    Array(state.students.size) { false })
        }
    }

    fun deleteLessonFunc(index: Int): (Event) -> Unit {
        return { _: Event ->
            val deleteL = state.lessons.toMutableList().apply { removeAt(index) }.toTypedArray()
            val newpresents = state.presents.toMutableList().apply { removeAt(index) }.toTypedArray()
            setState {
                lessons = deleteL
                presents = newpresents
            }
        }
    }

    fun addStudentFunc() = { _: Event ->
        val newName = document.getElementById("name") as HTMLInputElement
        val newSurname = document.getElementById("surname") as HTMLInputElement
        setState {
            students += Student(newName.value, newSurname.value)
            presents.mapIndexed { index, _ ->
                presents[index] += arrayOf(false)
            }
        }
    }

    fun deleteStudentFunc(index: Int): (Event) -> Unit {
        return { _: Event ->
            val deleteStud = state.students.toMutableList().apply { removeAt(index) }.toTypedArray()
            val newpresents = state.presents.mapIndexed { id, _ ->
                state.presents[id].toMutableList().apply {
                    removeAt(index)
                }.toTypedArray()
            }
            setState {
                students = deleteStud
                presents = newpresents.toTypedArray()
            }
        }
    }


    fun onClick(indexLesson: Int, indexStudent: Int) =
            { _: Event ->
                setState {
                    presents[indexLesson][indexStudent] =
                            !presents[indexLesson][indexStudent]
                }
            }
}

fun RBuilder.app(
) =
        child(
                withDisplayName("AppHoc", App::class)
        ) {
        }





