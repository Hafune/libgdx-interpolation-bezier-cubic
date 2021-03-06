package mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.VisUI.SkinScale

class Main : ApplicationAdapter() {
    private lateinit var stage: Stage
    private lateinit var sceneHandler: SceneHandler
    private lateinit var settings: Settings

    override fun create() {
        VisUI.setSkipGdxVersionCheck(true)
        VisUI.load(SkinScale.X1)

        stage = Stage(ScreenViewport())
        Gdx.input.inputProcessor = stage

        settings = Settings()

        if (settings[Settings.Keys.appWidth] == null || settings[Settings.Keys.appHeight] == null) {
            settings[Settings.Keys.appWidth] = Gdx.graphics.width.toString()
            settings[Settings.Keys.appHeight] = Gdx.graphics.height.toString()
            settings.exportSetting()
        }

        try {
            Gdx.graphics.setWindowedMode(
                settings[Settings.Keys.appWidth]!!.toInt(),
                settings[Settings.Keys.appHeight]!!.toInt()
            )
        } catch (_: Exception) {
        }

        sceneHandler = SceneHandler(settings)
        stage.addActor(sceneHandler)
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(Gdx.graphics.deltaTime.coerceAtMost(1 / 30f))

        sceneHandler.render()
        stage.draw()
    }

    override fun dispose() {
        VisUI.dispose()
        stage.dispose()
        if (settings[Settings.Keys.appWidth] != Gdx.graphics.width.toString() ||
            settings[Settings.Keys.appHeight] != Gdx.graphics.height.toString()
        ) {
            settings[Settings.Keys.appWidth] = Gdx.graphics.width.toString()
            settings[Settings.Keys.appHeight] = Gdx.graphics.height.toString()
            settings.exportSetting()
        }
    }
}