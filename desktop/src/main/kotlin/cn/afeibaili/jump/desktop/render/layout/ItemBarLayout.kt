package cn.afeibaili.jump.desktop.render.layout

import cn.afeibaili.gl.image.ImageUtil
import cn.afeibaili.gl.image.atlas.Image
import cn.afeibaili.gl.render.layout.adapt.columnAdapt
import cn.afeibaili.gl.render.layout.align.AlignmentLayout
import cn.afeibaili.gl.render.layout.align.AlignmentSetting
import cn.afeibaili.gl.render.layout.align.AlignmentType
import cn.afeibaili.gl.render.layout.align.block
import cn.afeibaili.gl.render.layout.image.icon
import cn.afeibaili.jump.desktop.Application
import cn.afeibaili.jump.desktop.item.ItemBar


/**
 * # 物品栏UI
 *
 * @author AfeiBaili
 * @version 2026/9/27 00:36
 */
class ItemBarLayout {
    lateinit var itemBarLayout: AlignmentLayout
    val itemBar: ItemBar get() = Application.worldEditor.itemBar

    fun load() {
        layout()
    }

    private fun layout() = Application.screen.layout {
        itemBarLayout = block(setting = { it.maxSize() }) {
            columnAdapt(setting = { it: AlignmentSetting -> it.align(AlignmentType.BOTTOM_CENTER) }) {
                icon(Image("dirt", ImageUtil.loadImage("B:\\Java\\Kotlin\\PixelDuel\\resource\\block\\dirt.png"))) {
                    it.size(50f, 50f)
                }
                icon(Image("stone", ImageUtil.loadImage("B:\\Java\\Kotlin\\PixelDuel\\resource\\block\\stone.png"))) {
                    it.size(50f, 50f)
                }
                icon(Image("grass", ImageUtil.loadImage("B:\\Java\\Kotlin\\PixelDuel\\resource\\block\\grass.png"))) {
                    it.size(50f, 50f)
                }
                icon(
                    Image(
                        "grass_dirt",
                        ImageUtil.loadImage("B:\\Java\\Kotlin\\PixelDuel\\resource\\block\\grass_dirt.png")
                    )
                ) {
                    it.size(50f, 50f)
                }
            }
        }
    }
}