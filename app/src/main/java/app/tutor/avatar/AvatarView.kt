package app.tutor.avatar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sin

/**
 * Avatar desenhada 100% em código (Canvas), sem imagens e sem IA.
 * Anima: respiração, piscar, olhar suave e boca mexendo quando [talking] = true.
 */
class AvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    enum class Mood { NEUTRO, FELIZ, PENSANDO }

    var mood: Mood = Mood.FELIZ
        set(value) {
            field = value
            invalidate()
        }

    var talking: Boolean = false
        set(value) {
            field = value
            if (!value) mouth = 0f
            invalidate()
        }

    private var time = 0f
    private var lastNanos = 0L
    private var mouth = 0f
    private var blink = 0f
    private var blinkClock = -1f
    private var nextBlink = 2.5f

    private val p = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()
    private val path = Path()

    private val cSkin = Color.parseColor("#F2C7A5")
    private val cSkinDark = Color.parseColor("#E0AE8A")
    private val cHair = Color.parseColor("#3B2A24")
    private val cShirt = Color.parseColor("#2F6FED")
    private val cEye = Color.parseColor("#2B1B17")
    private val cLip = Color.parseColor("#B5485A")

    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1000
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener { step() }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lastNanos = System.nanoTime()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        animator.cancel()
        super.onDetachedFromWindow()
    }

    private fun step() {
        val now = System.nanoTime()
        var dt = (now - lastNanos) / 1_000_000_000f
        lastNanos = now
        if (dt > 0.1f) dt = 0.1f
        time += dt

        // piscar
        if (blinkClock < 0f && time >= nextBlink) blinkClock = 0f
        if (blinkClock >= 0f) {
            blinkClock += dt
            val d = 0.18f
            blink = if (blinkClock >= d) 0f else sin(Math.PI.toFloat() * blinkClock / d)
            if (blinkClock >= d) {
                blinkClock = -1f
                nextBlink = time + 2f + (Math.random().toFloat() * 3f)
            }
        }

        // boca
        mouth = if (talking) {
            val wave = abs(sin(time * 11f)) * (0.65f + 0.35f * sin(time * 3.3f))
            0.15f + 0.85f * wave
        } else {
            0f
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val s = min(width, height) / 400f
        canvas.save()
        canvas.translate(width / 2f - 200f * s, 0f)
        canvas.scale(s, s)

        val bob = sin(time * 1.6f) * 3f
        canvas.translate(0f, bob)

        // corpo
        p.style = Paint.Style.FILL
        p.color = cShirt
        rect.set(70f, 325f, 330f, 520f)
        canvas.drawRoundRect(rect, 70f, 70f, p)

        // pescoço
        p.color = cSkinDark
        rect.set(177f, 270f, 223f, 342f)
        canvas.drawRoundRect(rect, 14f, 14f, p)

        // gola
        p.color = Color.WHITE
        path.reset()
        path.moveTo(170f, 330f)
        path.lineTo(200f, 365f)
        path.lineTo(230f, 330f)
        path.close()
        canvas.drawPath(path, p)

        // cabelo (parte de trás)
        p.color = cHair
        rect.set(92f, 40f, 308f, 340f)
        canvas.drawRoundRect(rect, 100f, 110f, p)

        // rosto
        p.color = cSkin
        rect.set(110f, 70f, 290f, 300f)
        canvas.drawOval(rect, p)

        // franja
        p.color = cHair
        rect.set(104f, 52f, 296f, 196f)
        canvas.drawArc(rect, 180f, 180f, true, p)
        path.reset()
        path.moveTo(104f, 124f)
        path.quadTo(170f, 190f, 250f, 120f)
        path.quadTo(285f, 150f, 296f, 124f)
        path.lineTo(296f, 100f)
        path.lineTo(104f, 100f)
        path.close()
        canvas.drawPath(path, p)

        // bochechas
        p.color = Color.argb(70, 235, 90, 110)
        canvas.drawCircle(143f, 228f, 18f, p)
        canvas.drawCircle(257f, 228f, 18f, p)

        // olhos
        val open = 1f - blink
        val lookX = sin(time * 0.7f) * 2.5f
        drawEye(canvas, 160f, 182f, open, lookX)
        drawEye(canvas, 240f, 182f, open, lookX)

        // óculos
        p.style = Paint.Style.STROKE
        p.strokeWidth = 4f
        p.color = Color.parseColor("#3A3A46")
        canvas.drawCircle(160f, 182f, 29f, p)
        canvas.drawCircle(240f, 182f, 29f, p)
        canvas.drawLine(189f, 180f, 211f, 180f, p)

        // sobrancelhas
        p.strokeWidth = 5f
        p.strokeCap = Paint.Cap.ROUND
        p.color = cHair
        val raise = if (mood == Mood.PENSANDO) -9f else 0f
        canvas.drawLine(138f, 142f, 182f, 140f, p)
        canvas.drawLine(218f, 140f + raise, 262f, 142f + raise * 0.4f, p)

        // nariz
        p.strokeWidth = 3f
        p.color = cSkinDark
        path.reset()
        path.moveTo(198f, 205f)
        path.quadTo(194f, 224f, 204f, 226f)
        canvas.drawPath(path, p)

        drawMouth(canvas)
        p.strokeCap = Paint.Cap.BUTT
        canvas.restore()
    }

    private fun drawEye(canvas: Canvas, cx: Float, cy: Float, open: Float, lookX: Float) {
        p.style = Paint.Style.FILL
        p.color = Color.WHITE
        rect.set(cx - 18f, cy - 15f * open - 1f, cx + 18f, cy + 15f * open + 1f)
        canvas.drawOval(rect, p)
        if (open > 0.15f) {
            p.color = cEye
            rect.set(cx - 9f + lookX, cy - 9f * open, cx + 9f + lookX, cy + 9f * open)
            canvas.drawOval(rect, p)
            if (open > 0.5f) {
                p.color = Color.WHITE
                canvas.drawCircle(cx + 3f + lookX, cy - 4f * open, 2.6f, p)
            }
        }
    }

    private fun drawMouth(canvas: Canvas) {
        val cx = 200f
        val cy = 253f
        if (mouth > 0.04f) {
            val h = 4f + 17f * mouth
            val w = 20f - 4f * mouth
            p.style = Paint.Style.FILL
            p.color = Color.parseColor("#7A2233")
            rect.set(cx - w, cy - h * 0.35f, cx + w, cy + h)
            canvas.drawOval(rect, p)
            p.style = Paint.Style.STROKE
            p.strokeWidth = 3f
            p.color = cLip
            canvas.drawOval(rect, p)
        } else {
            p.style = Paint.Style.STROKE
            p.strokeWidth = 4f
            p.color = cLip
            path.reset()
            path.moveTo(cx - 22f, cy)
            when (mood) {
                Mood.FELIZ -> path.quadTo(cx, cy + 20f, cx + 22f, cy)
                Mood.PENSANDO -> path.quadTo(cx, cy + 3f, cx + 22f, cy - 5f)
                Mood.NEUTRO -> path.lineTo(cx + 22f, cy)
            }
            canvas.drawPath(path, p)
        }
    }
}
