package co.anitrend.arch.processor.utils

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import java.io.File

@OptIn(ExperimentalCompilerApi::class)
object KspTestUtil {

    fun compile(
        sourceFiles: List<SourceFile>,
        symbolProcessorProviders: List<SymbolProcessorProvider>,
    ): KotlinCompilation.Result {
        return KotlinCompilation().also { compiler ->
            compiler.sources = sourceFiles
            compiler.inheritClassPath = true
            compiler.symbolProcessorProviders = symbolProcessorProviders
            compiler.messageOutputStream = System.out
            compiler.kspWithCompilation = true
        }.compile()
    }

    fun getSourcesFromResult(result: KotlinCompilation.Result): List<File> {
        val kspSourcesDir = result.outputDirectory.resolve("../ksp/sources")
        return kspSourcesDir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .toList()
    }
}