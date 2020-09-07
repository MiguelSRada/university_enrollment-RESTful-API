package com.example

import com.example.Courses.autoIncrement
import com.example.Courses.primaryKey
import com.example.ProgramsAndCourses.references
import com.example.Students.autoIncrement
import com.example.Students.primaryKey
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.dateTimeParam
import kotlin.concurrent.timer

object Students: Table("students"){
    val id = long("id").primaryKey().autoIncrement()
    val name = varchar("name",255)
    val lastName = varchar("last name",255)
    val program_id = long("program_id").references(Programs.id)
}

object Programs: Table("programs"){
    val id = long("id").primaryKey().autoIncrement()
    val name = varchar("name",255)
}

object Courses: Table("courses"){
    val id = long("id").primaryKey().autoIncrement()
    val name = varchar("name",255)
    val credits = integer("credits")
    val capacity = integer("capacity")
    val enrolled = integer("enrolled")
}

object Enrollments: Table("enrollments"){
    val id = long("id").primaryKey().autoIncrement()
    val student_id = long("student_id").references(Students.id)
    val course_enrolled = long("course_enrolled").references(ProgramsAndCourses.id)
}

object ProgramsAndCourses: Table(){
    val id = long("programAndCourse").primaryKey()
    val program_id = long("program_id").references(Programs.id)
    val course_id = long("course_id").references(Courses.id)
}

object StudentsAndCourses: Table(){
    val student_id = long("student_id").references(Students.id)
    val course_id = long("course_id").references(Courses.id)
}