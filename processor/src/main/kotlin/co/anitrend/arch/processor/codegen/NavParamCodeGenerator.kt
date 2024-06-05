/**
 * Copyright 2024 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.anitrend.arch.processor.codegen

import co.anitrend.arch.annotation.NavParam
import co.anitrend.arch.processor.codegen.contract.ICodeGenerator
import co.anitrend.arch.processor.codegen.extension.annotationArgOf
import co.anitrend.arch.processor.model.Spec
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

class NavParamCodeGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : ICodeGenerator {
    private fun generateContent(
        classes: List<KSClassDeclaration>,
        spec: Spec,
    ): FileSpec {
        val fileSpecBuilder = FileSpec.builder(spec.packageName, spec.fileName)
        val typeSpecBuilder = TypeSpec.objectBuilder(spec.fileName)

        classes.forEach { classDeclaration ->
            val className = classDeclaration.simpleName.asString().uppercase()
            val propertyValue = requireNotNull(classDeclaration.qualifiedName).asString()

            val argument =
                classDeclaration.annotationArgOf {
                    it.name?.getShortName() == NavParam::enabled.name
                }

            if (argument.value == true) {
                typeSpecBuilder.addProperty(
                    PropertySpec.builder(className, String::class)
                        .addModifiers(KModifier.CONST)
                        .initializer("%S", propertyValue)
                        .build(),
                )
            }
        }

        fileSpecBuilder.addType(typeSpecBuilder.build())
        return fileSpecBuilder.build()
    }

    private fun generateSourceFiles(
        classes: List<KSClassDeclaration>,
        spec: Spec,
    ) {
        val fileSpec = generateContent(classes = classes, spec = spec)

        val file =
            codeGenerator.createNewFile(
                dependencies =
                    Dependencies(
                        aggregating = false,
                        sources =
                            classes.mapNotNull(
                                transform = KSClassDeclaration::containingFile,
                            ).toTypedArray(),
                    ),
                packageName = spec.packageName,
                fileName = spec.fileName,
            )

        file.writer().use { stream ->
            runCatching {
                fileSpec.writeTo(stream)
            }.onFailure(logger::exception)
            stream.flush()
        }
    }

    override operator fun invoke(classes: List<KSClassDeclaration>) {
        val packageName = classes.first().packageName.asString()
        val parentClassName =
            classes.first().parentDeclaration?.simpleName?.asString()
                ?: throw UnsupportedOperationException(
                    "The annotated item in `$packageName` should belong in a parent class",
                )

        val spec =
            Spec(
                packageName = packageName,
                fileName = "${parentClassName}Param",
            )

        generateSourceFiles(classes = classes, spec = spec)
    }
}
