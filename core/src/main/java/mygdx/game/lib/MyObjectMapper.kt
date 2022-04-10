@file:Suppress("UNCHECKED_CAST")

package mygdx.game.lib

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

object MyObjectMapper : ObjectMapper() {

    init {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        enable(SerializationFeature.INDENT_OUTPUT)
    }

    fun buildMap(o: Any): HashMap<String, Any> {
        return readValue(writeValueAsString(o), HashMap::class.java) as HashMap<String, Any>
    }

    fun updateByMap(o: Any, map: Map<String, Any>) {
        readerForUpdating(o).readValue<Any>(writeValueAsString(map))
    }
}