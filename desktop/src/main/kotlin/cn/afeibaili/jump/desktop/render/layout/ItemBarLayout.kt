package cn.afeibaili.jump.desktop.render.layout

import cn.afeibaili.gl.image.atlas.Image
import cn.afeibaili.gl.render.Color
import cn.afeibaili.gl.render.layout.adapt.columnAdapt
import cn.afeibaili.gl.render.layout.align.AlignmentLayout
import cn.afeibaili.gl.render.layout.align.AlignmentSetting
import cn.afeibaili.gl.render.layout.align.AlignmentType
import cn.afeibaili.gl.render.layout.align.block
import cn.afeibaili.gl.render.layout.image.Icon
import cn.afeibaili.gl.render.layout.image.IconUpdater
import cn.afeibaili.gl.render.layout.image.icon
import cn.afeibaili.gl.render.layout.shape.border.BorderComponent
import cn.afeibaili.gl.render.layout.shape.border.border
import cn.afeibaili.jump.desktop.Application
import cn.afeibaili.jump.desktop.item.ItemBar
import cn.afeibaili.jump.desktop.render.texture.TextureManager


/**
 * # 物品栏UI
 *
 * @author AfeiBaili
 * @version 2026/9/27 00:36
 */
class ItemBarLayout {
    lateinit var itemBarLayout: AlignmentLayout
    val itemBar: ItemBar get() = Application.worldEditor.itemBar
    val blockImages = TextureManager.blockImageList
    val iconUpdater = IconUpdater()
    val iconSize = 50f
    lateinit var selectedItem: BorderComponent

    fun load() {
        layout()
    }

    private fun layout() = Application.screen.layout {
        itemBarLayout = block(setting = { it.maxSize() }) {
            columnAdapt(setting = { it: AlignmentSetting -> it.align(AlignmentType.LEFT_BOTTOM) }) {

                for (index in 0 until itemBar.bar.size) {
                    val grid = itemBar.bar[index]
                    val image: Image = if (grid.item?.id == null) blockImages.getImage("air")
                    else blockImages.getImage(grid.item!!.id)
                    icon(index.toString(), image, iconUpdater) { it.size(iconSize) }
                }
                selectedItem = border(5f, Color.WHITE.setAlpha(0.9f)) { it.size(iconSize) }
            }
        }
    }

    fun update() {
        blockImages.updateDynamicImage()
        itemBar.bar.forEachIndexed { index, grid ->
            grid.item?.id?.let { iconUpdater.update(index.toString(), blockImages.getImage(it)) }
        }
        val icon: Icon = iconUpdater[itemBar.pointer.toString()]!!
        selectedItem.relativeX = icon.relativeX
        selectedItem.relativeX = icon.relativeX
    }
}