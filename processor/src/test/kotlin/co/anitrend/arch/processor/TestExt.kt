package co.anitrend.arch.processor

import co.anitrend.arch.processor.provider.NavParamProcessorProvider
import co.anitrend.arch.processor.utils.KspTestUtil
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.jupiter.api.Assertions


fun template(@Language("kotlin") content: String) = content.trimIndent()

@OptIn(ExperimentalCompilerApi::class)
fun verifyPassing(
    source: SourceFile,
    @Language("kotlin") output: String
) {
    val result = KspTestUtil.compile(
        sourceFiles = listOf(source),
        symbolProcessorProviders = listOf(NavParamProcessorProvider()),
    )

    Assertions.assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

    val generatedFiles = KspTestUtil.getSourcesFromResult(result)
    Assertions.assertTrue(
        generatedFiles.isNotEmpty(),
        "`generatedFiles` cannot be empty, make sure that files are being written"
    )

    val generatedFile = generatedFiles.find { it.name == "MyItemParam.kt" }
    Assertions.assertNotNull(
        generatedFile,
        "No file matching `MyItemParam.kt` exists in `generatedFiles`"
    )

    Assertions.assertEquals(output, generatedFile?.readText()?.trim())
}

@OptIn(ExperimentalCompilerApi::class)
fun verifyFailing(source: SourceFile) {
    val result = KspTestUtil.compile(
        sourceFiles = listOf(source),
        symbolProcessorProviders = listOf(NavParamProcessorProvider()),
    )

    Assertions.assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.exitCode)

    val generatedFiles = KspTestUtil.getSourcesFromResult(result)
    Assertions.assertTrue(
        generatedFiles.isEmpty(),
        "`generatedFiles` should be empty"
    )
}