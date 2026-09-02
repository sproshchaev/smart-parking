package com.prosoft.parking.storage

import com.prosoft.parking.model.Session
import java.nio.file.Path
import kotlin.io.path.*

class SessionLog(private val path: Path) {

    private fun ensureFile() {
        path.createParentDirectories()
        if (!path.exists()) path.createFile()
    }

    fun clear() {
        path.deleteIfExists()
        ensureFile()
    }

    fun append(session: Session, minutes: Int, fee: Int) {
        ensureFile()
        path.appendText("${session.plate};${session.spotId};$minutes;$fee\n")
    }

    fun revenue(): Int =
        if (!path.exists()) 0
        else path.useLines { lines ->
            lines.filter {
                it.isNotBlank()
            }.sumOf { it.split(";")[3].toInt() }
        }

    fun topPlates(limit: Int): List<Pair<String, Int>> =
        if (!path.exists()) emptyList()
        else path.useLines { lines ->
            lines.filter { it.isNotBlank() }
                .map { it.split(";") }
                .groupBy({ it[0] }, { it[3].toInt() })
                .map { (plate, fees) -> plate to fees.sum() }
                .sortedByDescending { it.second }
                .take(limit)
        }

}