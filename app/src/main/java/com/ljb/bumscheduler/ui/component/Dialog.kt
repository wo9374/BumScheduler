package com.ljb.bumscheduler.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ljb.bumscheduler.R
import com.ljb.bumscheduler.ui.theme.BlueGray39
import com.ljb.bumscheduler.ui.theme.DefaultBlue
import com.ljb.bumscheduler.ui.theme.Gray55
import com.ljb.bumscheduler.ui.theme.White95
import com.ljb.data.repository.DARK_MODE
import com.ljb.data.repository.DEVICE_MODE
import com.ljb.data.repository.LIGHT_MODE

@Composable
fun BaseDialog(
    titleIcon: Painter,
    title: String,
    description: String,
    onDismissRequest: () -> Unit,
    component: @Composable () -> Unit = {}
) = Dialog(onDismissRequest = onDismissRequest) {

    Surface(shape = RoundedCornerShape(12.dp)) {
        val imgSize = 20.dp

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(imgSize),
                    painter = titleIcon,
                    contentDescription = title,
                )

                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = title,
                    fontSize = 16.sp,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }

            Text(
                modifier = Modifier.padding(
                    start = 14.dp,
                    end = 14.dp,
                    top = 8.dp,
                    bottom = 12.dp
                ),
                text = description,
                fontSize = 13.sp,
            )

            component()
        }
    }
}

@Composable
fun ButtonDialog(
    titleIcon: Painter,
    title: String,
    description: String,
    onDismissRequest: () -> Unit,

    confirmTxt: String,
    confirmClicked: () -> Unit,
) {
    BaseDialog(titleIcon, title, description, onDismissRequest) {
        Row {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                onClick = onDismissRequest
            ) {
                Text("취소")
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                onClick = {
                    confirmClicked()
                    onDismissRequest()
                }
            ) {
                Text(confirmTxt)
            }
        }
    }
}

@Composable
fun RadioButtonDialog(
    titleIcon: Painter,
    title: String,
    description: String,
    radioSelected: String,
    onDismissRequest: () -> Unit,
    onRadioSelected: (String) -> Unit,
) {
    BaseDialog(titleIcon, title, description, onDismissRequest) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            DarkModeView(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(horizontal = 7.dp),
                false,
                selected = radioSelected == LIGHT_MODE,
                radioTxt = "라이트",
                onSelected = {
                    onRadioSelected(LIGHT_MODE)
                }
            )

            DarkModeView(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(horizontal = 7.dp),
                true,
                selected = radioSelected == DARK_MODE,
                radioTxt = "다크",
                onSelected = {
                    onRadioSelected(DARK_MODE)
                }
            )

            DarkModeView(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(horizontal = 7.dp),
                isSystemInDarkTheme(),
                selected = radioSelected == DEVICE_MODE,
                radioTxt = "기기 설정",
                onSelected = {
                    onRadioSelected(DEVICE_MODE)
                }
            )
        }
    }
}

@Composable
fun DarkModeView(
    modifier: Modifier,
    darkMode: Boolean,
    radioTxt: String,
    selected: Boolean,
    onSelected: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp,
                    )
                )
                .background(if (darkMode) Color.DarkGray else Color.LightGray)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 3.dp, start = 3.dp, end = 3.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 10.dp,
                        )
                    )
                    .background(if (darkMode) BlueGray39 else White95)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 4.dp, end = 4.dp, bottom = 6.dp)
                        .aspectRatio(2f / 1f)
                        .clip(shape = RoundedCornerShape(6.dp))
                        .background(if (darkMode) Gray55 else Color.White),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(2) {
                        Row(
                            modifier = Modifier.weight(0.5f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 6.dp)
                                    .aspectRatio(1f)
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .background(DefaultBlue)
                            )

                            Spacer(
                                modifier = Modifier
                                    .weight(4f)
                                    .padding(horizontal = 8.dp)
                                    .aspectRatio(10f / 1f)
                                    .background(Color.LightGray)
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .aspectRatio(12f / 1f)
                        .clip(
                            shape = RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                            )
                        )
                        .background(if (darkMode) Gray55 else Color.White)
                )
            }
        }

        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = radioTxt,
            fontSize = 13.sp,
            color = if (selected) DefaultBlue else LocalContentColor.current,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )

        RadioButton(
            selected = selected,
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = DefaultBlue,
                //unselectedColor =
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonDialogPreview() {
    val iconPainter = painterResource(id = R.drawable.ic_calendar_64px)
    val menuTxt = stringResource(id = R.string.replace_holiday)
    val dialogDescription = stringResource(id = R.string.dialog_delete_holiday)

    ButtonDialog(
        titleIcon = iconPainter,
        title = menuTxt,
        description = dialogDescription,
        onDismissRequest = {},
        confirmTxt = "삭제",
        confirmClicked = {},
    )
}

@Preview(showBackground = true)
@Composable
fun RadioButtonDialogPreview() {
    val iconPainter = painterResource(id = R.drawable.ic_darkmode_64px)
    val menuTxt = stringResource(id = R.string.setting_darkmode)
    val dialogDescription = stringResource(id = R.string.dialog_setting_darkmode)

    RadioButtonDialog(
        titleIcon = iconPainter,
        title = menuTxt,
        description = dialogDescription,
        radioSelected = DEVICE_MODE,
        onDismissRequest = {}
    ){

    }
}