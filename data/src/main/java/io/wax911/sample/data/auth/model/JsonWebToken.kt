package io.wax911.sample.data.auth.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JsonWebToken(
    @PrimaryKey
    var id: Long = 0,
    val access_token: String,
    val expires_in: Long,
    var expiry_time: Long = 0,
    val token_type: String,
    val scope: String,
    val refresh_token: String
) {
    fun getTokenKey() = "Bearer $access_token"
}
