package cn.afeibaili.jump.desktop.world

import cn.afeibaili.gl.input.Key
import cn.afeibaili.gl.input.MouseButton
import cn.afeibaili.gl.util.Time
import cn.afeibaili.jump.common.block.Block
import cn.afeibaili.jump.common.block.Blocks
import cn.afeibaili.jump.common.identifier
import cn.afeibaili.jump.common.item.BlockItem
import cn.afeibaili.jump.common.world.World
import cn.afeibaili.jump.desktop.Application
import cn.afeibaili.jump.desktop.entity.Player
import cn.afeibaili.jump.desktop.input.KeySet
import cn.afeibaili.jump.desktop.input.MouseButtonSet
import cn.afeibaili.jump.desktop.item.ItemBar
import cn.afeibaili.jump.desktop.item.ItemGrid
import cn.afeibaili.jump.desktop.logic.TickHandler
import org.lwjgl.glfw.GLFW
import kotlin.math.floor


/**
 * # 地图修改器
 *
 * @author AfeiBaili
 * @version 2026/8/27 20:23
 */

class WorldEditor(val world: World) : TickHandler {
    var currentCursorBlock: Block? = null
    val currentPlayerX get() = Player.self.x
    val currentPlayerY get() = Player.self.y
    val currentCursorX get() = Application.windowSystem.cursorX
    val currentCursorY get() = Application.windowSystem.cursorY
    val aspect get() = Application.windowSystem.aspect
    val width get() = Application.screenWidth
    val height get() = Application.screenHeight
    val zoom get() = Application.camera.zoom
    var blockPositionX = 0
    var blockPositionY = 0
    var maxLayerIndex = world.layers.size
    var currentLayerIndex = 0
    var itemBar: ItemBar = ItemBar()
    var placeInterval = 50
    var accumulator = 0L
    var lastPlaceMillis = Time.millis()

    // input /////
    val mouseButtonSet = MouseButtonSet("mouse" identifier "world.editor")
    val layerKeySet = KeySet("world.editor.layer")
    val itemBarKeySet = KeySet("world.editor.item.bar")

    init {
        TickHandler.addHandler(this)
        loadInput()
        mouseButtonSet.on()
        layerKeySet.on()
        itemBarKeySet.on()
    }

    override fun tick() {
        blockPositionX = getCurrentBlockX()
        blockPositionY = getCurrentBlockY()
        currentCursorBlock = world.getBlockAt(0, blockPositionX, blockPositionY)
    }

    fun switchNextLayer() {
        if (currentLayerIndex < maxLayerIndex - 1) currentLayerIndex++
        else currentLayerIndex = 0
    }

    fun switchPreviousLayer() {
        if (currentLayerIndex <= 0) currentLayerIndex = world.layers.size - 1
        else currentLayerIndex--
    }

    fun placeBlockByButton() {
        val grid: ItemGrid = itemBar.getGrid()
        if (grid.item !is BlockItem && grid.item == null) return
        val blockItem: BlockItem = grid.item as BlockItem
        world.setBlockAt(currentLayerIndex, getCurrentBlockX(), getCurrentBlockY(), blockItem.blockItemType.blockType)
    }

    fun breakBlockByButton() {
        world.setBlockAt(currentLayerIndex, getCurrentBlockX(), getCurrentBlockY(), Blocks.AIR.blockType)
    }

    fun getCurrentBlockX() = floor(currentPlayerX + (currentCursorX.toFloat() / width) * zoom * aspect).toInt()
    fun getCurrentBlockY() = floor(currentPlayerY + ((height - currentCursorY.toFloat()) / height) * zoom).toInt()

    fun loadInput() {
        mouseButtonSet.bind(MouseButton("place.block", GLFW.GLFW_MOUSE_BUTTON_2)) {
            if (buttonPressed()) {
                val currentMillis = Time.millis()
                val delta = currentMillis - lastPlaceMillis
                lastPlaceMillis = currentMillis
                accumulator += delta
                while (accumulator > placeInterval) {
                    accumulator -= placeInterval
                    placeBlockByButton()
                }
            }
        }
        mouseButtonSet.bind(MouseButton("break.block", GLFW.GLFW_MOUSE_BUTTON_1)) {
            if (buttonPressed()) breakBlockByButton()
        }
        layerKeySet.bind(Key("switch.last.layer", GLFW.GLFW_KEY_MINUS)) {
            released { switchPreviousLayer() }
        }
        layerKeySet.bind(Key("switch.next.layer", GLFW.GLFW_KEY_EQUAL)) {
            released { switchNextLayer() }
        }
        itemBarKeySet.bind(Key("item.bar.1", GLFW.GLFW_KEY_1)) {
            pressed { itemBar.switchGird(0) }
        }
        itemBarKeySet.bind(Key("item.bar.2", GLFW.GLFW_KEY_2)) {
            pressed { itemBar.switchGird(1) }
        }
        itemBarKeySet.bind(Key("item.bar.3", GLFW.GLFW_KEY_3)) {
            pressed { itemBar.switchGird(2) }
        }
        itemBarKeySet.bind(Key("item.bar.4", GLFW.GLFW_KEY_4)) {
            pressed { itemBar.switchGird(3) }
        }
        itemBarKeySet.bind(Key("item.bar.5", GLFW.GLFW_KEY_5)) {
            pressed { itemBar.switchGird(4) }
        }
        itemBarKeySet.bind(Key("item.bar.6", GLFW.GLFW_KEY_6)) {
            pressed { itemBar.switchGird(5) }
        }
        itemBarKeySet.bind(Key("item.bar.7", GLFW.GLFW_KEY_7)) {
            pressed { itemBar.switchGird(6) }
        }
        itemBarKeySet.bind(Key("item.bar.8", GLFW.GLFW_KEY_8)) {
            pressed { itemBar.switchGird(7) }
        }
        itemBarKeySet.bind(Key("item.bar.9", GLFW.GLFW_KEY_9)) {
            pressed { itemBar.switchGird(8) }
        }
        itemBarKeySet.bind(Key("item.bar.10", GLFW.GLFW_KEY_0)) {
            pressed { itemBar.switchGird(9) }
        }
    }
}
