package mygdx.game.lib

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle

object FileStorage {
    private val hash = HashMap<String, FileHandle>()

    operator fun get(fileName: String): FileHandle {
        if (!hash.containsKey(fileName)) {
            hash[fileName] = Gdx.app.files.internal(fileName)
        }
        return hash[fileName]!!
    }
}