package mygdx.game.lib

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter

class MyFileChooserOpen(callback: (file: FileHandle) -> Unit) : FileChooser(Mode.OPEN) {

    var file: FileHandle? = null

    init {
        setDirectory(Gdx.files.localStoragePath)
        this.selectionMode = SelectionMode.FILES
        setKeepWithinStage(false)

        setListener(object : FileChooserAdapter() {
            override fun selected(files: Array<FileHandle>) {
                file = files.first()
                callback(files.first()!!)
            }
        })

    }
}