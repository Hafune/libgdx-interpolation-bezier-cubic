package mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.fasterxml.jackson.core.type.TypeReference
import mygdx.game.lib.FileStorage
import mygdx.game.lib.MyObjectMapper


class Settings {

    enum class Keys {
        path,
        appWidth,
        appHeight
    }

    private val fileName = "settings.properties"
    private var settings = HashMap<String, String>()

    operator fun get(key: Keys) = settings[key.name]
    operator fun set(key: Keys, value: String) {
        settings[key.name] = value
    }

    init {
        loadSettings(initializeFile())
    }

    private fun initializeFile(): FileHandle {
        val file = FileStorage[fileName]
        if (!file.exists()) {
            exportSetting()
        }
        return file
    }

    private fun loadSettings(file: FileHandle) {
        val typeRef = object : TypeReference<HashMap<String, String>>() {}
        settings = MyObjectMapper.readValue(file.file(), typeRef)
    }

    fun exportSetting() = Gdx.files.local(fileName).writeString(MyObjectMapper.writeValueAsString(settings), false)
}