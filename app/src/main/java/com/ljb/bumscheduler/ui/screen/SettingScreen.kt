package com.ljb.bumscheduler.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ljb.bumscheduler.R
import com.ljb.bumscheduler.ui.theme.defaultGray
import com.ljb.bumscheduler.ui.theme.defaultTxtColor

@Composable
fun SettingScreen(
    //viewModel: CalendarViewModel = hiltViewModel()
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            SettingItem(SettingMenu.Holiday) {

            }

            SettingItem(SettingMenu.Schedule) {

            }

            SettingItem(SettingMenu.DarkMode) {

            }
        }
    }
}

sealed class SettingMenu(
    val iconPainter: Int,
    val itemText: String,
    val expandText: String,
) {
    data object Holiday : SettingMenu(
        R.drawable.ic_calendar_64px,
        "공휴일 정보 재구성",
        "모든 공휴일 정보를 삭제 합니다.\n달력 사용시 자동으로 공휴일 정보를 다시 받아옵니다.",
    )

    data object Schedule : SettingMenu(
        R.drawable.ic_schedule_64px,
        "일정 정보 모두 삭제",
        "저장된 모든 일정 정보를 삭제 합니다.\n삭제를 진행하시겠습니까?",
    )

    data object DarkMode : SettingMenu(
        R.drawable.ic_darkmode_64px,
        "다크모드 설정",
        "다크모드를 설정합니다.\n",
    )
}

@Composable
fun SettingItem(
    item: SettingMenu,
    itemClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val imgSize = 20.dp
    val imgBoxSize = 34.dp

    val menuInnerPadding = 8.dp

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 2.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable { expanded = !expanded },
    ) {

        Column(
            modifier = Modifier.padding(menuInnerPadding)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(imgBoxSize)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(
                            defaultGray(isSystemInDarkTheme())
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(imgSize),
                        painter = painterResource(item.iconPainter),
                        contentDescription = item.itemText,
                    )
                }

                Text(
                    modifier = Modifier.padding(start = menuInnerPadding),
                    text = item.itemText,
                    color = defaultTxtColor(isSystemInDarkTheme()),
                    fontSize = 16.sp,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }

            if (expanded) {
                when (item) {
                    is SettingMenu.Holiday, SettingMenu.Schedule -> {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(start = imgBoxSize + menuInnerPadding),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = item.expandText,
                                fontSize = 12.sp,
                            )

                            BlueButton(btnText = "삭제") {
                                // BtnClick
                            }
                        }
                    }

                    is SettingMenu.DarkMode -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Text(
                                modifier = Modifier.padding(start = imgBoxSize + menuInnerPadding),
                                text = item.expandText,
                                fontSize = 12.sp,
                            )

                            //TODO 다크모드 설정 ui 추가 필요
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BlueButton(
    btnText: String,
    btnClick: () -> Unit
) {
    Button(
        modifier = Modifier.wrapContentSize(),
        contentPadding = PaddingValues(12.dp),
        onClick = btnClick
    ) {
        Text(
            text = btnText,
            fontSize = 12.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingItemPreview() {
    Column {
        SettingItem(SettingMenu.Holiday) {}
        SettingItem(SettingMenu.Schedule) {}
        SettingItem(SettingMenu.DarkMode) {}
    }
}