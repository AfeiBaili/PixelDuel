package cn.afeibaili.jump.desktop.render.font

import cn.afeibaili.gl.font.FontFactory
import cn.afeibaili.jump.common.resource.ResourceFileGetter


/**
 * # 字体类
 *
 * @author AfeiBaili
 * @version 2026/10/8 23:26
 */

object Fonts {
    val font = FontFactory.create(
        "source", ResourceFileGetter.getResourceFile("font/SourceHanSansHWSC-Regular.otf").canonicalPath, 64
    ).apply { texture.upload() }
}