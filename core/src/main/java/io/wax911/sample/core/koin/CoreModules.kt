package io.wax911.sample.core.koin

import io.wax911.sample.core.api.NetworkClient
import io.wax911.sample.core.auth.AuthenticationHelper
import io.wax911.sample.core.auth.contract.IAuthenticationHelper
import io.wax911.sample.core.dao.DatabaseHelper
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.core.util.Settings
import io.wax911.support.core.controller.SupportRequestClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModules = module {
    factory<SupportRequestClient> {
        NetworkClient()
    }

    factory {
        Settings.newInstance(
            arg = androidContext()
        )
    }

    single {
        DatabaseHelper.newInstance(
            arg = androidContext()
        )
    }

    factory<IAuthenticationHelper> {
        AuthenticationHelper(
            databaseHelper = get()
        )
    }
}

val corePresenterModules = module {
    factory {
        CorePresenter(
            androidContext(),
            settings = get()
        )
    }
}

val coreViewModelModules = module {

}

val coreRepositoryModules = module {

}