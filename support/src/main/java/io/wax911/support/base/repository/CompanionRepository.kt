package io.wax911.support.base.repository

import androidx.room.RoomDatabase
import io.wax911.support.base.dao.SupportRepository

interface CompanionRepository<R: SupportRepository<*, *>> {

    /**
     * Returns the repository that should be used by the view model. A cast of the database may
     * be required inorder to get the internal dao objects
     * <br/>
     *
     * @param database application database instance to use
     */
    fun newInstance(database: RoomDatabase) : R
}