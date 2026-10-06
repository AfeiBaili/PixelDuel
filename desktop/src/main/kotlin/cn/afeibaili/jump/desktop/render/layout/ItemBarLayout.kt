package cn.afeibaili.jump.desktop.render.layout

import cn.afeibaili.gl.image.atlas.Image
import cn.afeibaili.gl.render.layout.adapt.columnAdapt
import cn.afeibaili.gl.render.layout.align.AlignmentLayout
import cn.afeibaili.gl.render.layout.align.AlignmentSetting
import cn.afeibaili.gl.render.layout.align.AlignmentType
import cn.afeibaili.gl.render.layout.align.block
import cn.afeibaili.gl.render.layout.image.IconUpdater
import cn.afeibaili.gl.render.layout.image.icon
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

    fun load() {
        layout()
    }

    private fun layout() = Application.screen.layout {
        itemBarLayout = block(setting = { it.maxSize() }) {
            columnAdapt(setting = { it: AlignmentSetting -> it.align(AlignmentType.BOTTOM_CENTER) }) {
                for (index in 0 until itemBar.bar.size) {
                    val grid = itemBar.bar[index]
                    val image: Image = if (grid.item?.id == null) blockImages.getImage("air")
                    else blockImages.getImage(grid.item!!.id)
                    icon(index.toString(), image, iconUpdater)
                }
            }
        }
    }

    fun update() {
        blockImages.updateDynamicImage()
        itemBar.bar.forEachIndexed { index, grid ->
            grid.item?.id?.let { iconUpdater.update(index.toString(), blockImages.getImage(it)) }
        }
    }
}