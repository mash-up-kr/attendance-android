package com.mashup.ui.schedule.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray950
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body3

/**
 * 일정 탭을 표시하는 컴포저블입니다.
 * @param modifier Modifier
 * @param tabMenu List<String> 탭 메뉴 리스트
 * @param selectedTabIndex Int 선택된 탭 인덱스
 * @param updateSelectedTabIndex (Int) -> Unit 선택된 탭 인덱스 업데이트 함수
 *
 * 사용 예시
 * ```
 * val selectedTabIndex by remember { mutableIntStateOf(0) }
 *
 * ScheduleTabRow(
 *   modifier = Modifier.fillMaxWidth(),
 *   selectedTabIndex = selectedTabIndex,
 *   updateSelectedTabIndex = { selectedTabIndex = it }
 * )
 *
 * when(selectedTabIndex) {
 *  0 -> { // 이번 주 일정 Contents }
 *  1 -> { // 전체 일정 Contents }
 * }
 * ```
 */
@Composable
fun ScheduleTabRow(
    modifier: Modifier = Modifier,
    tabMenu: List<String> = listOf("이번 주 일정", "전체 일정"),
    selectedTabIndex: Int = 0,
    updateSelectedTabIndex: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier.height(26.dp)
        )
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = Gray950,
                    modifier = Modifier.customTabIndicatorOffset(
                        tabPositions[selectedTabIndex],
                        tabWidth = 150.dp
                    ).clip(
                        RoundedCornerShape(20.dp)
                    )
                )
            },
            tabs = {
                tabMenu.forEachIndexed { index, a ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            updateSelectedTabIndex(index)
                        }
                    ) {
                        Text(
                            text = a,
                            style = Body3.copy(
                                fontWeight = if (index == selectedTabIndex) {
                                    FontWeight.W700
                                } else {
                                    FontWeight.W400
                                }

                            ),
                            color = Gray950
                        )
                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )
                    }
                }
            }
        )
    }
}

fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = ""
    )

    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = ""
    )

    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart) // indicator 표시 위치
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}

@Preview
@Composable
private fun PreviewTabRow() {
    MashUpTheme {
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        ScheduleTabRow(
            modifier = Modifier.fillMaxWidth().background(color = Color.White),
            selectedTabIndex = selectedTabIndex,
            updateSelectedTabIndex = {
                selectedTabIndex = it
            }
        )
    }
}
