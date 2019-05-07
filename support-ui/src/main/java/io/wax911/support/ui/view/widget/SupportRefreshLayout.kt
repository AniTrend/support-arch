package io.wax911.support.ui.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation
import android.widget.AbsListView

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.MotionEventCompat
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Created by max on 2017/12/05.
 * Both way swipe refresh layout.
 *
 * This is a more powerful [SwipeRefreshLayout], it can swipe
 * to refresh and load.
 *
 * @author wangdaye MySplash
 */

class SupportRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewGroup(context, attrs), NestedScrollingParent, NestedScrollingChild {

    private var mTarget: View? = null // the target of the gesture
    private var mListener: OnRefreshAndLoadListener? = null
    private var mRefreshing = false
    private var mLoading = false
    private var mRefreshEnabled = true
    private var mLoadEnabled = true
    private val mTouchSlop: Int
    private val mDragTriggerDistances = floatArrayOf(-1f, -1f)

    // If nested scrolling is enabled, the total amount that needed to be
    // consumed by this as the nested scrolling parent is used in place of the
    // overscroll determined by MOVE events in the onTouch handler
    private var mTotalUnconsumed: Float = 0.toFloat()
    private val mNestedScrollingParentHelper: NestedScrollingParentHelper
    private val mNestedScrollingChildHelper: NestedScrollingChildHelper
    private val mParentScrollConsumed = IntArray(2)
    private val mParentOffsetInWindow = IntArray(2)
    private var mNestedScrollInProgress: Boolean = false

    private val mMediumAnimationDuration: Int
    private var mDragOffsetDistance: Int = 0
    // Whether or not the starting offset has been determined.
    private var mOriginalOffsetCalculated = false

    private var mInitialDownY: Float = 0.toFloat()
    private var mIsBeingDragged: Boolean = false
    // Whether this item is scaled up rather than clipped.
    private val mScale = false

    // Target is returning to its start offset because it was cancelled or a
    // refresh was triggered.
    private var mReturningToStart: Boolean = false
    private val mDecelerateInterpolator: DecelerateInterpolator

    private var mCircleViews: Array<SupportCircleImageView>? = null

    protected var mFrom: Int = 0

    private var mStartingScale: Float = 0.toFloat()

    private var mProgress: Array<SupportProgressDrawable>? = null

    private var mScaleAnimation: Animation? = null

    private var mScaleDownAnimation: Animation? = null

    private var mAlphaStartAnimation: Animation? = null

    private var mAlphaMaxAnimation: Animation? = null

    private var mScaleDownToStartAnimation: Animation? = null

    private var mNotify: Boolean = false

    private val mCircleWidth: Int
    private val mCircleHeight: Int

    /**
     * @return Whether the BothWaySwipeRefreshWidget is actively showing refresh
     * progress.
     */
    // state.

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    // scale and show
    /* notify */ var isRefreshing: Boolean
        get() = mRefreshing
        set(refreshing) {
            if (refreshing && (mRefreshing || mLoading)) {
                return
            }
            if (refreshing) {
                mCircleViews!![DIRECTION_BOTTOM].visibility = View.GONE
                mRefreshing = true
                setTargetOffsetTopAndBottom(
                    DIRECTION_TOP,
                    (mDragTriggerDistances[DIRECTION_TOP] - mDragOffsetDistance).toInt()
                )
                mNotify = false
                startScaleUpAnimation(DIRECTION_TOP, mRefreshListener)
            } else {
                setRefreshing(false, false)
            }
        }

    /**
     * @return Whether the BothWaySwipeRefreshWidget is actively showing load
     * progress.
     */
    /**
     * Notify the widget that load state has changed. Do not call this when
     * load is triggered by a swipe gesture.
     *
     * @param loading Whether or not the view should show load progress.
     */
    // scale and show
    /* notify */ var isLoading: Boolean
        get() = mLoading
        set(loading) {
            if (loading && (mRefreshing || mLoading)) {
                return
            }
            if (loading) {
                mCircleViews!![DIRECTION_TOP].visibility = View.GONE
                mLoading = true
                setTargetOffsetTopAndBottom(
                    DIRECTION_BOTTOM,
                    (-mDragTriggerDistances[DIRECTION_BOTTOM] - mDragOffsetDistance).toInt()
                )
                mNotify = false
                startScaleUpAnimation(DIRECTION_BOTTOM, mLoadListener)
            } else {
                setLoading(false, false)
            }
        }

    private val mAnimateToTopCorrectPosition = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            setTargetOffsetTopAndBottom(
                DIRECTION_TOP,
                (mFrom + (mDragTriggerDistances[DIRECTION_TOP] - mFrom) * interpolatedTime - mDragOffsetDistance).toInt()
            )
            mProgress!![DIRECTION_TOP].setArrowScale(1 - interpolatedTime)
        }
    }

    private val mAnimateToBottomCorrectPosition = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            setTargetOffsetTopAndBottom(
                DIRECTION_BOTTOM,
                (mFrom + (-mDragTriggerDistances[DIRECTION_BOTTOM] - mFrom) * interpolatedTime - mDragOffsetDistance).toInt()
            )
            mProgress!![DIRECTION_BOTTOM].setArrowScale(1 - interpolatedTime)
        }
    }

    private val mAnimateToTopStartPosition = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            moveToStart(DIRECTION_TOP, interpolatedTime)
        }
    }

    private val mAnimateToBottomStartPosition = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            moveToStart(DIRECTION_BOTTOM, interpolatedTime)
        }
    }

    // animation listener.

    private val mRefreshListener = object : Animation.AnimationListener {

        override fun onAnimationStart(animation: Animation) {}

        override fun onAnimationRepeat(animation: Animation) {}

        override fun onAnimationEnd(animation: Animation) {
            if (mRefreshing) {
                // Make sure the progress view is fully visible
                mProgress!![DIRECTION_TOP].alpha = MAX_ALPHA
                mProgress!![DIRECTION_TOP].start()
                if (mNotify) {
                    if (mListener != null) {
                        mListener!!.onRefresh()
                    }
                }
                mDragOffsetDistance = mDragTriggerDistances[DIRECTION_TOP].toInt()
            } else {
                reset()
            }
        }
    }

    private val mLoadListener = object : Animation.AnimationListener {

        override fun onAnimationStart(animation: Animation) {}

        override fun onAnimationRepeat(animation: Animation) {}

        override fun onAnimationEnd(animation: Animation) {
            if (mLoading) {
                // Make sure the progress view is fully visible
                mProgress!![DIRECTION_BOTTOM].alpha = MAX_ALPHA
                mProgress!![DIRECTION_BOTTOM].start()
                if (mNotify) {
                    if (mListener != null) {
                        mListener!!.onLoad()
                    }
                }
                mDragOffsetDistance = (-mDragTriggerDistances[DIRECTION_BOTTOM]).toInt()
            } else {
                reset()
            }
        }
    }

    init {

        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

        mMediumAnimationDuration = resources.getInteger(android.R.integer.config_mediumAnimTime)

        setWillNotDraw(false)
        mDecelerateInterpolator = DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR)

        val metrics = resources.displayMetrics
        mCircleWidth = (CIRCLE_DIAMETER * metrics.density).toInt()
        mCircleHeight = (CIRCLE_DIAMETER * metrics.density).toInt()
        mDragTriggerDistances[DIRECTION_TOP] = DEFAULT_CIRCLE_TARGET * metrics.density
        mDragTriggerDistances[DIRECTION_BOTTOM] = DEFAULT_CIRCLE_TARGET * metrics.density

        createProgressView()
        isChildrenDrawingOrderEnabled = true

        mNestedScrollingParentHelper = NestedScrollingParentHelper(this)
        mNestedScrollingChildHelper = NestedScrollingChildHelper(this)
        mNestedScrollingChildHelper.isNestedScrollingEnabled = true
        isNestedScrollingEnabled = true

        val a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS)
        isEnabled = a.getBoolean(0, true)
        a.recycle()
    }

    private fun createProgressView() {
        this.mCircleViews = arrayOf(
            SupportCircleImageView(context, CIRCLE_BG_LIGHT, CIRCLE_DIAMETER / 2f),
            SupportCircleImageView(context, CIRCLE_BG_LIGHT, CIRCLE_DIAMETER / 2f)
        )
        this.mProgress = arrayOf(SupportProgressDrawable(context, this), SupportProgressDrawable(context, this))

        for (i in 0..1) {
            mProgress!![i].setBackgroundColor(CIRCLE_BG_LIGHT)
            mCircleViews!![i].setImageDrawable(mProgress!![i])
            mCircleViews!![i].visibility = View.GONE
            addView(mCircleViews!![i])
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        reset()
    }

    // control.

    private fun ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child != mCircleViews!![0] && child != mCircleViews!![1]) {
                    mTarget = child
                    break
                }
            }
        }
    }

    fun setDragTriggerDistance(dir: Int, distance: Int) {
        var distance = distance
        if (dir == DIRECTION_BOTTOM) {
            distance += mCircleHeight
        }
        mDragTriggerDistances[dir] = distance.toFloat()
    }

    // draw.

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mTarget == null) {
            ensureTarget()
        }
        if (mTarget != null) {
            mTarget!!.measure(
                View.MeasureSpec.makeMeasureSpec(
                    measuredWidth - paddingLeft - paddingRight,
                    View.MeasureSpec.EXACTLY
                ), View.MeasureSpec.makeMeasureSpec(
                    measuredHeight - paddingTop - paddingBottom,
                    View.MeasureSpec.EXACTLY
                )
            )
        }

        for (i in 0..1) {
            mCircleViews!![i].measure(
                View.MeasureSpec.makeMeasureSpec(mCircleWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(mCircleHeight, View.MeasureSpec.EXACTLY)
            )
        }
        if (!mOriginalOffsetCalculated) {
            mOriginalOffsetCalculated = true
            mDragOffsetDistance = 0
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = measuredWidth
        val height = measuredHeight
        if (childCount == 0) {
            return
        }
        if (mTarget == null) {
            ensureTarget()
        }
        if (mTarget != null) {
            val childLeft = paddingLeft
            val childTop = paddingTop
            val childWidth = width - paddingLeft - paddingRight
            val childHeight = height - paddingTop - paddingBottom
            mTarget!!.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
        }

        if (mDragOffsetDistance == 0) {
            mCircleViews!![0].layout(
                width / 2 - mCircleWidth / 2,
                -mCircleHeight,
                width / 2 + mCircleWidth / 2,
                0
            )
            mCircleViews!![1].layout(
                width / 2 - mCircleWidth / 2,
                measuredHeight,
                width / 2 + mCircleWidth / 2,
                measuredHeight + mCircleHeight
            )
        } else if (mDragOffsetDistance > 0) {
            mCircleViews!![0].layout(
                width / 2 - mCircleWidth / 2,
                mDragOffsetDistance - mCircleHeight,
                width / 2 + mCircleWidth / 2,
                mDragOffsetDistance
            )
            mCircleViews!![1].layout(
                width / 2 - mCircleWidth / 2,
                measuredHeight,
                width / 2 + mCircleWidth / 2,
                measuredHeight + mCircleHeight
            )
        } else if (mDragOffsetDistance < 0) {
            mCircleViews!![0].layout(
                width / 2 - mCircleWidth / 2,
                -mCircleHeight,
                width / 2 + mCircleWidth / 2,
                0
            )
            mCircleViews!![1].layout(
                width / 2 - mCircleWidth / 2,
                measuredHeight + mDragOffsetDistance,
                width / 2 + mCircleWidth / 2,
                measuredHeight + mCircleHeight + mDragOffsetDistance
            )
        }
    }

    // touch

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        ensureTarget()

        val action = ev.actionMasked

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false
        }

        if (!isEnabled || mReturningToStart || mNestedScrollInProgress || mRefreshing || mLoading) {
            return false
        }

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                val oldOffset = mDragOffsetDistance
                for (i in 0..1) {
                    setTargetOffsetTopAndBottom(i, -oldOffset)
                }

                mIsBeingDragged = false
                val initialDownY = ev.y
                if (initialDownY == -1f) {
                    return false
                }
                mInitialDownY = initialDownY
            }

            MotionEvent.ACTION_MOVE -> {
                val yDiff = ev.y - mInitialDownY
                if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollUp() && mRefreshEnabled) {
                    mIsBeingDragged = true
                    mProgress!![DIRECTION_TOP].alpha = STARTING_PROGRESS_ALPHA
                } else if (yDiff < -mTouchSlop && !mIsBeingDragged && !canChildScrollDown() && mLoadEnabled) {
                    mIsBeingDragged = true
                    mProgress!![DIRECTION_BOTTOM].alpha = STARTING_PROGRESS_ALPHA
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mIsBeingDragged = false
        }

        return mIsBeingDragged
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.actionMasked

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false
        }

        if (!isEnabled || mReturningToStart
            || mNestedScrollInProgress || mRefreshing || mLoading
        ) {
            return false
        }

        when (action) {
            MotionEvent.ACTION_DOWN -> mIsBeingDragged = false

            MotionEvent.ACTION_MOVE -> {
                val y = ev.y
                val offset = (y - mInitialDownY) * DRAG_RATE
                if (mIsBeingDragged) {
                    if (offset > 0 && !canChildScrollUp()) {
                        moveSpinner(DIRECTION_TOP, offset)
                    } else if (offset < 0 && !canChildScrollDown()) {
                        moveSpinner(DIRECTION_BOTTOM, offset)
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                val y = ev.y
                val offset = (y - mInitialDownY) * DRAG_RATE
                mIsBeingDragged = false
                if (offset > 0 && !canChildScrollUp()) {
                    finishSpinner(DIRECTION_TOP, offset)
                } else if (offset < 0 && !canChildScrollDown()) {
                    finishSpinner(DIRECTION_BOTTOM, offset)
                }
                return false
            }
            MotionEvent.ACTION_CANCEL -> return false
        }

        return true
    }

    private fun moveSpinner(dir: Int, dragDistance: Float) {
        mProgress!![dir].showArrow(true)
        val originalDragPercent = Math.abs(dragDistance) / mDragTriggerDistances[dir]

        val dragPercent = Math.min(1f, Math.abs(originalDragPercent))
        val adjustedPercent = Math.max(dragPercent - .4, 0.0).toFloat() * 5 / 3
        val extraOS = Math.abs(dragDistance) - mDragTriggerDistances[dir]
        val tensionSlingshotPercent = Math.max(
            0f,
            Math.min(extraOS, mDragTriggerDistances[dir] * 2) / mDragTriggerDistances[dir]
        )
        val tensionPercent =
            (tensionSlingshotPercent / 4 - Math.pow((tensionSlingshotPercent / 4).toDouble(), 2.0)).toFloat() * 2f
        val extraMove = mDragTriggerDistances[dir] * tensionPercent * 2f

        val offset =
            (mDragTriggerDistances[dir] * dragPercent + extraMove).toInt() * if (dir == DIRECTION_TOP) 1 else -1

        // where 1.0f is a full circle
        if (mCircleViews!![dir].visibility != View.VISIBLE) {
            mCircleViews!![dir].visibility = View.VISIBLE
        }

        if (!mScale) {
            mCircleViews!![dir].scaleX = 1f
            mCircleViews!![dir].scaleY = 1f
        }

        if (mScale) {
            setAnimationProgress(
                dir,
                Math.min(Math.abs(dragDistance / mDragTriggerDistances[dir]), 1f)
            )
        }
        if (Math.abs(dragDistance) < mDragTriggerDistances[dir]) {
            if (mProgress!![dir].alpha > STARTING_PROGRESS_ALPHA && !isAnimationRunning(mAlphaStartAnimation)) {
                // Animate the alpha
                startProgressAlphaStartAnimation(dir)
            }
        } else {
            if (mProgress!![dir].alpha < MAX_ALPHA && !isAnimationRunning(mAlphaMaxAnimation)) {
                // Animate the alpha
                startProgressAlphaMaxAnimation(dir)
            }
        }
        val strokeStart = adjustedPercent * .8f
        mProgress!![dir].setStartEndTrim(0f, Math.min(MAX_PROGRESS_ANGLE, strokeStart))
        mProgress!![dir].setArrowScale(Math.min(1f, adjustedPercent))

        val rotation = (-0.25f + .4f * adjustedPercent + tensionPercent * 2) * .5f
        mProgress!![dir].setProgressRotation(rotation)
        setTargetOffsetTopAndBottom(dir, offset - mDragOffsetDistance)
    }

    private fun finishSpinner(dir: Int, dragDistance: Float) {
        if (Math.abs(dragDistance) > mDragTriggerDistances[dir]) {
            if (dir == DIRECTION_TOP) {
                setRefreshing(true, true /* notify */)
            } else {
                setLoading(true, true)
            }
        } else {
            // cancel refresh
            if (dir == DIRECTION_TOP) {
                mRefreshing = false
            } else {
                mLoading = false
            }
            mProgress!![dir].setStartEndTrim(0f, 0f)
            var listener: Animation.AnimationListener? = null
            if (!mScale) {
                listener = object : Animation.AnimationListener {

                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {
                        if (!mScale) {
                            startScaleDownAnimation(dir, null)
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation) {}

                }
            }
            animateOffsetToStartPosition(dir, mDragOffsetDistance, listener)
            mProgress!![dir].showArrow(false)
        }
    }

    private fun isAnimationRunning(animation: Animation?): Boolean {
        return animation != null && animation.hasStarted() && !animation.hasEnded()
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    fun canChildScrollUp(): Boolean {
        return mTarget!!.canScrollVertically(-1)
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll down. Override this if the child view is a custom view.
     */
    fun canChildScrollDown(): Boolean {
        return mTarget!!.canScrollVertically(1)
    }

    override fun requestDisallowInterceptTouchEvent(b: Boolean) {
        if (android.os.Build.VERSION.SDK_INT < 21 && mTarget is AbsListView || mTarget != null && !ViewCompat.isNestedScrollingEnabled(
                mTarget!!
            )
        ) {
            // do nothing.
        } else {
            super.requestDisallowInterceptTouchEvent(b)
        }
    }

    private fun setRefreshing(refreshing: Boolean, notify: Boolean) {
        if (refreshing && (mRefreshing || mLoading)) {
            return
        }
        if (mRefreshing != refreshing) {
            mNotify = notify
            ensureTarget()
            mRefreshing = refreshing
            if (mRefreshing) {
                animateOffsetToCorrectPosition(DIRECTION_TOP, mDragOffsetDistance, mRefreshListener)
            } else {
                startScaleDownAnimation(DIRECTION_TOP, mRefreshListener)
            }
        }
    }

    private fun setLoading(loading: Boolean, notify: Boolean) {
        if (loading && (mRefreshing || mLoading)) {
            return
        }
        if (mLoading != loading) {
            mNotify = notify
            ensureTarget()
            mLoading = loading
            if (mLoading) {
                animateOffsetToCorrectPosition(DIRECTION_BOTTOM, mDragOffsetDistance, mLoadListener)
            } else {
                startScaleDownAnimation(DIRECTION_BOTTOM, mLoadListener)
            }
        }
    }

    /**
     * Set the BothWaySwipeRefreshLayoutWidget is allowed to refresh.
     *
     * @param enabled Whether it is allowed to refresh.
     */
    fun setRefreshEnabled(enabled: Boolean) {
        mRefreshEnabled = enabled
    }

    /**
     * Set the BothWaySwipeRefreshLayoutWidget is allowed to load.
     *
     * @param enabled Whether it is allowed to load.
     */
    fun setLoadEnabled(enabled: Boolean) {
        mLoadEnabled = enabled
    }

    // position.

    private fun moveToStart(dir: Int, interpolatedTime: Float) {
        val offset = (mFrom * (1 - interpolatedTime)).toInt()
        setTargetOffsetTopAndBottom(dir, offset - mDragOffsetDistance)
    }

    private fun setTargetOffsetTopAndBottom(dir: Int, offset: Int) {
        mCircleViews!![dir].bringToFront()
        mCircleViews!![dir].offsetTopAndBottom(offset)
        mDragOffsetDistance += offset
    }

    private fun reset() {
        val oldOffset = mDragOffsetDistance
        for (i in 0..1) {
            mCircleViews!![i].clearAnimation()
            mProgress!![i].stop()
            mCircleViews!![i].visibility = View.GONE
            setColorViewAlpha(i, MAX_ALPHA)
            // Return the circle to its start position
            if (mScale) {
                setAnimationProgress(i, 0f /* animation complete and view is hidden */)
            } else {
                setTargetOffsetTopAndBottom(i, -oldOffset)
            }
        }
        mDragOffsetDistance = 0
    }

    // color.

    private fun setColorViewAlpha(dir: Int, targetAlpha: Int) {
        mCircleViews!![dir].background.alpha = targetAlpha
        mProgress!![dir].alpha = targetAlpha
    }

    /**
     * Set the background color of the progress spinner disc.
     *
     * @param colorRes Resource id of the color.
     */
    fun setProgressBackgroundColorSchemeResource(@ColorRes colorRes: Int) {
        setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context, colorRes))
    }

    /**
     * Set the background color of the progress spinner disc.
     *
     * @param color Color.
     */
    fun setProgressBackgroundColorSchemeColor(@ColorInt color: Int) {
        for (i in 0..1) {
            mCircleViews!![i].setBackgroundColor(color)
            mProgress!![i].setBackgroundColor(color)
        }
    }

    /**
     * Set the color resources used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     *
     * @param colorResIds Colors.
     */
    fun setColorSchemeResources(@ColorRes vararg colorResIds: Int) {
        val colorRes = IntArray(colorResIds.size)
        for (i in colorResIds.indices) {
            colorRes[i] = ContextCompat.getColor(context, colorResIds[i])
        }
        setColorSchemeColors(*colorRes)
    }

    /**
     * Set the colors used in the progress animation. The first
     * color will also be the color of the bar that grows in response to a user
     * swipe gesture.
     *
     * @param colors Color.
     */
    @SuppressLint("SupportAnnotationUsage")
    @ColorInt
    fun setColorSchemeColors(vararg colors: Int) {
        ensureTarget()
        for (i in 0..1) {
            mProgress!![i].setColorSchemeColors(*colors)
        }
    }

    // animation.

    // to correct position.

    private fun animateOffsetToCorrectPosition(dir: Int, from: Int, listener: Animation.AnimationListener?) {
        mFrom = from
        if (dir == DIRECTION_TOP) {
            mAnimateToTopCorrectPosition.reset()
            mAnimateToTopCorrectPosition.duration = ANIMATE_TO_TRIGGER_DURATION.toLong()
            mAnimateToTopCorrectPosition.interpolator = mDecelerateInterpolator
        } else {
            mAnimateToBottomCorrectPosition.reset()
            mAnimateToBottomCorrectPosition.duration = ANIMATE_TO_TRIGGER_DURATION.toLong()
            mAnimateToBottomCorrectPosition.interpolator = mDecelerateInterpolator
        }
        if (listener != null) {
            mCircleViews!![dir].animationListener = listener
        }
        mCircleViews!![dir].clearAnimation()
        mCircleViews!![dir].startAnimation(
            if (dir == DIRECTION_TOP)
                mAnimateToTopCorrectPosition
            else
                mAnimateToBottomCorrectPosition
        )
    }

    // to start position.

    private fun animateOffsetToStartPosition(dir: Int, from: Int, listener: Animation.AnimationListener?) {
        if (mScale) {
            // Scale the item back down
            if (dir == DIRECTION_TOP) {
                startScaleDownReturnToTopStartAnimation(from, listener)
            } else {
                startScaleDownReturnToBottomStartAnimation(from, listener)
            }
        } else {
            mFrom = from
            if (dir == DIRECTION_TOP) {
                mAnimateToTopStartPosition.reset()
                mAnimateToTopStartPosition.duration = ANIMATE_TO_START_DURATION.toLong()
                mAnimateToTopStartPosition.interpolator = mDecelerateInterpolator
            } else {
                mAnimateToBottomStartPosition.reset()
                mAnimateToBottomStartPosition.duration = ANIMATE_TO_START_DURATION.toLong()
                mAnimateToBottomStartPosition.interpolator = mDecelerateInterpolator
            }
            if (listener != null) {
                mCircleViews!![dir].animationListener = listener
            }
            mCircleViews!![dir].clearAnimation()
            mCircleViews!![dir].startAnimation(
                if (dir == DIRECTION_TOP) mAnimateToTopStartPosition else mAnimateToBottomStartPosition
            )
        }
    }

    private fun startScaleDownReturnToTopStartAnimation(
        from: Int,
        listener: Animation.AnimationListener?
    ) {
        mFrom = from
        mStartingScale = mCircleViews!![DIRECTION_TOP].scaleX
        mScaleDownToStartAnimation = object : Animation() {
            public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val targetScale = mStartingScale + -mStartingScale * interpolatedTime
                setAnimationProgress(DIRECTION_TOP, targetScale)
                moveToStart(DIRECTION_TOP, interpolatedTime)
            }
        }
        mScaleDownToStartAnimation!!.duration = SCALE_DOWN_DURATION.toLong()
        if (listener != null) {
            mCircleViews!![DIRECTION_TOP].animationListener = listener
        }
        mCircleViews!![DIRECTION_TOP].clearAnimation()
        mCircleViews!![DIRECTION_TOP].startAnimation(mScaleDownToStartAnimation)
    }

    private fun startScaleDownReturnToBottomStartAnimation(
        from: Int,
        listener: Animation.AnimationListener?
    ) {
        mFrom = from
        mStartingScale = mCircleViews!![DIRECTION_BOTTOM].scaleX
        mScaleDownToStartAnimation = object : Animation() {
            public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val targetScale = mStartingScale + -mStartingScale * interpolatedTime
                setAnimationProgress(DIRECTION_BOTTOM, targetScale)
                moveToStart(DIRECTION_BOTTOM, interpolatedTime)
            }
        }
        mScaleDownToStartAnimation!!.duration = SCALE_DOWN_DURATION.toLong()
        if (listener != null) {
            mCircleViews!![DIRECTION_BOTTOM].animationListener = listener
        }
        mCircleViews!![DIRECTION_BOTTOM].clearAnimation()
        mCircleViews!![DIRECTION_BOTTOM].startAnimation(mScaleDownToStartAnimation)
    }

    private fun startScaleUpAnimation(dir: Int, listener: Animation.AnimationListener?) {
        mCircleViews!![dir].visibility = View.VISIBLE
        // Pre API 11, alpha is used in place of scale up to show the
        // progress circle appearing.
        // Don't adjust the alpha during appearance otherwise.
        mProgress!![dir].alpha = MAX_ALPHA
        mScaleAnimation = object : Animation() {
            public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                setAnimationProgress(dir, interpolatedTime)
            }
        }
        mScaleAnimation!!.duration = mMediumAnimationDuration.toLong()
        if (listener != null) {
            mCircleViews!![dir].animationListener = listener
        }
        mCircleViews!![dir].clearAnimation()
        mCircleViews!![dir].startAnimation(mScaleAnimation)
    }

    private fun setAnimationProgress(dir: Int, progress: Float) {
        mCircleViews!![dir].scaleX = progress
        mCircleViews!![dir].scaleY = progress
    }

    private fun startScaleDownAnimation(dir: Int, listener: Animation.AnimationListener?) {
        mScaleDownAnimation = object : Animation() {
            public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                setAnimationProgress(dir, 1 - interpolatedTime)
            }
        }
        mScaleDownAnimation!!.duration = SCALE_DOWN_DURATION.toLong()
        mCircleViews!![dir].animationListener = listener
        mCircleViews!![dir].clearAnimation()
        mCircleViews!![dir].startAnimation(mScaleDownAnimation)
    }

    private fun startProgressAlphaStartAnimation(dir: Int) {
        mAlphaStartAnimation = startAlphaAnimation(dir, mProgress!![dir].alpha, STARTING_PROGRESS_ALPHA)
    }

    private fun startProgressAlphaMaxAnimation(dir: Int) {
        mAlphaMaxAnimation = startAlphaAnimation(dir, mProgress!![dir].alpha, MAX_ALPHA)
    }

    private fun startAlphaAnimation(dir: Int, startingAlpha: Int, endingAlpha: Int): Animation {
        val alpha = object : Animation() {
            public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                mProgress!![dir].alpha = (startingAlpha + (endingAlpha - startingAlpha) * interpolatedTime).toInt()
            }
        }
        alpha.duration = ALPHA_ANIMATION_DURATION.toLong()
        // Clear out the previous animation listeners.
        mCircleViews!![dir].animationListener = null
        mCircleViews!![dir].clearAnimation()
        mCircleViews!![dir].startAnimation(alpha)
        return alpha
    }

    // listener.

    // on refresh and load listener.

    interface OnRefreshAndLoadListener {
        fun onRefresh()
        fun onLoad()
    }

    fun setOnRefreshAndLoadListener(listener: OnRefreshAndLoadListener) {
        mListener = listener
    }

    // nested scroll parent.

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return (isEnabled
                && !mReturningToStart && !mRefreshing && !mLoading
                && (mRefreshEnabled || mLoadEnabled)
                && nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes)
        // Dispatch up to the nested parent
        startNestedScroll(axes and ViewCompat.SCROLL_AXIS_VERTICAL)
        mTotalUnconsumed = 0f
        mNestedScrollInProgress = true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = mTotalUnconsumed.toInt()
                mTotalUnconsumed = 0f
            } else {
                mTotalUnconsumed -= dy.toFloat()
                consumed[1] = dy
            }
            moveSpinner(DIRECTION_TOP, mTotalUnconsumed)
        } else if (dy < 0 && mTotalUnconsumed < 0) {
            if (dy < mTotalUnconsumed) {
                consumed[1] = mTotalUnconsumed.toInt()
                mTotalUnconsumed = 0f
            } else {
                mTotalUnconsumed -= dy.toFloat()
                consumed[1] = dy
            }
            moveSpinner(DIRECTION_BOTTOM, mTotalUnconsumed)
        }

        // Now let our nested parent consume the leftovers
        val parentConsumed = mParentScrollConsumed
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0]
            consumed[1] += parentConsumed[1]
        }
    }

    override fun getNestedScrollAxes(): Int {
        return mNestedScrollingParentHelper.nestedScrollAxes
    }

    override fun onNestedScroll(
        target: View, dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int
    ) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(
            dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
            mParentOffsetInWindow
        )

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        val dy = dyUnconsumed + mParentOffsetInWindow[1]
        if (dy < 0 && !canChildScrollUp() && !mRefreshing && mRefreshEnabled) {
            mTotalUnconsumed -= dy.toFloat()
            moveSpinner(DIRECTION_TOP, mTotalUnconsumed)
        } else if (dy > 0 && !canChildScrollDown() && !mLoading && mLoadEnabled) {
            mTotalUnconsumed -= dy.toFloat()
            moveSpinner(DIRECTION_BOTTOM, mTotalUnconsumed)
        }
    }

    override fun onNestedPreFling(
        target: View, velocityX: Float,
        velocityY: Float
    ): Boolean {
        return dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun onNestedFling(
        target: View, velocityX: Float, velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun onStopNestedScroll(target: View) {
        mNestedScrollingParentHelper.onStopNestedScroll(target)
        mNestedScrollInProgress = false
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            finishSpinner(DIRECTION_TOP, mTotalUnconsumed)
        } else if (mTotalUnconsumed < 0) {
            finishSpinner(DIRECTION_BOTTOM, mTotalUnconsumed)
        }
        mTotalUnconsumed = 0f
        // Dispatch up our nested parent
        stopNestedScroll()
    }

    // nested scrolling child.

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mNestedScrollingChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mNestedScrollingChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mNestedScrollingChildHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int, offsetInWindow: IntArray?
    ): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedScroll(
            dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed, offsetInWindow
        )
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
            dx, dy, consumed, offsetInWindow
        )
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    companion object {

        // direction
        val DIRECTION_TOP = 0
        val DIRECTION_BOTTOM = 1

        private val MAX_ALPHA = 255
        private val STARTING_PROGRESS_ALPHA = (.3f * MAX_ALPHA).toInt()

        private val CIRCLE_DIAMETER = 40

        private val DECELERATE_INTERPOLATION_FACTOR = 2f
        private val DRAG_RATE = .5f

        // Max amount of circle that can be filled by progress during swipe gesture,
        // where 1.0 is a full circle
        private val MAX_PROGRESS_ANGLE = .8f

        private val SCALE_DOWN_DURATION = 150

        private val ALPHA_ANIMATION_DURATION = 300

        private val ANIMATE_TO_TRIGGER_DURATION = 200

        private val ANIMATE_TO_START_DURATION = 200

        // Default background for the progress spinner
        private val CIRCLE_BG_LIGHT = -0x50506
        // Default offset in dips from the top of the view to where the progress spinner should stop
        private val DEFAULT_CIRCLE_TARGET = 64
        private val LAYOUT_ATTRS = intArrayOf(android.R.attr.enabled)
    }
}