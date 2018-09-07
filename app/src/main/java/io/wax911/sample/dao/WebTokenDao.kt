package io.wax911.sample.dao

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.model.WebToken
import io.wax911.support.base.dao.QueryBase

@Dao
interface WebTokenDao : QueryBase<WebToken> {
    @get:Query("select * from WebToken limit 1")
    val defaultToken: WebToken?
}
