package io.wax911.sample.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BaseModel(
        @PrimaryKey
        val id: Long,
        val name: String)