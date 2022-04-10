package mygdx.game.lib

import com.badlogic.gdx.graphics.Texture

object TextureStorage {
    private val textureMap = HashMap<String, Texture>()

    operator fun get(path: String): Texture {
        if (textureMap[path] == null) {
            textureMap[path] = Texture(FileStorage[path])
        }
        return textureMap[path]!!
    }
}