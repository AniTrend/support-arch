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

package co.anitrend.arch.theme.animator.contract

import android.animation.Animator
import android.view.View
import android.view.animation.Interpolator

/**
 * Contract recycler animator
 *
 * @since v0.9.X
 */
abstract class AbstractAnimator {

    open val animationDuration: SupportAnimatorDuration =
        SupportAnimatorDuration.SHORT

    abstract val interpolator: Interpolator

    abstract fun getAnimators(view: View): Array<Animator>
}
