package com.example.students

import com.example.courses.CoursesRepository
import com.example.enrollments.EnrollmentsRepository
import com.example.programs.ProgramsRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class StudentsService(
    private val studentsRepository: StudentsRepository,
    private val programsRepository: ProgramsRepository,
    private val coursesRepository: CoursesRepository,
    private val enrollmentsRepository: EnrollmentsRepository,
    private val db: Database
) {
    fun findStudents(searchText: String?): List<Student> = transaction(db) {
        when (searchText) {
            null -> studentsRepository.loadStudents()
            else -> studentsRepository.loadStudentsBySearchText(searchText)
        }
    }

    fun createStudent(name: String, lastName: String, program_id: Long): Long = transaction(db) {
        programsRepository.findById(program_id) ?: throw IllegalArgumentException("that program doesn't exist")
        studentsRepository.createStudent(name, lastName, program_id)
    }

    fun enrollCourse(student_id: Long, course_id: Long) {
        studentsRepository.findById(student_id)?.id ?: throw IllegalArgumentException("that student doesn't exist")
        coursesRepository.findById(course_id)?.id ?: throw IllegalArgumentException("that course doesn't exist")

    }

    fun leaveCourse(studentId: Long, courseId: Long) {
        studentsRepository.findById(studentId) ?: throw IllegalArgumentException("that student doesn't exist")
        if(!enrollmentsRepository.isEnrolled(studentId, courseId)) throw IllegalArgumentException("the student isn't enrolled")
        coursesRepository.leaveCourse(studentId, courseId)

    }
}