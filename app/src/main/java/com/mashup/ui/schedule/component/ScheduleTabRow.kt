package com.mashup.ui.schedule.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray950
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body3

@Composable
fun ScheduleTabRow(
    modifier: Modifier = Modifier,
    tabMenu: List<String> = listOf("아번 주 일정", "전체 일정"),
    selectedTabIndex: Int = 0,
    updateSelectedTabIndex: (Int) -> Unit = {}
) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                color = Gray950,
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
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

@Preview
@Composable
private fun PreviewTabRow() {
    MashUpTheme {
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        ScheduleTabRow(
            selectedTabIndex = selectedTabIndex,
            updateSelectedTabIndex = {
                selectedTabIndex = it
            }
        )
    }
}
