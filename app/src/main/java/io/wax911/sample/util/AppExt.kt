package io.wax911.sample.util

import android.content.Context
import io.wax911.sample.dao.DatabaseHelper

/**
 * Gets the application database
 */
fun Context.getDatabase() : DatabaseHelper =
    DatabaseHelper.getInstance(this)