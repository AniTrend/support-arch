package co.anitrend.arch.processor.utils

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import java.io.File

@OptIn(ExperimentalCompilerApi::class)
object KspTestUtil {

    fun compile(
        sourceFiles: List<SourceFile>,
        symbolProcessorProviders: List<SymbolProcessorProvider>,
    ): JvmCompilationResult {
        return KotlinCompilation().also { compiler ->
            compiler.sources = sourceFiles
            compiler.inheritClassPath = true
            compiler.useKsp2()
            compiler.symbolProcessorProviders += symbolProcessorProviders
            compiler.messageOutputStream = System.out
        }.compile()
    }

    fun getSourcesFromResult(result: JvmCompilationResult): List<File> {
        val kspSourcesDir = result.outputDirectory.resolve("../ksp/sources")
        return kspSourcesDir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .toList()
    }
}