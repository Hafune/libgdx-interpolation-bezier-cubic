# ActionComposer

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff).

Project template included simple launchers and an `ApplicationAdapter` extension with GUI created using the [VisUI](https://github.com/kotcrab/vis-ui) library.

## Gradle

This project uses [Gradle](http://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

![image](https://user-images.githubusercontent.com/58075695/162628290-01bffedb-914c-4116-9009-65c1ea2133ab.png)
![image](https://user-images.githubusercontent.com/58075695/162628318-1049c4b4-6b37-4217-ac6e-f8bf61506ee8.png)
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

![image](https://user-images.githubusercontent.com/58075695/162628784-2b31e91e-551c-4bde-bbae-e95e8b22ad5d.png)

If you have never used liftoff, here is a screenshot of the program launch
![image](https://user-images.githubusercontent.com/58075695/162629212-2ff46cf3-477d-4b11-a12c-da2209ca627c.png)


You can use my code as you wish.