package com.ljb.bumscheduler.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ljb.bumscheduler.DlogUtil
import com.ljb.bumscheduler.MyTag
import com.ljb.bumscheduler.allMonth
import com.ljb.bumscheduler.currentDate
import com.ljb.bumscheduler.formatMonth
import com.ljb.bumscheduler.formatYearMonth
import com.ljb.bumscheduler.initialPage
import com.ljb.bumscheduler.ui.theme.DefaultBlue
import com.ljb.bumscheduler.ui.theme.DefaultRed
import com.ljb.bumscheduler.ui.theme.defaultTxtColor
import com.ljb.bumscheduler.viewmodel.CalendarViewModel
import com.ljb.bumscheduler.yearRange
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MapScreen(viewModel: CalendarViewModel = CalendarViewModel()) {

    val state by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState(initialPage = initialPage) {
        allMonth
    }

    Scaffold(
        topBar = {
            CalendarAppBar(
                modifier = Modifier.height(52.dp),
                menuClicked = {
                    DlogUtil.d(MyTag, "TopAppbar Menu Clicked")
                },
                searchClicked = {
                    DlogUtil.d(MyTag, "TopAppbar Search Clicked")
                },
                todayClicked = {
                    DlogUtil.d(MyTag, "TopAppbar Today Clicked")
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
        {

            TestHeader(
                modifier = Modifier.padding(horizontal = 10.dp),
                monthDate = state.displayDate
            )

            LaunchedEffect(pagerState){
                snapshotFlow { pagerState.currentPage }.collect {
                    val pagedMonth = LocalDate.of(
                        yearRange.first + it / 12,
                        it % 12 + 1,
                        1
                    )
                    viewModel.changeMonth(pagedMonth)
                }
            }

            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { page ->

                val year = yearRange.first + page / 12
                val month = page % 12 + 1
                val calendarDate = LocalDate.of(year, month, 1)

                CalendarGrid(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    calendarDate = calendarDate,
                    selectedDate = state.selectedDate,
                    onSelectDate = {

                    }
                )
            }

        }.also {
            DlogUtil.d(MyTag, "Recomposition MapScreen Column")
        }
    }.also {
        DlogUtil.d(MyTag, "Recomposition MapScreen Scaffold")
    }
}

@Composable
fun TestHeader(
    modifier: Modifier,
    monthDate: LocalDate
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 현재 년도면 월만 표시
        val displayMonth = if (monthDate.year == currentDate.year) {
            monthDate.format(formatMonth)
        } else {
            monthDate.format(formatYearMonth)
        }

        // 월 표시
        Text(
            text = displayMonth,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = defaultTxtColor(isSystemInDarkTheme())
        )

        // 요일 표시
        Row(
            modifier = Modifier.padding(top = 12.dp)
        ) {
            // 일요일부터 시작하도록 배열 회전
            val daysOfWeek = DayOfWeek.values()
            val firstDayIndex = daysOfWeek.indexOf(DayOfWeek.SUNDAY)
            val rotatedDays = daysOfWeek.drop(firstDayIndex) + daysOfWeek.take(firstDayIndex)

            for (dayOfWeek in rotatedDays) {

                // 요일 컬러 지정
                val textColor = when (dayOfWeek) {
                    DayOfWeek.SUNDAY -> DefaultRed
                    DayOfWeek.SATURDAY -> DefaultBlue
                    else -> defaultTxtColor(isSystemInDarkTheme())
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.KOREAN
                    ), // DayOfWeek 요일 Get 함수, 한국어로 표시
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestCalendar(
    viewModel: CalendarViewModel,
    pagerState: PagerState,
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
) {
    // HorizontalPager 가 Paging 될 때 마다 호출
    LaunchedEffect(pagerState.currentPage) {
        val pagedMonth = LocalDate.of(
            yearRange.first + pagerState.currentPage / 12,
            pagerState.currentPage % 12 + 1,
            1
        )
        //onSelectDate(pagedMonth)
        viewModel.changeMonth(pagedMonth)
    }

    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.Top
    ) { page ->

        val year = yearRange.first + page / 12
        val month = page % 12 + 1
        val calendarDate = LocalDate.of(year, month, 1)

        CalendarGrid(
            modifier = Modifier.padding(horizontal = 10.dp),
            calendarDate = calendarDate,
            selectedDate = selectedDate,
            onSelectDate = onSelectDate
        ).also {
            DlogUtil.d(MyTag, "Recomposition HorizontalCalendar CalendarGrid")
        }
    }.also {
        DlogUtil.d(MyTag, "Recomposition HorizontalCalendar HorizontalPager")
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    MapScreen()
}