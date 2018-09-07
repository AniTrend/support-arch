package io.wax911.sample.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WebToken(
        @PrimaryKey val id: Long,
        val refresh_token: String,
        val access_token: String,
        val token_type: String,
        val expires_in: Long,
        val expires: Long) : Cloneable {

    fun getHeader() : String = "$token_type $access_token"

    override fun clone(): WebToken {
        super.clone()
        return this
    }
}
