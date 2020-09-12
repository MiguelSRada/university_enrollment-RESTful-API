package com.example.students

import com.example.Students
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class StudentsRepository {
    fun loadStudents(): List<Student> = Students.selectAll().map { Students.toStudent(it) }

    fun loadStudentsBySearchText(searchText: String): List<Student> =
        Students
            .select { (Students.name like searchText(searchText)) or (Students.lastName like searchText(searchText)) }
            .map { Students.toStudent(it) }

    fun createStudent(name: String, lastName: String, programId: Long): Long {
        return Students.insert {
            it[Students.name] = name
            it[Students.lastName] = lastName
            it[Students.program_id]
        }[Students.id]
    }

    fun findById(id: Long): Student? =
        Students
            .select { Students.id eq id }
            .map { Students.toStudent(it) }
            .firstOrNull()
}

private fun Students.toStudent(it: ResultRow): Student =
    Student(it[id], it[name], it[lastName], it[program_id])

private fun searchText(searchText: String): String {
    var newString = searchText
    if (newString.endsWith("%").not()) newString = "$newString%"
    if (newString.startsWith("%").not()) newString = "%$newString"
    return newString
}

