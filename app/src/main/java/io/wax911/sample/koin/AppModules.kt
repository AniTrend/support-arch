package io.wax911.sample.koin

import io.wax911.sample.core.koin.coreModules
import io.wax911.sample.data.koin.dataModules
import io.wax911.sample.service.FileProviderService
import org.koin.dsl.module

private val appModule = module {
    factory {
        FileProviderService()
    }
}

val appModules = listOf(
    appModule
) + coreModules + dataModules