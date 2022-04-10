package mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisImage
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisTextField
import com.kotcrab.vis.ui.widget.spinner.FloatSpinnerModel
import com.kotcrab.vis.ui.widget.spinner.Spinner
import ktx.actors.onChange
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

    private val x0 = buildKnobField()
    private val y0 = buildKnobField()
    private val x1 = buildKnobField()
    private val y1 = buildKnobField()

    private var lockFields = false
    private val x0Label = VisLabel("x0")
    private val y0Label = VisLabel("y0")
    private val x1Label = VisLabel("x1")
    private val y1Label = VisLabel("y1")

    private var shapeRenderer = ShapeRenderer()

    private var data = BezierCubicData()
    private val interpolation = BezierCubic(data.x0, data.y0, data.x1, data.y1)

    private val path = settings[Settings.Keys.path]
    private val exportPath = VisTextField(path ?: "${Gdx.files.localStoragePath}data.json")
    private val importButton = VisTextButton("import")

    private val fileChooserOpen: MyFileChooserOpen = MyFileChooserOpen {
        loadData(it)
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

        val pad = 10f
        val tableHead = Table()
        tableHead.add(exportButton).padRight(pad)
        tableHead.add(exportPath).growX()
        tableHead.row()
        tableHead.add(importButton).padRight(pad).padTop(pad)

        val positionsFields = Table()
        positionsFields.add(x0Label).padRight(pad).padLeft(pad)
        positionsFields.add(x0).growX()
        positionsFields.add(y0Label).padRight(pad).padLeft(pad)
        positionsFields.add(y0).growX()
        positionsFields.add(x1Label).padRight(pad).padLeft(pad)
        positionsFields.add(x1).growX()
        positionsFields.add(y1Label).padRight(pad).padLeft(pad)
        positionsFields.add(y1).growX()

        val resetButton = VisTextButton("reload")
        resetButton.onClick {
            if (settings[Settings.Keys.path] != null) {
                loadData(FileStorage[settings[Settings.Keys.path]!!])
            }
        }
        positionsFields.add(resetButton).padRight(pad).padLeft(pad)

        tableHead.add(positionsFields).padTop(pad).grow().row()

        add(tableHead).top().growX().row()

        group.addActor(knobA)
        group.addActor(knobB)

        add(group).grow().center()


        if (settings[Settings.Keys.path] != null) {
            loadData(FileStorage[settings[Settings.Keys.path]!!])
        } else updateFields()
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

    private fun buildKnob(x: Float, y: Float): VisImage {
        val fileName = "knob.png"
        val image = VisImage(TextureStorage[fileName])
        image.setOrigin(Align.center)
        image.x = -image.width / 2 + x
        image.y = -image.height / 2 + y
        image.userObject = Vector2(x, y)


        image.addListener(object : MyDragListener() {
            override fun drag(event: InputEvent, x: Float, y: Float, pointer: Int) {
                image.x += x
                image.y += y
                updateFields()
            }
        })
        return image
    }

    private fun buildKnobField(): Spinner {
        val model = FloatSpinnerModel("0", "-500000", "500000", "0.01", 4)
        val spinner = Spinner(name, model)
        spinner.onChange {
            if (lockFields) return@onChange
            readKnobFields()
        }
        return spinner
    }

    private fun writeDate() {
        data.x0 = (knobA.x + knobA.originX) / group.width
        data.y0 = (knobA.y + knobA.originY) / group.height
        data.x1 = (knobB.x + knobB.originX) / group.width
        data.y1 = (knobB.y + knobB.originY) / group.height
    }

    private fun readDate() {
        knobA.x = -knobA.originX + group.width * data.x0
        knobA.y = -knobA.originY + group.height * data.y0
        knobB.x = -knobB.originX + group.width * data.x1
        knobB.y = -knobB.originY + group.height * data.y1

        updateFields()
    }

    private fun readKnobFields() {
        knobA.x = -knobA.originX + group.width * (x0.model as FloatSpinnerModel).value.toFloat()
        knobA.y = -knobA.originY + group.height * (y0.model as FloatSpinnerModel).value.toFloat()
        knobB.x = -knobB.originX + group.width * (x1.model as FloatSpinnerModel).value.toFloat()
        knobB.y = -knobB.originY + group.height * (y1.model as FloatSpinnerModel).value.toFloat()
    }

    private fun updateFields() {
        lockFields = true
        (x0.model as FloatSpinnerModel).value = ((knobA.x + knobA.originX) / group.width).toBigDecimal()
        (y0.model as FloatSpinnerModel).value = ((knobA.y + knobA.originY) / group.height).toBigDecimal()
        (x1.model as FloatSpinnerModel).value = ((knobB.x + knobB.originX) / group.width).toBigDecimal()
        (y1.model as FloatSpinnerModel).value = ((knobB.y + knobB.originY) / group.height).toBigDecimal()
        lockFields = false
    }

    private fun drawKnobLine(im: VisImage) {
        val v0 = group.localToStageCoordinates(Vector2(im.userObject as Vector2))
        val v1 = group.localToStageCoordinates(Vector2(im.x + im.originX, im.y + im.originY))

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.line(v0.x, v0.y, v1.x, v1.y)
        shapeRenderer.end()
    }

    private fun loadData(file: FileHandle) {
        try {
            data = MyObjectMapper.readValue(file.readString(), BezierCubicData::class.java)
            exportPath.text = file.path()
            readDate()
        } catch (e: Exception) {
            importButton.color = Color.RED
            importButton.addAction(Actions.color(Color.WHITE, 1f))
        }
    }
}