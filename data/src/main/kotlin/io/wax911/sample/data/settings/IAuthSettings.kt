package io.wax911.sample.data.settings

interface IAuthSettings {

    var authenticatedUserId: Long

    companion object  {
        const val INVALID_USER_ID: Long = -1
    }
}