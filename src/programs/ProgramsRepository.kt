package com.example.programs

import com.example.Programs
import org.jetbrains.exposed.sql.*

class ProgramsRepository {
    fun loadStudents(): List<Program> = Programs.selectAll().map { Programs.toProgram(it) }

    fun findById(id:Long): Program? = Programs
            .select { Programs.id eq id }
            .map { Programs.toProgram(it) }
            .firstOrNull()



}

private fun Programs.toProgram(it: ResultRow) = Program(it[id],it[name])
