package mygdx.game.lib

import com.badlogic.gdx.math.Bezier
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2

class BezierCubic(x0: Float, y0: Float, x1: Float, y1: Float) : Interpolation() {

    val v = Vector2()
    private val vTemp = Vector2()
    private val p0 = Vector2()
    private val p1 = Vector2(x0, y0)
    private val p2 = Vector2(x1, y1)
    private val p3 = Vector2(1f, 1f)

    override fun apply(a: Float): Float {
        return Bezier.cubic(v, a, p0, p1, p2, p3, vTemp).y
    }

    fun change(x0: Float, y0: Float, x1: Float, y1: Float) {
        p1.x = x0
        p1.y = y0
        p2.x = x1
        p2.y = y1
    }
}