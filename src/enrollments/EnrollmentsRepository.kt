package com.example.enrollments

import com.example.Enrollments
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

class EnrollmentsRepository {
    fun findEnrollments(studentId: Long, courseId: Long) =
        Enrollments.select { (Enrollments.student_id eq studentId) and (Enrollments.course_enrolled eq courseId) }
            .map { Enrollments.toEnrollment(it) }
            .firstOrNull()



    fun isEnrolled(studentId: Long, courseId: Long)= findEnrollments(studentId, courseId) != null


}

private fun Enrollments.toEnrollment(it: ResultRow) =
    Enrollment(it[Enrollments.id],it[Enrollments.student_id],it[Enrollments.course_enrolled])