package com.olezhaku.idsc.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.TimeUnit

class NoRootError : Exception("Root not available")
class TimeoutError : Exception("Timed out")

object RootShell {
    private const val TIMEOUT_MS = 15_000L

    suspend fun run(command: String) = withContext(Dispatchers.IO) {
        val process = try {
            Runtime.getRuntime().exec("su")
        } catch (e: IOException) {
            throw NoRootError()
        }

        process.outputStream.bufferedWriter().use { writer ->
            writer.write(command)
            writer.newLine()
            writer.write("exit")
            writer.newLine()
        }

        val finished = process.waitFor(TIMEOUT_MS, TimeUnit.MILLISECONDS)

        val stdout = process.inputStream.bufferedReader().readText()
        val stderr = process.errorStream.bufferedReader().readText()

        if (!finished) {
            process.destroyForcibly()
            throw TimeoutError()
        }

        val exitCode = process.exitValue()

        if (exitCode != 0) {
            throw RuntimeException(
                stderr.ifBlank {
                    stdout.ifBlank { "Failed with exit code $exitCode" }
                }
            )
        }
    }
}