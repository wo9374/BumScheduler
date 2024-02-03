package com.ljb.bumscheduler.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ljb.bumscheduler.R
import com.ljb.bumscheduler.ui.component.ButtonDialog
import com.ljb.bumscheduler.ui.component.RadioButtonDialog
import com.ljb.bumscheduler.viewmodel.CalendarEvent
import com.ljb.bumscheduler.viewmodel.CalendarViewModel
import com.ljb.bumscheduler.viewmodel.SettingViewModel
import com.ljb.data.repository.DEVICE_MODE

@Composable
fun SettingScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            BtnDialogMenu(
                iconPainter = painterResource(id = R.drawable.ic_calendar_64px),
                menuTxt = stringResource(id = R.string.replace_holiday),
                dialogDescription = stringResource(id = R.string.dialog_delete_holiday)
            ) {
                calendarViewModel.processEvent(CalendarEvent.ClearHoliday)
            }

            BtnDialogMenu(
                iconPainter = painterResource(id = R.drawable.ic_schedule_64px),
                menuTxt = stringResource(id = R.string.delete_scheduler),
                dialogDescription = stringResource(id = R.string.dialog_delete_scheduler)
            ) {

            }

            RadioDialogMenu(
                viewModel = settingViewModel,
                iconPainter = painterResource(id = R.drawable.ic_darkmode_64px),
                menuTxt = stringResource(id = R.string.setting_darkmode),
                dialogDescription = stringResource(id = R.string.dialog_setting_darkmode)
            )
        }
    }
}

@Composable
fun BtnDialogMenu(
    iconPainter: Painter,
    menuTxt: String,
    dialogDescription: String,
    onConfirmDialog: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 2.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable { showDialog = true },
    ) {
        MenuBar(
            icon = iconPainter,
            title = menuTxt
        )

        if (showDialog) {
            ButtonDialog(
                titleIcon = iconPainter,
                title = menuTxt,
                description = dialogDescription,
                onDismissRequest = {
                    showDialog = false
                },
                confirmTxt = "삭제",
                confirmClicked = onConfirmDialog,
            )
        }
    }
}

@Composable
fun RadioDialogMenu(
    viewModel: SettingViewModel,
    iconPainter: Painter,
    menuTxt: String,
    dialogDescription: String
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 2.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable { showDialog = true },
    ) {
        MenuBar(
            icon = iconPainter,
            title = menuTxt
        )

        val darkModeState by viewModel.darkModeState.collectAsState()

        if (showDialog) {
            RadioButtonDialog(
                titleIcon = iconPainter,
                title = menuTxt,
                description = dialogDescription,
                radioSelected = darkModeState,
                onDismissRequest = { showDialog = false },
                onRadioSelected = {
                    viewModel.setDarkModePrefs(it)
                }
            )
        }
    }
}


@Composable
fun MenuBar(
    icon: Painter,
    title: String
) {
    val imgSize = 20.dp
    val imgBoxSize = 34.dp
    val menuInnerPadding = 8.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(menuInnerPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(imgBoxSize)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.outline),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(imgSize),
                painter = icon,
                contentDescription = title,
            )
        }

        Text(
            modifier = Modifier.padding(start = menuInnerPadding),
            text = title,
            fontSize = 16.sp,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BtnDialogMenuPreview() {
    var showDialog by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 2.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable { showDialog = true },
    ) {
        MenuBar(
            icon = painterResource(id = R.drawable.ic_calendar_64px),
            title = stringResource(id = R.string.replace_holiday)
        )

        if (showDialog) {
            ButtonDialog(
                titleIcon = painterResource(id = R.drawable.ic_calendar_64px),
                title = stringResource(id = R.string.replace_holiday),
                description = stringResource(id = R.string.dialog_delete_holiday),
                onDismissRequest = {},
                confirmTxt = "삭제",
                confirmClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RadioDialogMenuPreview() {
    var showDialog by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 2.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable { showDialog = true },
    ) {
        MenuBar(
            icon = painterResource(id = R.drawable.ic_darkmode_64px),
            title = stringResource(id = R.string.setting_darkmode)
        )

        if (showDialog) {
            RadioButtonDialog(
                titleIcon = painterResource(id = R.drawable.ic_darkmode_64px),
                title = stringResource(id = R.string.setting_darkmode),
                description = stringResource(id = R.string.dialog_setting_darkmode),
                radioSelected = DEVICE_MODE,
                onDismissRequest = {},
            ) {

            }
        }
    }
}