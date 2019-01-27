package io.wax911.sample.repository

import android.content.Context
import android.os.Bundle
import androidx.room.RoomDatabase
import io.wax911.sample.api.NetworkClient
import io.wax911.sample.dao.DatabaseHelper
import io.wax911.sample.model.BaseModel
import io.wax911.support.repository.SupportRepository
import io.wax911.support.repository.CompanionRepository
import io.wax911.support.util.SupportStateKeyUtil
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class BaseRepository private constructor(): SupportRepository<Long, BaseModel?>() {

    /**
     * Requires the network client to be created in the implementing repo,
     * to access the created client please use:
     * @see networkClient
     */
    override fun initNetworkClient() = NetworkClient()

    /**
     * Creates the network client for implementing class using the given parameters
     *
     * @param bundle bundle of parameters for the request
     */
    override fun createNetworkClientRequestAsync(bundle: Bundle, context: Context): Deferred<Unit> = GlobalScope.async {
        when (bundle.getString(SupportStateKeyUtil.arg_bundle)) {

        }
    }

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database using the given parameters
     * <br/>
     *
     * @param bundle bundle of parameters for the request
     */
    override fun requestFromCacheAsync(bundle: Bundle, context: Context) = GlobalScope.async {
        when (bundle.getString(SupportStateKeyUtil.arg_bundle)) {

        }
    }

    companion object : CompanionRepository<BaseRepository>{

        /**
         * Returns the repository that should be used by the view model.
         * <br/>
         *
         * @param database application database instance to use
         */
        override fun newInstance(database: RoomDatabase): BaseRepository {
            val databaseHelper = database as DatabaseHelper
            return BaseRepository().also { repo ->
                repo.modelDao = databaseHelper.baseModelDao()
            }
        }
    }
}
