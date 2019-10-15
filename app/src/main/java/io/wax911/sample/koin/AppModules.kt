package io.wax911.sample.koin

import co.anitrend.arch.core.provider.SupportFileProvider
import io.wax911.sample.core.koin.coreModules
import io.wax911.sample.data.koin.dataModules
import org.koin.dsl.module

private val appModule = module {
    factory {
        SupportFileProvider()
    }
}

val appModules = listOf(
    appModule
) + coreModules + dataModules