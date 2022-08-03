package co.anitrend.arch.buildSrc.plugin.extensions

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

/**
 * Run cli commands on [Project.getRootProject] and returns the output in string format
 *
 * @param command The CLI command to execute
 *
 * @return [String]
 */
fun Project.runCommand(command: String): String {
    val outputStream = ByteArrayOutputStream()
    rootProject.exec {
        commandLine(command.split(' '))
        standardOutput = outputStream
    }
    return String(outputStream.toByteArray()).trim()
}

val Project.gitSha: String
    get() = runCommand("git rev-parse --short=8 HEAD")

val Project.gitCommitCount: String
    get() = runCommand("git rev-list --count HEAD")

val Project.gitBranch: String
    get() = runCommand("git rev-parse --abbrev-ref HEAD")