package mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisImage
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisTextField
import ktx.actors.onClick
import mygdx.game.lib.*
import java.io.BufferedWriter
import java.io.FileWriter


class SceneHandler(settings: Settings) : Table() {

    private var group = Group().also {
        it.width = 200f
        it.height = 200f
    }
    private var knobA = buildKnob(0f, 0f)
    private var knobB = buildKnob(group.width, group.height)

    private var shapeRenderer = ShapeRenderer()

    private var data = BezierCubicData()
    private val interpolation = BezierCubic(data.x0, data.y0, data.x1, data.y1)

    private val path = settings[Settings.Keys.path]
    private val exportPath = VisTextField(path ?: "${Gdx.files.localStoragePath}data.json")
    private val importButton = VisTextButton("import")

    private val fileChooserOpen: MyFileChooserOpen = MyFileChooserOpen {
        try {
            data = MyObjectMapper.readValue(it.readString(), BezierCubicData::class.java)
            exportPath.text = it.path()
        } catch (e: Exception) {
            importButton.color = Color.RED
            importButton.addAction(Actions.color(Color.WHITE, 1f))
        }
    }

    init {
        top().left()
        shapeRenderer.color = Color.GREEN;

        setFillParent(true)

        val exportButton = VisTextButton("export")
        exportButton.onClick {
            try {
                val writer = BufferedWriter(FileWriter(exportPath.text))
                writer.write(MyObjectMapper.writeValueAsString(data))
                writer.close()
                settings[Settings.Keys.path] = exportPath.text
                settings.exportSetting()
            } catch (e: Exception) {
                exportPath.color = Color.RED
                exportPath.addAction(Actions.color(Color.WHITE, 1f))
            }
        }

        importButton.onClick {
            stage.addActor(fileChooserOpen)
        }

        add(exportButton).padRight(10f).top()
        add(exportPath).growX().top()
        row()
        add(importButton).padRight(10f).padTop(10f).top()

        group.addActor(knobA)
        group.addActor(knobB)

        val verticalGroup = VerticalGroup()
        verticalGroup.addActor(group)
        verticalGroup.align(Align.center)
        add(verticalGroup).grow().center()
    }

    private fun buildKnob(x: Float, y: Float): VisImage {
        val fileName = "radio_button_active.png"
        val im = VisImage(TextureStorage[fileName])
        im.setOrigin(Align.center)
        im.x = -im.width / 2 + x
        im.y = -im.height / 2 + y
        im.userObject = Vector2(x, y)


        im.addListener(object : MyDragListener() {
            override fun drag(event: InputEvent, x: Float, y: Float, pointer: Int) {
                im.x += x
                im.y += y
            }
        })

        return im
    }

    fun render() {
        shapeRenderer.projectionMatrix = stage.camera.combined;

        drawKnobLine(knobA)
        drawKnobLine(knobB)

        writeDate()
        drawInterpolation()
    }

    private fun drawInterpolation() {
        interpolation.change(data.x0, data.y0, data.x1, data.y1)

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (t in 0..group.width.toInt()) {
            val x0 = t.toFloat()
            val y0 = interpolation.apply(x0 / group.width) * group.height
            val x1 = x0 + 1f
            val y1 = interpolation.apply(x1 / group.width) * group.height

            val v0 = group.localToStageCoordinates(Vector2(x0, y0))
            val v1 = group.localToStageCoordinates(Vector2(x1, y1))

            shapeRenderer.line(v0.x, v0.y, v1.x, v1.y);
        }
        shapeRenderer.end()
    }

    private fun writeDate() {
        data.x0 = (knobA.x + knobA.originX) / group.width
        data.y0 = (knobA.y + knobA.originY) / group.height
        data.x1 = (knobB.x + knobB.originX) / group.width
        data.y1 = (knobB.y + knobB.originY) / group.height
    }

    private fun drawKnobLine(im: VisImage) {
        val v0 = group.localToStageCoordinates(Vector2(im.userObject as Vector2))
        val v1 = group.localToStageCoordinates(Vector2(im.x + im.originX, im.y + im.originY))

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.line(v0.x, v0.y, v1.x, v1.y)
        shapeRenderer.end()
    }
}