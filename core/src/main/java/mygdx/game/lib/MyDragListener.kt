package mygdx.game.lib

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

open class MyDragListener : ClickListener() {

    override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
        val offsetX = x - touchDownX
        val offsetY = y - touchDownY

        if (event.isStopped) return
        drag(event, offsetX, offsetY, pointer)
        event.stop()
    }

    open fun drag(event: InputEvent, x: Float, y: Float, pointer: Int) {}
}