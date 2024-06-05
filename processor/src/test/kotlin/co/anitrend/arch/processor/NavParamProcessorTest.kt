package co.anitrend.arch.processor

import com.tschuchort.compiletesting.SourceFile
import kotlin.test.Test

class NavParamProcessorTest {

    @Test
    fun `should generate both params for 'Id' and 'Description'`() {
        verifyPassing(
            source = SourceFile.kotlin(
                name = "MyItem.kt",
                contents = template(
                    """
                        package io.wax911.items

                        import co.anitrend.arch.annotation.NavParam

                        object MyItem {
                            @NavParam
                            data class Id(val id: String)
                            
                            @NavParam
                            data class Description(val description: String)
                        }
                    """
                )
            ),
            output = template(
                """
                    package io.wax911.items

                    import kotlin.String

                    public object MyItemParam {
                      public const val ID: String = "io.wax911.items.MyItem.Id"

                      public const val DESCRIPTION: String = "io.wax911.items.MyItem.Description"
                    }
                """
            )
        )
    }

    @Test
    fun `should only generate one param for 'Id' when 'Description' is disabled`() {
        verifyPassing(
            source = SourceFile.kotlin(
                name = "MyItem.kt",
                contents = template(
                    """
                        package io.wax911.items

                        import co.anitrend.arch.annotation.NavParam

                        object MyItem {
                            @NavParam
                            data class Id(val id: String)
                            
                            @NavParam(enabled = false)
                            data class Description(val description: String)
                        }
                    """
                )
            ),
            output = template(
                """
                    package io.wax911.items

                    import kotlin.String

                    public object MyItemParam {
                      public const val ID: String = "io.wax911.items.MyItem.Id"
                    }
                """
            )
        )
    }

    @Test
    fun `should fail when annotation is applied to a top level class without nesting`() {
        verifyFailing(
            source = SourceFile.kotlin(
                name = "MyItem.kt",
                contents = template(
                    """
                        package io.wax911.items

                        import co.anitrend.arch.annotation.NavParam

                        @NavParam
                        data class Id(val id: String)
                    """
                )
            ),
        )
    }
}