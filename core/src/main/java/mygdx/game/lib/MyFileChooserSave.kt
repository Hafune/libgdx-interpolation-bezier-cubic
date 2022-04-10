package mygdx.game.lib

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter

class MyFileChooserSave(onSave: ((file: FileHandle) -> Unit)? = null) : FileChooser(Mode.SAVE) {

    var data = ""

    init {
        setDirectory("E:/PlatformerOneEditor/assets")
        selectionMode = SelectionMode.FILES
        setKeepWithinStage(false)
        setDefaultPrefsName("json")

        setListener(object : FileChooserAdapter() {
            override fun selected(files: Array<FileHandle>) {
                val file = files.first()
                file.writeString(data, false)
                onSave?.invoke(file)
            }
        })

    }
}