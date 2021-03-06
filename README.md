# ActionComposer

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff).

Project template included simple launchers and an `ApplicationAdapter` extension with GUI created using the [VisUI](https://github.com/kotcrab/vis-ui) library.

Important - I plan to gradually develop this project and therefore I do not guarantee backward compatibility of new versions at all. 

![image](https://user-images.githubusercontent.com/58075695/162771538-b1779166-6fc7-42c1-ab9f-2422b624fa53.png)
![image](https://user-images.githubusercontent.com/58075695/162771751-fb6c7b33-ae8f-4e30-9818-f63187568ed2.png)
![image](https://user-images.githubusercontent.com/58075695/162628434-1c0a1780-0715-4f65-9dd5-31bdec6afb64.png)

I'm writing tools for my game, at some point I realized that I was wasting a lot of time trying to pick up an interpolation.
This utility is still very raw, but it already does what I wrote it for, namely generates a json file.
You can read this file in your game in any convenient way and use it with com.badlogic.gdx.math.Bezier.cubic
Or you can take my implementation and use it.

```
package mygdx.game.lib

import com.badlogic.gdx.math.Bezier
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2

class BezierCubic(x0: Float, y0: Float, x1: Float, y1: Float) : Interpolation() {

    private val v = Vector2()
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
```

![image](https://user-images.githubusercontent.com/58075695/162629646-caf94e28-9e02-420d-b2e6-532d07398727.png)

If you have never used liftoff, here is a screenshot of the program launch
![image](https://user-images.githubusercontent.com/58075695/162629212-2ff46cf3-477d-4b11-a12c-da2209ca627c.png)


You can use my code as you wish.