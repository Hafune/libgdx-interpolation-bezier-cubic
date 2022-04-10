package mygdx.game.lib

import com.badlogic.gdx.Gdx

object InterpolationStorage {
    private val hash = HashMap<String, BezierCubic>()

    operator fun get(path: String): BezierCubic {
        if (hash[path] == null) {
            val data = MyObjectMapper.readValue(FileStorage[path].readString(), BezierCubicData::class.java)
            hash[path] = BezierCubic(data.x0, data.y0, data.x1, data.y1)
        }
        return hash[path]!!
    }

    fun buildNew(fileName: String): BezierCubic {
        val data = MyObjectMapper.readValue(Gdx.app.files.internal(fileName).readString(), BezierCubicData::class.java)
        return BezierCubic(data.x0, data.y0, data.x1, data.y1)
    }
}