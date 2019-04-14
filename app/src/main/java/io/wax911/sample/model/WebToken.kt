package io.wax911.sample.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WebToken(
        @PrimaryKey var id: Long = 0,
        val access_token: String,
        val expires_in: Long,
        var expiry_time: Long = 0,
        val token_type: String,
        val scope: String,
        val refresh_token: String) : Cloneable {

    fun calculateExpires() {
        expiry_time = (System.currentTimeMillis() / 1000L) + ((expires_in - 1500))
    }

    fun hasExpired(): Boolean {
        val currentSystemTime = (System.currentTimeMillis() / 1000L)
        return expiry_time < currentSystemTime
    }

    fun getHeader() = "$token_type $access_token"

    override fun clone(): WebToken {
        super.clone()
        return this
    }
}
