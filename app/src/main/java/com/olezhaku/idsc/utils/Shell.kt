package com.olezhaku.idsc.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit

class NoRootError : Exception("Root not available")

object Shell {
    private const val TIMEOUT_MS = 15_000L

    private suspend fun exec(command: String): String = withContext(Dispatchers.IO) {
        val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))

        val finished = process.waitFor(TIMEOUT_MS, TimeUnit.MILLISECONDS)

        if (!finished) {
            process.destroyForcibly()
            throw RuntimeException("Command timed out: $command")
        }

        val stdout = process.inputStream.bufferedReader().readText()
        val stderr = process.errorStream.bufferedReader().readText()

        if (process.exitValue() != 0) {
            throw RuntimeException(stderr.ifBlank { "Command failed: $command" })
        }

        stdout
    }

    suspend fun executeScript(context: Context, script: String): String = withContext(Dispatchers.IO) {
        val suProbe = exec("which su")
        if (suProbe.trim().isEmpty()) {
            throw NoRootError()
        }

        val file = File(context.cacheDir, "run-${System.currentTimeMillis()}.sh")

        try {
            file.writeText(script)
            file.setExecutable(true, false)

            exec("chmod 755 ${file.absolutePath}")

            exec("su -c ${file.absolutePath}")
        } finally {
            runCatching {
                file.delete()
            }
        }
    }
}
