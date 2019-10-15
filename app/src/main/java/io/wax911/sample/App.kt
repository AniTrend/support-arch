package io.wax911.sample

import io.wax911.sample.core.TraktTrendApplication
import io.wax911.sample.koin.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : TraktTrendApplication() {

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     *
     * Initializes dependencies for the entire application, this function is automatically called
     * in [onCreate] as the first call to assure all injections are available
     */
    override fun initializeDependencyInjection() {
        startKoin {
            androidLogger()
            androidContext(
                applicationContext
            )
            modules(appModules)
        }
    }
}
