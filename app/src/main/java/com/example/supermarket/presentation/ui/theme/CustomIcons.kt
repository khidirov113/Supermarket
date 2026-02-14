package com.example.supermarket.presentation.ui.theme


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object CustomIcons {

    val ChevronRight: ImageVector
        get() {
            if (_VscodeCodiconsChevronRight != null) return _VscodeCodiconsChevronRight!!

            _VscodeCodiconsChevronRight = ImageVector.Builder(
                name = "chevron-right",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 16f,
                viewportHeight = 16f
            ).apply {
                path(
                    fill = SolidColor(Color.Black)
                ) {
                    moveTo(5.64645f, 3.14645f)
                    curveTo(5.45118f, 3.34171f, 5.45118f, 3.65829f, 5.64645f, 3.85355f)
                    lineTo(9.79289f, 8f)
                    lineTo(5.64645f, 12.1464f)
                    curveTo(5.45118f, 12.3417f, 5.45118f, 12.6583f, 5.64645f, 12.8536f)
                    curveTo(5.84171f, 13.0488f, 6.15829f, 13.0488f, 6.35355f, 12.8536f)
                    lineTo(10.8536f, 8.35355f)
                    curveTo(11.0488f, 8.15829f, 11.0488f, 7.84171f, 10.8536f, 7.64645f)
                    lineTo(6.35355f, 3.14645f)
                    curveTo(6.15829f, 2.95118f, 5.84171f, 2.95118f, 5.64645f, 3.14645f)
                    close()
                }
            }.build()

            return _VscodeCodiconsChevronRight!!
        }

    private var _VscodeCodiconsChevronRight: ImageVector? = null


}