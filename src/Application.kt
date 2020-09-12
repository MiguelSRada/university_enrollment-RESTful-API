package com.example

import com.example.courses.CoursesRepository
import com.example.programs.ProgramsRepository
import com.example.students.StudentsRepository
import com.example.students.StudentsService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

data class Error(val error: String)
data class Result(val result: String)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(StatusPages) {
        exception<IllegalArgumentException> { cause ->
            call.respond(HttpStatusCode.BadRequest, Error(cause.message ?: "unknown system exception"))
        }
    }

    val db = Database.connect(
        "jdbc:mysql://localhost:3306/universityEnrollments?serverTimezone=UTC", driver = "com.mysql.cj.jdbc.Driver",
        user = "root", password = "@Bestyear2020"
    )

    val repositoryStudents = StudentsRepository()
    val repositoryPrograms = ProgramsRepository()
    val repositoryCourses = CoursesRepository()
    val studentsService = StudentsService(repositoryStudents, repositoryPrograms,repositoryCourses, db)

    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, Result("university-enrollment"))
        }

        route("students") {
            get("/") {
                call.respond(
                    HttpStatusCode.OK,
                    studentsService.findStudents(call.request.queryParameters["searchText"])
                )
            }
        }


    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

