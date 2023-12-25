package com.ljb.bumscheduler.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ljb.bumscheduler.ui.theme.DefaultBlue
import com.ljb.bumscheduler.ui.theme.DefaultRed
import com.ljb.bumscheduler.ui.theme.defaultTxtColor
import com.ljb.bumscheduler.ui.theme.grayColor
import com.ljb.bumscheduler.ui.theme.reverseTxtColor
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    Scaffold { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            CalendarApp()
        }
    }
}

@Composable
fun CalendarApp() {
    val currentDate = LocalDate.now()
    var selectedDate by remember { mutableStateOf(currentDate) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CalendarHeader(currentDate)

            CalendarGrid(
                currentDate,
                selectedDate,
                onSelectedDate = { date ->
                    selectedDate = date
                }
            )
        }
    }
}

@Composable
fun CalendarHeader(currentDate: LocalDate) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //월 표시
        Text(
            text = currentDate.format(DateTimeFormatter.ofPattern("M월")),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            // 일요일부터 시작하도록 배열을 회전
            val daysOfWeek = DayOfWeek.values()
            val firstDayIndex = daysOfWeek.indexOf(DayOfWeek.SUNDAY)
            val rotatedDays = daysOfWeek.drop(firstDayIndex) + daysOfWeek.take(firstDayIndex)

            for (dayOfWeek in rotatedDays) {
                val textColor = if (dayOfWeek == DayOfWeek.SUNDAY)
                    DefaultRed
                else if (dayOfWeek == DayOfWeek.SATURDAY)
                    DefaultBlue
                else
                    defaultTxtColor(isSystemInDarkTheme())

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN), // 한국어로 표시
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CalendarGrid(
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onSelectedDate: (LocalDate) -> Unit
) {
    val lastDay by remember { mutableIntStateOf(currentDate.lengthOfMonth()) }
    val days by remember { mutableStateOf(IntRange(1, lastDay).toList()) }

    //첫 날의 Column 위치
    val firstDayOfWeek = currentDate.withDayOfMonth(1).dayOfWeek.value

    //마지막 날의 Column 위치 (0 = 일요일)
    val lastDayColumn = (firstDayOfWeek + lastDay - 1) % 7
    val afterDay = 7 - lastDayColumn - 1 // 일주일, 마지막 날 Column, index로 인한 -1

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
    ) {

        //첫 날의 요일을 맞추기 위한 빈 박스 생성
        repeat(firstDayOfWeek) {
            item {
                Box(modifier = Modifier.size(50.dp))
            }
        }

        items(days) { day ->
            val date = currentDate.withDayOfMonth(day)
            val isSelected = remember(selectedDate) {
                selectedDate.compareTo(date) == 0
            }

            CalendarDay(
                date = date,
                isToday = date == LocalDate.now(),
                isSelected = isSelected,
                onDateSelected = onSelectedDate
            )
        }

        //마지막 날 이후 빈 박스 생성
        repeat(afterDay) {
            item {
                Box(modifier = Modifier.size(50.dp))
            }
        }
    }
}

@Composable
fun CalendarDay(
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val textColor = when (date.dayOfWeek) {
        DayOfWeek.SUNDAY -> DefaultRed
        DayOfWeek.SATURDAY -> DefaultBlue
        else -> {
            if (isToday)
                reverseTxtColor(isSystemInDarkTheme())
            else
                defaultTxtColor(isSystemInDarkTheme())
        }
    }

    val todayBgColor = when (date.dayOfWeek) {
        DayOfWeek.SUNDAY -> DefaultRed
        DayOfWeek.SATURDAY -> DefaultBlue
        else -> grayColor(isSystemInDarkTheme())
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .calendarBorder(isSelected)
            .noRippleClickable { onDateSelected(date) },
        contentAlignment = Alignment.TopCenter
    ) {

        Box(
            modifier = Modifier
                .width(18.dp)
                .wrapContentHeight()
                .padding(top = 2.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .calendarBackground(isToday, todayBgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun Modifier.calendarBorder(boolean: Boolean) = composed {
    this.then(
        if (boolean) {
            border(
                width = 2.dp,
                color = grayColor(isSystemInDarkTheme()),
                shape = RoundedCornerShape(10.dp)
            )
        } else {
            this
        }
    )
}

fun Modifier.calendarBackground(boolean: Boolean, color: Color) = this.then(
    if (boolean) {
        background(color)
    } else {
        this
    }
)

//Modifier onClick 클릭 효과 제거
fun Modifier.noRippleClickable(enabled: Boolean = true, onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick.invoke()
    }
}


@Preview(showBackground = true)
@Composable
fun CalendarAppPreview() {
    CalendarApp()
}

@Preview(showBackground = true)
@Composable
fun CalendarHeaderPreview() {
    CalendarHeader(LocalDate.now())
}

@Preview(showBackground = true)
@Composable
fun CalendarGridPreview() {
    val currentDate = LocalDate.now()
    var selectedDate by remember { mutableStateOf(currentDate) }
    CalendarGrid(
        currentDate,
        selectedDate,
        onSelectedDate = { date ->
            selectedDate = date
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarDayPreview() {
    CalendarDay(
        date = LocalDate.now(),
        isToday = true,
        isSelected = true,
        onDateSelected = {}
    )
}
