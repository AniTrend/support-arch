package io.wax911.support.ui.view.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import io.wax911.support.extension.getCompatColor

/**
 * Circle image view.
 *
 * A widget for [SupportRefreshLayout].
 *
 */

class SupportCircleImageView : AppCompatImageView {

    var animationListener: Animation.AnimationListener? = null
    private var mShadowRadius: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, color: Int, radius: Float) : super(context) {
        val density = getContext().resources.displayMetrics.density
        val diameter = (radius * density * 2f).toInt()
        val shadowYOffset = (density * Y_OFFSET).toInt()
        val shadowXOffset = (density * X_OFFSET).toInt()

        mShadowRadius = (density * SHADOW_RADIUS).toInt()

        val circle: ShapeDrawable
        if (elevationSupported()) {
            circle = ShapeDrawable(OvalShape())
            ViewCompat.setElevation(this, SHADOW_ELEVATION * density)
        } else {
            val oval = OvalShadow(mShadowRadius, diameter)
            circle = ShapeDrawable(oval)
            setLayerType(View.LAYER_TYPE_SOFTWARE, circle.paint)
            circle.paint.setShadowLayer(
                mShadowRadius.toFloat(), shadowXOffset.toFloat(), shadowYOffset.toFloat(),
                KEY_SHADOW_COLOR
            )
            val padding = mShadowRadius
            // set padding so the inner image sits correctly within the shadow.
            setPadding(padding, padding, padding, padding)
        }
        circle.paint.color = color
        background = circle
    }

    private fun elevationSupported(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= 21
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!elevationSupported()) {
            setMeasuredDimension(measuredWidth + mShadowRadius * 2, measuredHeight + mShadowRadius * 2)
        }
    }

    public override fun onAnimationStart() {
        super.onAnimationStart()
        animationListener?.onAnimationStart(animation)
    }

    public override fun onAnimationEnd() {
        super.onAnimationEnd()
        animationListener?.onAnimationEnd(animation)
    }

    /**
     * Update the background color of the circle image view.
     *
     * @param colorRes Id of a color resource.
     */
    fun setBackgroundColorRes(colorRes: Int) {
        setBackgroundColor(context.getCompatColor(colorRes))
    }

    override fun setBackgroundColor(color: Int) {
        if (background is ShapeDrawable)
            (background as ShapeDrawable).paint.color = color
    }

    private inner class OvalShadow internal constructor(shadowRadius: Int, private val mCircleDiameter: Int) : OvalShape() {
        private val mRadialGradient by lazy(LazyThreadSafetyMode.NONE) {
            RadialGradient(
                (mCircleDiameter / 2).toFloat(), (mCircleDiameter / 2).toFloat(),
                shadowRadius.toFloat(), intArrayOf(FILL_SHADOW_COLOR, Color.TRANSPARENT), null, Shader.TileMode.CLAMP
            )
        }
        private val mShadowPaint by lazy(LazyThreadSafetyMode.NONE) {
            Paint().also {
                it.shader = mRadialGradient
            }
        }

        init { mShadowRadius = shadowRadius }

        override fun draw(canvas: Canvas, paint: Paint) {
            val viewWidth = this@SupportCircleImageView.width
            val viewHeight = this@SupportCircleImageView.height
            canvas.drawCircle(
                (viewWidth / 2).toFloat(), (viewHeight / 2).toFloat(), (mCircleDiameter / 2 + mShadowRadius).toFloat(),
                mShadowPaint
            )
            canvas.drawCircle(
                (viewWidth / 2).toFloat(),
                (viewHeight / 2).toFloat(),
                (mCircleDiameter / 2).toFloat(),
                paint
            )
        }
    }

    companion object {

        private const val KEY_SHADOW_COLOR = 0x1E000000
        private const val FILL_SHADOW_COLOR = 0x3D000000
        // PX
        private const val X_OFFSET = 0f
        private const val Y_OFFSET = 1.75f
        private const val SHADOW_RADIUS = 3.5f
        private const val SHADOW_ELEVATION = 4
    }
}
