package cn.afeibaili.jump.desktop.render.layout

import cn.afeibaili.gl.font.FontFactory
import cn.afeibaili.gl.image.ImageUtil
import cn.afeibaili.gl.image.atlas.Image
import cn.afeibaili.gl.render.layout.adapt.columnAdapt
import cn.afeibaili.gl.render.layout.align.AlignmentLayout
import cn.afeibaili.gl.render.layout.align.AlignmentSetting
import cn.afeibaili.gl.render.layout.align.AlignmentType
import cn.afeibaili.gl.render.layout.align.block
import cn.afeibaili.gl.render.layout.image.icon
import cn.afeibaili.gl.render.layout.text.TextUpdater
import cn.afeibaili.jump.common.resource.ResourceFileGetter
import cn.afeibaili.jump.desktop.Application


/**
 * # 物品栏UI
 *
 * @author AfeiBaili
 * @version 2026/9/27 00:36
 */

class ItemBarLayout {
    val testFont = FontFactory.create(
        "source", ResourceFileGetter.getResourceFile("font/SourceHanSansHWSC-Regular.otf").canonicalPath, 64
    ).apply { texture.upload() }

    fun load() {
        layout()
    }

    private fun layout() = Application.screen.layout {
        val textUpdater = TextUpdater()

        val itemBar: AlignmentLayout = block(setting = { it.maxSize() }) {
            columnAdapt(setting = { it: AlignmentSetting -> it.align(AlignmentType.BOTTOM_CENTER) }) {
                icon(Image("dirt", ImageUtil.loadImage("B:\\Java\\Kotlin\\PixelDuel\\resource\\block\\dirt.png"))) {
                    it.size(50f, 50f)
                }
                icon(Image("dirt", ImageUtil.loadImage("B:\\Java\\Kotlin\\PixelDuel\\resource\\block\\stone.png"))) {
                    it.size(50f, 50f)
                }
            }
        }
    }
}