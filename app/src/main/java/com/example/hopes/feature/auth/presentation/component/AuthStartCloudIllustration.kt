package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 시작 화면(피그마 01) 하단의 장식용 구름이다. 화면 폭을 가득 채운다.
 *
 * 뭉게구름 세 개를 겹쳐 그린다. 구름마다 위는 하늘색, 아래는 흰색으로 흐려지고, 겹친 곳은 조금 더 진해진다.
 * 좌표는 폭 400, 높이 220 기준이며 실제 크기에 맞춰 늘린다.
 */
@Composable
fun AuthStartCloudIllustration(modifier: Modifier = Modifier) {
    Canvas(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(DESIGN_WIDTH / DESIGN_HEIGHT)
                .blur(EDGE_BLUR, BlurredEdgeTreatment.Unbounded),
    ) {
        val scale = size.width / DESIGN_WIDTH
        CLOUDS.forEach { cloud -> drawCloud(cloud, scale) }
    }
}

private fun DrawScope.drawCloud(
    cloud: Cloud,
    scale: Float,
) {
    // 한 구름의 원·밑판은 하나의 Path로 합쳐 칠해야 겹친 부분이 진해지지 않는다.
    val path =
        Path().apply {
            cloud.puffs.forEach { puff ->
                addOval(
                    Rect(
                        center = Offset(puff.x * scale, puff.y * scale),
                        radius = puff.radius * scale,
                    ),
                )
            }
            addRoundRect(
                RoundRect(
                    rect =
                        Rect(
                            left = cloud.base.left * scale,
                            top = cloud.base.top * scale,
                            right = cloud.base.right * scale,
                            bottom = cloud.base.bottom * scale,
                        ),
                    cornerRadius = CornerRadius(cloud.baseRadius * scale),
                ),
            )
        }
    val bounds = path.getBounds()

    drawPath(
        path = path,
        brush =
            Brush.verticalGradient(
                colors = listOf(cloud.topColor, CLOUD_BOTTOM_COLOR),
                startY = bounds.top,
                endY = bounds.bottom,
            ),
    )
}

private data class Puff(
    val x: Float,
    val y: Float,
    val radius: Float,
)

private data class Cloud(
    val puffs: List<Puff>,
    val base: Rect,
    val baseRadius: Float,
    val topColor: Color,
)

private const val DESIGN_WIDTH = 400f
private const val DESIGN_HEIGHT = 220f
private val EDGE_BLUR = 3.dp

// 아래쪽은 흰색에 가깝게 흐려진다.
private val CLOUD_BOTTOM_COLOR = Color(0x26FFFFFF)

// 뒤에서 앞 순서다. 앞 구름일수록 위쪽 하늘색이 조금 더 진하다.
private val CLOUDS =
    listOf(
        // 왼쪽 구름
        Cloud(
            puffs = listOf(Puff(111f, 96f, 68f), Puff(58f, 142f, 48f)),
            base = Rect(0f, 122f, 205f, 206f),
            baseRadius = 42f,
            topColor = Color(0x80C9DCFB),
        ),
        // 오른쪽 뒤의 큰 구름
        Cloud(
            puffs = listOf(Puff(285f, 86f, 82f), Puff(380f, 110f, 50f), Puff(205f, 122f, 40f)),
            base = Rect(150f, 126f, 420f, 210f),
            baseRadius = 42f,
            topColor = Color(0x73D2E2FC),
        ),
        // 오른쪽 앞의 작은 구름
        Cloud(
            puffs = listOf(Puff(290f, 150f, 56f), Puff(366f, 150f, 42f)),
            base = Rect(190f, 152f, 420f, 218f),
            baseRadius = 34f,
            topColor = Color(0x8CBCD3FA),
        ),
    )

@Preview(showBackground = true, widthDp = 402)
@Composable
private fun AuthStartCloudIllustrationPreview() {
    AuthStartCloudIllustration()
}
