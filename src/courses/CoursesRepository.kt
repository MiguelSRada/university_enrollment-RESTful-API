package com.example.courses

import com.example.Courses
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class CoursesRepository {
    fun loadCourses(): List<Course> = Courses.selectAll().map { Courses.toCourse(it) }

    fun findById(id: Long) = Courses.select { Courses.id eq id }.map { Courses.toCourse(it) }.firstOrNull()

    fun checkForAvailableSpots(courseId: Long): Boolean {
        val course = findById(courseId) ?: throw IllegalArgumentException("that course doesn't exist")
        return course.capacity > course.enrolled
    }

    fun courseEnrollment(courseId: Long) {
        val course = findById(courseId) ?: throw IllegalArgumentException("that course doesn't exist")
        if (checkForAvailableSpots(courseId)) Courses.update({Courses.id eq courseId}) {
            it[enrolled] = course.enrolled + 1
            }
    }

    fun leaveCourse(studentId: Long, courseId: Long) {
        val course = findById(courseId) ?: throw IllegalArgumentException("that course doesn't exist")
        Courses.update({Courses.id eq courseId}) {
            it[enrolled] = course.enrolled + 1
        }


    }

}

private fun Courses.toCourse(it: ResultRow) = Course(it[id], it[name], it[credits], it[capacity], it[enrolled])
