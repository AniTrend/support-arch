package io.wax911.support.ui.view.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.ArrayList

import androidx.annotation.IntDef
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

/**
 * Material progress drawable.
 *
 * A widget for [SupportRefreshLayout].
 */

class SupportProgressDrawable(context: Context, private val mParent: View) : Drawable(), Animatable {
    /** The list of animators operating on this drawable.  */
    private val mAnimators = ArrayList<Animation>()

    /** The indicator ring, used to manage animation state.  */
    private val mRing: Ring

    /** Canvas rotation in degrees.  */
    private var rotation: Float = 0.toFloat()
        internal set(rotation) {
            field = rotation
            invalidateSelf()
        }

    private val mResources: Resources
    private var mAnimation: Animation? = null
    private var mRotationCount: Float = 0.toFloat()
    private var mWidth: Double = 0.toDouble()
    private var mHeight: Double = 0.toDouble()
    private var mFinishing: Boolean = false

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(LARGE, DEFAULT)
    internal annotation class ProgressDrawableSize

    init {
        mResources = context.resources

        val mCallback = object : Drawable.Callback {
            override fun invalidateDrawable(d: Drawable) {
                invalidateSelf()
            }

            override fun scheduleDrawable(d: Drawable, what: Runnable, `when`: Long) {
                scheduleSelf(what, `when`)
            }

            override fun unscheduleDrawable(d: Drawable, what: Runnable) {
                unscheduleSelf(what)
            }
        }
        mRing = Ring(mCallback)
        val COLORS = intArrayOf(Color.BLACK)
        mRing.setColors(COLORS)

        updateSizes(DEFAULT)
        setupAnimators()
    }

    private fun setSizeParameters(
        progressCircleWidth: Double, progressCircleHeight: Double,
        centerRadius: Double, strokeWidth: Double, arrowWidth: Float, arrowHeight: Float
    ) {
        val ring = mRing
        val metrics = mResources.displayMetrics
        val screenDensity = metrics.density

        mWidth = progressCircleWidth * screenDensity
        mHeight = progressCircleHeight * screenDensity
        ring.strokeWidth = strokeWidth.toFloat() * screenDensity
        ring.centerRadius = centerRadius * screenDensity
        ring.setColorIndex(0)
        ring.setArrowDimensions(arrowWidth * screenDensity, arrowHeight * screenDensity)
        ring.setInsets(mWidth.toInt(), mHeight.toInt())
    }

    /**
     * Set the overall size for the progress spinner. This updates the radius
     * and stroke width of the ring.
     *
     * @param size One of [SupportProgressDrawable.LARGE] or
     * [SupportProgressDrawable.DEFAULT]
     */
    private fun updateSizes(@ProgressDrawableSize size: Int) {
        if (size == LARGE) {
            setSizeParameters(
                CIRCLE_DIAMETER_LARGE.toDouble(), CIRCLE_DIAMETER_LARGE.toDouble(), CENTER_RADIUS_LARGE.toDouble(),
                STROKE_WIDTH_LARGE.toDouble(), ARROW_WIDTH_LARGE.toFloat(), ARROW_HEIGHT_LARGE.toFloat()
            )
        } else {
            setSizeParameters(
                CIRCLE_DIAMETER.toDouble(),
                CIRCLE_DIAMETER.toDouble(),
                CENTER_RADIUS.toDouble(),
                STROKE_WIDTH.toDouble(),
                ARROW_WIDTH.toFloat(),
                ARROW_HEIGHT.toFloat()
            )
        }
    }

    /**
     * @param show Set to true to display the arrowhead on the progress spinner.
     */
    fun showArrow(show: Boolean) {
        mRing.setShowArrow(show)
    }

    /**
     * @param scale Set the scale of the arrowhead for the spinner.
     */
    fun setArrowScale(scale: Float) {
        mRing.setArrowScale(scale)
    }

    /**
     * Set the start and end trim for the progress spinner arc.
     *
     * @param startAngle start angle
     * @param endAngle end angle
     */
    fun setStartEndTrim(startAngle: Float, endAngle: Float) {
        mRing.startTrim = startAngle
        mRing.endTrim = endAngle
    }

    /**
     * Set the amount of rotation to apply to the progress spinner.
     *
     * @param rotation Rotation is from [0..1]
     */
    fun setProgressRotation(rotation: Float) {
        mRing.rotation = rotation
    }

    /**
     * Update the background color of the circle image view.
     */
    fun setBackgroundColor(color: Int) {
        mRing.setBackgroundColor(color)
    }

    /**
     * Set the colors used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     *
     * @param colors
     */
    fun setColorSchemeColors(vararg colors: Int) {
        mRing.setColors(colors)
        mRing.setColorIndex(0)
    }

    override fun getIntrinsicHeight(): Int {
        return mHeight.toInt()
    }

    override fun getIntrinsicWidth(): Int {
        return mWidth.toInt()
    }

    override fun draw(c: Canvas) {
        val bounds = bounds
        val saveCount = c.save()
        c.rotate(rotation, bounds.exactCenterX(), bounds.exactCenterY())
        mRing.draw(c, bounds)
        c.restoreToCount(saveCount)
    }

    override fun setAlpha(alpha: Int) {
        mRing.alpha = alpha
    }

    override fun getAlpha(): Int {
        return mRing.alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mRing.setColorFilter(colorFilter)
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun isRunning(): Boolean {
        val animators = mAnimators
        val N = animators.size
        for (i in 0 until N) {
            val animator = animators[i]
            if (animator.hasStarted() && !animator.hasEnded()) {
                return true
            }
        }
        return false
    }

    override fun start() {
        mAnimation!!.reset()
        mRing.storeOriginals()
        // Already showing some part of the ring
        if (mRing.endTrim != mRing.startTrim) {
            mFinishing = true
            mAnimation!!.duration = (ANIMATION_DURATION / 2).toLong()
            mParent.startAnimation(mAnimation)
        } else {
            mRing.setColorIndex(0)
            mRing.resetOriginals()
            mAnimation!!.duration = ANIMATION_DURATION.toLong()
            mParent.startAnimation(mAnimation)
        }
    }

    override fun stop() {
        mParent.clearAnimation()
        rotation = 0
        mRing.setShowArrow(false)
        mRing.setColorIndex(0)
        mRing.resetOriginals()
    }

    private fun getMinProgressArc(ring: Ring): Float {
        return Math.toRadians(
            ring.strokeWidth / (2.0 * Math.PI * ring.centerRadius)
        ).toFloat()
    }

    // Adapted from ArgbEvaluator.java
    private fun evaluateColorChange(fraction: Float, startValue: Int, endValue: Int): Int {
        val startA = startValue shr 24 and 0xff
        val startR = startValue shr 16 and 0xff
        val startG = startValue shr 8 and 0xff
        val startB = startValue and 0xff

        val endA = endValue shr 24 and 0xff
        val endR = endValue shr 16 and 0xff
        val endG = endValue shr 8 and 0xff
        val endB = endValue and 0xff

        return startA + (fraction * (endA - startA)).toInt() shl 24 or (
                startR + (fraction * (endR - startR)).toInt() shl 16) or (
                startG + (fraction * (endG - startG)).toInt() shl 8) or
                startB + (fraction * (endB - startB)).toInt()
    }

    /**
     * Update the ring color if this is within the last 25% of the animation.
     * The new ring color will be a translation from the starting ring color to
     * the next color.
     */
    private fun updateRingColor(interpolatedTime: Float, ring: Ring) {
        if (interpolatedTime > COLOR_START_DELAY_OFFSET) {
            // scale the interpolatedTime so that the full
            // transformation from 0 - 1 takes place in the
            // remaining time
            ring.setColor(
                evaluateColorChange(
                    (interpolatedTime - COLOR_START_DELAY_OFFSET) / (1.0f - COLOR_START_DELAY_OFFSET),
                    ring.startingColor,
                    ring.nextColor
                )
            )
        }
    }

    private fun applyFinishTranslation(interpolatedTime: Float, ring: Ring) {
        // shrink back down and complete a full rotation before
        // starting other circles
        // Rotation goes between [0..1].
        updateRingColor(interpolatedTime, ring)
        val targetRotation = (Math.floor((ring.startingRotation / MAX_PROGRESS_ARC).toDouble()) + 1f).toFloat()
        val minProgressArc = getMinProgressArc(ring)
        val startTrim =
            ring.startingStartTrim + (ring.startingEndTrim - minProgressArc - ring.startingStartTrim) * interpolatedTime
        ring.startTrim = startTrim
        ring.endTrim = ring.startingEndTrim
        val rotation = ring.startingRotation + (targetRotation - ring.startingRotation) * interpolatedTime
        ring.rotation = rotation
    }

    private fun setupAnimators() {
        val ring = mRing
        val animation = object : Animation() {
            public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (mFinishing) {
                    applyFinishTranslation(interpolatedTime, ring)
                } else {
                    // The minProgressArc is calculated from 0 to create an
                    // angle that matches the stroke width.
                    val minProgressArc = getMinProgressArc(ring)
                    val startingEndTrim = ring.startingEndTrim
                    val startingTrim = ring.startingStartTrim
                    val startingRotation = ring.startingRotation

                    updateRingColor(interpolatedTime, ring)

                    // Moving the start trim only occurs in the first 50% of a
                    // single ring animation
                    if (interpolatedTime <= START_TRIM_DURATION_OFFSET) {
                        // scale the interpolatedTime so that the full
                        // transformation from 0 - 1 takes place in the
                        // remaining time
                        val scaledTime = interpolatedTime / (1.0f - START_TRIM_DURATION_OFFSET)
                        val startTrim = startingTrim + (MAX_PROGRESS_ARC - minProgressArc) * MATERIAL_INTERPOLATOR
                            .getInterpolation(scaledTime)
                        ring.startTrim = startTrim
                    }

                    // Moving the end trim starts after 50% of a single ring
                    // animation completes
                    if (interpolatedTime > END_TRIM_START_DELAY_OFFSET) {
                        // scale the interpolatedTime so that the full
                        // transformation from 0 - 1 takes place in the
                        // remaining time
                        val minArc = MAX_PROGRESS_ARC - minProgressArc
                        val scaledTime =
                            (interpolatedTime - START_TRIM_DURATION_OFFSET) / (1.0f - START_TRIM_DURATION_OFFSET)
                        val endTrim = startingEndTrim + minArc * MATERIAL_INTERPOLATOR.getInterpolation(scaledTime)
                        ring.endTrim = endTrim
                    }

                    var rotation = startingRotation + 0.25f * interpolatedTime
                    ring.rotation = rotation

                    val groupRotation =
                        FULL_ROTATION / NUM_POINTS * interpolatedTime + FULL_ROTATION * (mRotationCount / NUM_POINTS)
                    rotation = groupRotation
                }
            }
        }
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.RESTART
        animation.interpolator = LINEAR_INTERPOLATOR
        animation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {
                mRotationCount = 0f
            }

            override fun onAnimationEnd(animation: Animation) {
                // do nothing
            }

            override fun onAnimationRepeat(animation: Animation) {
                ring.storeOriginals()
                ring.goToNextColor()
                ring.startTrim = ring.endTrim
                if (mFinishing) {
                    // finished closing the last ring from the swipe gesture; go
                    // into progress mode
                    mFinishing = false
                    animation.duration = ANIMATION_DURATION.toLong()
                    ring.setShowArrow(false)
                } else {
                    mRotationCount = (mRotationCount + 1) % NUM_POINTS
                }
            }
        })
        mAnimation = animation
    }

    private class Ring internal constructor(private val mCallback: Drawable.Callback) {
        private val mTempBounds = RectF()
        private val mPaint = Paint()
        private val mArrowPaint = Paint()

        internal var startTrim = 0.0f
            set(startTrim) {
                field = startTrim
                invalidateSelf()
            }
        internal var endTrim = 0.0f
            set(endTrim) {
                field = endTrim
                invalidateSelf()
            }
        var rotation = 0.0f
            set(rotation) {
                field = rotation
                invalidateSelf()
            }
        /**
         * @param strokeWidth Set the stroke width of the progress spinner in pixels.
         */
        internal var strokeWidth = 5.0f
            set(strokeWidth) {
                field = strokeWidth
                mPaint.strokeWidth = strokeWidth
                invalidateSelf()
            }
        var insets = 2.5f
            private set

        private var mColors: IntArray? = null
        // mColorIndex represents the offset into the available mColors that the
        // progress circle should currently display. As the progress circle is
        // animating, the mColorIndex moves by one to the next available color.
        private var mColorIndex: Int = 0
        internal var startingStartTrim: Float = 0.toFloat()
            private set
        internal var startingEndTrim: Float = 0.toFloat()
            private set
        /**
         * @return The amount the progress spinner is currently rotated, between [0..1].
         */
        internal var startingRotation: Float = 0.toFloat()
            private set
        private var mShowArrow: Boolean = false
        private var mArrow: Path? = null
        private var mArrowScale: Float = 0.toFloat()
        /**
         * @param centerRadius Inner radius in px of the circle the progress
         * spinner arc traces.
         */
        internal var centerRadius: Double = 0.toDouble()
        private var mArrowWidth: Int = 0
        private var mArrowHeight: Int = 0
        /**
         * @return Current alpha of the progress spinner and arrowhead.
         */
        /**
         * @param alpha Set the alpha of the progress spinner and associated arrowhead.
         */
        internal var alpha: Int = 0
        private val mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private var mBackgroundColor: Int = 0
        private var mCurrentColor: Int = 0

        /**
         * @return int describing the next color the progress spinner should use when drawing.
         */
        internal val nextColor: Int
            get() = mColors!![nextColorIndex]

        private val nextColorIndex: Int
            get() = (mColorIndex + 1) % mColors!!.size

        internal val startingColor: Int
            get() = mColors!![mColorIndex]

        init {

            mPaint.strokeCap = Paint.Cap.SQUARE
            mPaint.isAntiAlias = true
            mPaint.style = Paint.Style.STROKE

            mArrowPaint.style = Paint.Style.FILL
            mArrowPaint.isAntiAlias = true
        }

        internal fun setBackgroundColor(color: Int) {
            mBackgroundColor = color
        }

        /**
         * Set the dimensions of the arrowhead.
         *
         * @param width Width of the hypotenuse of the arrow head
         * @param height Height of the arrow point
         */
        internal fun setArrowDimensions(width: Float, height: Float) {
            mArrowWidth = width.toInt()
            mArrowHeight = height.toInt()
        }

        /**
         * Draw the progress spinner
         */
        internal fun draw(c: Canvas, bounds: Rect) {
            val arcBounds = mTempBounds
            arcBounds.set(bounds)
            arcBounds.inset(insets, insets)

            val startAngle = (startTrim + rotation) * 360
            val endAngle = (endTrim + rotation) * 360
            val sweepAngle = endAngle - startAngle

            mPaint.color = mCurrentColor
            c.drawArc(arcBounds, startAngle, sweepAngle, false, mPaint)

            drawTriangle(c, startAngle, sweepAngle, bounds)

            if (alpha < 255) {
                mCirclePaint.color = mBackgroundColor
                mCirclePaint.alpha = 255 - alpha
                c.drawCircle(
                    bounds.exactCenterX(), bounds.exactCenterY(), (bounds.width() / 2).toFloat(),
                    mCirclePaint
                )
            }
        }

        private fun drawTriangle(c: Canvas, startAngle: Float, sweepAngle: Float, bounds: Rect) {
            if (mShowArrow) {
                if (mArrow == null) {
                    mArrow = Path()
                    mArrow!!.fillType = Path.FillType.EVEN_ODD
                } else {
                    mArrow!!.reset()
                }

                // Adjust the position of the triangle so that it is inset as
                // much as the arc, but also centered on the arc.
                val inset = insets.toInt() / 2 * mArrowScale
                val x = (centerRadius * Math.cos(0.0) + bounds.exactCenterX()).toFloat()
                val y = (centerRadius * Math.sin(0.0) + bounds.exactCenterY()).toFloat()

                // Update the path each time. This works around an issue in SKIA
                // where concatenating a rotation matrix to a scale matrix
                // ignored a starting negative rotation. This appears to have
                // been fixed as of API 21.
                mArrow!!.moveTo(0f, 0f)
                mArrow!!.lineTo(mArrowWidth * mArrowScale, 0f)
                mArrow!!.lineTo(mArrowWidth * mArrowScale / 2, mArrowHeight * mArrowScale)
                mArrow!!.offset(x - inset, y)
                mArrow!!.close()
                // draw a triangle
                mArrowPaint.color = mCurrentColor
                c.rotate(
                    startAngle + sweepAngle - ARROW_OFFSET_ANGLE, bounds.exactCenterX(),
                    bounds.exactCenterY()
                )
                c.drawPath(mArrow!!, mArrowPaint)
            }
        }

        /**
         * Set the colors the progress spinner alternates between.
         *
         * @param colors Array of integers describing the colors. Must be non-`null`.
         */
        internal fun setColors(colors: IntArray) {
            mColors = colors
            // if colors are reset, snackBar sure to reset the color index as well
            setColorIndex(0)
        }

        /**
         * Set the absolute color of the progress spinner. This is should only
         * be used when animating between current and next color when the
         * spinner is rotating.
         *
         * @param color int describing the color.
         */
        internal fun setColor(color: Int) {
            mCurrentColor = color
        }

        /**
         * @param index Index into the color array of the color to display in
         * the progress spinner.
         */
        internal fun setColorIndex(index: Int) {
            mColorIndex = index
            mCurrentColor = mColors!![mColorIndex]
        }

        /**
         * Proceed to the next available ring color. This will automatically
         * wrap back to the beginning of colors.
         */
        internal fun goToNextColor() {
            setColorIndex(nextColorIndex)
        }

        internal fun setColorFilter(filter: ColorFilter?) {
            mPaint.colorFilter = filter
            invalidateSelf()
        }

        internal fun setInsets(width: Int, height: Int) {
            val minEdge = Math.min(width, height).toFloat()
            val insets: Float
            if (centerRadius <= 0 || minEdge < 0) {
                insets = Math.ceil((strokeWidth / 2.0f).toDouble()).toFloat()
            } else {
                insets = (minEdge / 2.0f - centerRadius).toFloat()
            }
            this.insets = insets
        }

        /**
         * @param show Set to true to show the arrow head on the progress spinner.
         */
        internal fun setShowArrow(show: Boolean) {
            if (mShowArrow != show) {
                mShowArrow = show
                invalidateSelf()
            }
        }

        /**
         * @param scale Set the scale of the arrowhead for the spinner.
         */
        internal fun setArrowScale(scale: Float) {
            if (scale != mArrowScale) {
                mArrowScale = scale
                invalidateSelf()
            }
        }

        /**
         * If the start / end trim are offset to begin with, store them so that
         * animation starts from that offset.
         */
        internal fun storeOriginals() {
            startingStartTrim = startTrim
            startingEndTrim = endTrim
            startingRotation = rotation
        }

        /**
         * Reset the progress spinner to default rotation, start and end angles.
         */
        internal fun resetOriginals() {
            startingStartTrim = 0f
            startingEndTrim = 0f
            startingRotation = 0f
            startTrim = 0
            endTrim = 0
            rotation = 0
        }

        private fun invalidateSelf() {
            mCallback.invalidateDrawable(null!!)
        }
    }

    companion object {

        private val LINEAR_INTERPOLATOR = LinearInterpolator()
        private val MATERIAL_INTERPOLATOR = FastOutSlowInInterpolator()

        private val FULL_ROTATION = 1080.0f
        // Maps to ProgressBar.Large style
        private val LARGE = 0
        // Maps to ProgressBar default style
        private val DEFAULT = 1

        // Maps to ProgressBar default style
        private val CIRCLE_DIAMETER = 40
        private val CENTER_RADIUS = 8.75f //should add up to 10 when + stroke_width
        private val STROKE_WIDTH = 2.5f

        // Maps to ProgressBar.Large style
        private val CIRCLE_DIAMETER_LARGE = 56
        private val CENTER_RADIUS_LARGE = 12.5f
        private val STROKE_WIDTH_LARGE = 3f

        /**
         * The value in the linear interpolator for animating the drawable at which
         * the color transition should start
         */
        private val COLOR_START_DELAY_OFFSET = 0.75f
        private val END_TRIM_START_DELAY_OFFSET = 0.5f
        private val START_TRIM_DURATION_OFFSET = 0.5f

        /** The duration of a single progress spin in milliseconds.  */
        private val ANIMATION_DURATION = 1332

        /** The number of points in the progress "star".  */
        private val NUM_POINTS = 5f

        /** Layout info for the arrowhead in dp  */
        private val ARROW_WIDTH = 10
        private val ARROW_HEIGHT = 5
        private val ARROW_OFFSET_ANGLE = 5f

        /** Layout info for the arrowhead for the large spinner in dp  */
        private val ARROW_WIDTH_LARGE = 12
        private val ARROW_HEIGHT_LARGE = 6
        private val MAX_PROGRESS_ARC = .8f
    }
}
