/**
 * Copyright 2021 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.anitrend.arch.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.ui.common.ILifecycleController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * Core implementation for [androidx.appcompat.app.AppCompatActivity] components
 *
 * @since v0.9.X
 *
 * @see ISupportCoroutine
 * @see ISupportFragment
 */
abstract class SupportActivity :
    AppCompatActivity(),
    ILifecycleController,
    CoroutineScope by MainScope() {

    /** Current fragment in view tag which will be used by [getSupportFragmentManager] */
    protected var currentFragmentTag: String? = null

    /**
     * Can be used to configure custom theme styling as desired
     */
    protected abstract fun configureActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        configureActivity()
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initializeComponents(savedInstanceState)
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
