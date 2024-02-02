package com.ljb.bumscheduler.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ljb.bumscheduler.R
import com.ljb.bumscheduler.ui.component.bgBorder
import com.ljb.bumscheduler.ui.component.bgToday
import com.ljb.bumscheduler.ui.component.noRippleClickable
import com.ljb.bumscheduler.ui.component.topHorizontalBorder
import com.ljb.bumscheduler.ui.theme.DefaultBlue
import com.ljb.bumscheduler.ui.theme.DefaultGreen
import com.ljb.bumscheduler.ui.theme.DefaultRed
import com.ljb.bumscheduler.viewmodel.CalendarEvent
import com.ljb.bumscheduler.viewmodel.CalendarViewModel
import com.ljb.data.DlogUtil
import com.ljb.data.MyTag
import com.ljb.data.datasource.LocalHolidaySourceImpl
import com.ljb.data.datasource.RemoteHolidaySourceImpl
import com.ljb.data.di.DataModule
import com.ljb.data.di.NetworkModule
import com.ljb.data.mapper.allMonth
import com.ljb.data.mapper.currentDate
import com.ljb.data.mapper.formatMonth
import com.ljb.data.mapper.formatYearMonth
import com.ljb.data.mapper.initialPage
import com.ljb.data.mapper.yearRange
import com.ljb.data.repository.HolidayRepositoryImpl
import com.ljb.domain.model.Holiday
import com.ljb.domain.usecase.GetHolidayUseCase
import com.ljb.domain.usecase.RequestHolidayUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel()
) {

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        initialPageOffsetFraction = 0f
    ) {
        allMonth
    }

    val scope = rememberCoroutineScope()

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
                    scope.launch {
                        viewModel.processEvent(CalendarEvent.SelectDate(currentDate))
                        pagerState.animateScrollToPage(initialPage)
                    }
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

            CalendarHeader(
                modifier = Modifier.padding(horizontal = 10.dp),
                viewModel = viewModel
            ).also {
                DlogUtil.d(MyTag, "Recomposition CalendarScreen CalendarHeader")
            }

            HorizontalCalendar(
                pagerState = pagerState,
                viewModel = viewModel
            ).also {
                DlogUtil.d(MyTag, "Recomposition CalendarScreen HorizontalCalendar")
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .topHorizontalBorder(
                        strokeWidth = 0.3.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        cornerRadiusDp = 0.dp
                    )
                //.background(MaterialTheme.colorScheme.onBackground)
            )

            HorizontalScheduler(
                viewModel = viewModel
            ).also {
                DlogUtil.d(MyTag, "Recomposition CalendarScreen HorizontalScheduler")
            }
        }.also {
            DlogUtil.d(MyTag, "Recomposition CalendarScreen Column")
        }
    }.also {
        DlogUtil.d(MyTag, "Recomposition CalendarScreen Scaffold")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarAppBar(
    modifier: Modifier = Modifier,
    menuClicked: () -> Unit,
    searchClicked: () -> Unit,
    todayClicked: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            // AppBar 크기 변경으로 인한 자동 CenterVertically 적용이 안되어 Box 로 wrap
            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                IconButton(onClick = menuClicked) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                    )
                }
            }
        },
        actions = {
            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                IconButton(onClick = searchClicked) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                    )
                }
            }

            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                // Today Button - IconButton() 에 Text() 사용할 수 없어 Box Clickable 대체
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .clickable { todayClicked.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .bgBorder(
                                strokeWidth = 0.5.dp,
                                cornerRadiusDp = 4.dp,
                                enabled = true
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 1.dp),
                            text = currentDate.dayOfMonth.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CalendarHeader(
    modifier: Modifier,
    viewModel: CalendarViewModel
) {
    val monthDate by viewModel.calendarMonth.collectAsState()

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
                    else -> LocalContentColor.current
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                    // DayOfWeek 요일 Get 함수, 한국어로 표시
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
fun HorizontalCalendar(
    pagerState: PagerState,
    viewModel: CalendarViewModel
) {
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            val pagedMonth = LocalDate.of(
                yearRange.first + it / 12,
                it % 12 + 1,
                1
            )
            viewModel.processEvent(CalendarEvent.SwipeMonth(pagedMonth))
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
            viewModel = viewModel,
            calendarDate = calendarDate,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalScheduler(
    viewModel: CalendarViewModel
) {
    val monthDate by viewModel.calendarMonth.collectAsState()
    val selectedDate by viewModel.selectDate.collectAsState()

    val days = (1..monthDate.lengthOfMonth()).toList().map {
        monthDate.withDayOfMonth(it)
    }

    val schedulerState = rememberPagerState(
        initialPage = selectedDate.dayOfMonth - 1,
        initialPageOffsetFraction = 0f
    ) {
        days.size
    }

    HorizontalPager(
        state = schedulerState
    ) { page ->

        val holidayList by viewModel.holidayList.collectAsState()

        val holidayItem = holidayList.find { it.localDate == days[page] }

        if (holidayItem != null) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    SchedulerHoliday(holidayItem)
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.empty_schedule),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }

    }

    LaunchedEffect(monthDate) {
        snapshotFlow { schedulerState.currentPage }.collect { currentPage ->
            val date = days[currentPage]
            viewModel.processEvent(CalendarEvent.SelectDate(date))
        }
    }

    LaunchedEffect(monthDate) {
        snapshotFlow { selectedDate }.collectLatest {
            schedulerState.scrollToPage(selectedDate.dayOfMonth - 1)
        }
    }
}

@Composable
fun SchedulerHoliday(
    item: Holiday
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp)
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "DateRange",
                )
            }

            Spacer(
                modifier = Modifier
                    .width(4.dp)
                    .height(22.dp)
                    .clip(shape = RoundedCornerShape(2.dp))
                    .background(DefaultGreen)
            )

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = item.dateName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(id = R.string.all_day),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Composable
fun CalendarGrid(
    modifier: Modifier,
    viewModel: CalendarViewModel,
    calendarDate: LocalDate,
) {

    val lastDay = calendarDate.lengthOfMonth()

    // 첫번째 날의 컬럼 위치 ( 1: 월요일 ~ 7: 일요일)
    val firstDayColumn = calendarDate.dayOfWeek.value
    val lastDayColumn = (firstDayColumn + lastDay - 1) % 7

    // 다음 달 Day / 일요일 시작이라 -1
    val afterBoxCount = 7 - lastDayColumn - 1

    // 총 달력의 Count
    var totalGridCount = lastDay
    if (firstDayColumn != 7) totalGridCount += firstDayColumn
    if (afterBoxCount != 7) totalGridCount += afterBoxCount


    // 표시 Week 크기를 같게 위해 박스 크기 조절 (270)
    val boxHeight = if (totalGridCount <= 35) {
        54.dp
    } else {
        45.dp
    }

    // 모든 날짜 리스트
    val displayDateList = mutableListOf<LocalDate>()

    // 이전 달의 일부 LocalDate 추가 부분
    if (firstDayColumn < 7) {
        val prevMonth = calendarDate.minusMonths(1)
        val prevLastDay = prevMonth.lengthOfMonth()

        (prevLastDay - firstDayColumn + 1..prevLastDay).forEach {
            displayDateList.add(
                prevMonth.withDayOfMonth(it)
            )
        }
    }

    // 현재 달의 LocalDate 추가 부분
    IntRange(1, lastDay).forEach {
        displayDateList.add(
            calendarDate.withDayOfMonth(it)
        )
    }

    // 다음 달의 LocalDate 추가 부분
    val nextMonth = calendarDate.plusMonths(1)
    if (afterBoxCount != 0) {            // 다음 달의 일부 LocalDate 존재할 때 추가
        (1..afterBoxCount).forEach {
            displayDateList.add(
                nextMonth.withDayOfMonth(it)
            )
        }
    } else if (totalGridCount <= 28) {  // 현재 달이 4주 일때는 다음 달 날짜를 7개 더 추가
        (1..7).toList().forEach {
            displayDateList.add(
                nextMonth.withDayOfMonth(it)
            )
        }
    }

    val selectedDate by viewModel.selectDate.collectAsState()

    val holidayList by viewModel.holidayList.collectAsState()

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(7)
    ) {

        items(displayDateList) { day ->

            val holidayItem = holidayList.find { it.localDate == day }

            if (day.monthValue == calendarDate.monthValue) {
                CalendarDay(
                    height = boxHeight,
                    displayDate = day,
                    holidayItem = holidayItem,
                    isSelected = selectedDate == day,
                    onSelectDate = {
                        viewModel.processEvent(CalendarEvent.SelectDate(it))
                    }
                )
            } else {
                PrevNextDay(
                    height = boxHeight,
                    displayDate = day,
                    holidayItem = holidayItem
                )
            }
        }
    }
}

@Composable
fun CalendarDay(
    height: Dp,
    displayDate: LocalDate,
    holidayItem: Holiday?,
    isSelected: Boolean,
    onSelectDate: (LocalDate) -> Unit
) {
    val isToday = displayDate == currentDate    // 오늘 날짜 Boolean

    val dayOfWeek = displayDate.dayOfWeek

    val todayBgColor = if (holidayItem?.isHoliday == true) {
        DefaultRed
    } else {
        when (dayOfWeek) {
            DayOfWeek.SUNDAY -> DefaultRed
            DayOfWeek.SATURDAY -> DefaultBlue
            else -> LocalContentColor.current
        }
    }

    val textColor = if (isToday) {
        MaterialTheme.colorScheme.background
    } else if (holidayItem?.isHoliday == true) {
        DefaultRed
    } else {
        when (dayOfWeek) {
            DayOfWeek.SUNDAY -> DefaultRed
            DayOfWeek.SATURDAY -> DefaultBlue
            else -> LocalContentColor.current
        }
    }

    Column(
        modifier = Modifier
            .height(height)
            .clip(shape = RoundedCornerShape(10.dp))
            // 선택 Day Gray Border
            .bgBorder(
                strokeWidth = 0.5.dp,
                cornerRadiusDp = 10.dp,
                enabled = isSelected
            )
            .noRippleClickable { onSelectDate(displayDate) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .width(18.dp)
                .wrapContentHeight()
                .padding(vertical = 2.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .bgToday(isToday, todayBgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayDate.dayOfMonth.toString(),
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (holidayItem != null) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(shape = RoundedCornerShape(2.dp))
                    .background(DefaultGreen)
            )
        }
    }
}

@Composable
fun PrevNextDay(
    height: Dp,
    displayDate: LocalDate,
    holidayItem: Holiday?
) {
    val isToday = displayDate == currentDate    // 오늘 날짜 Boolean

    val dayOfWeek = displayDate.dayOfWeek

    val todayBgColor = if (holidayItem?.isHoliday == true) {
        DefaultRed
    } else {
        when (dayOfWeek) {
            DayOfWeek.SUNDAY -> DefaultRed
            DayOfWeek.SATURDAY -> DefaultBlue
            else -> LocalContentColor.current
        }
    }

    val textColor = if (isToday) {
        MaterialTheme.colorScheme.background
    } else if (holidayItem?.isHoliday == true) {
        DefaultRed
    } else {
        when (dayOfWeek) {
            DayOfWeek.SUNDAY -> DefaultRed
            DayOfWeek.SATURDAY -> DefaultBlue
            else -> LocalContentColor.current
        }
    }

    Column(
        modifier = Modifier
            .height(height)
            .clip(shape = RoundedCornerShape(10.dp)),        // 선택 Day Gray Border
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .width(18.dp)
                .wrapContentHeight()
                .padding(vertical = 2.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .bgToday(isToday, todayBgColor.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayDate.dayOfMonth.toString(),
                color = textColor.copy(alpha = 0.3f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }

        if (holidayItem != null) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(shape = RoundedCornerShape(2.dp))
                    .background(DefaultGreen.copy(alpha = 0.3f))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarAppBar() {
    CalendarAppBar(
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

@Preview(showBackground = true)
@Composable
fun CalendarHeaderPreview() {
    val impl = HolidayRepositoryImpl(
        LocalHolidaySourceImpl(
            DataModule.provideHolidayDao(
                DataModule.provideHolidayDatabase(LocalContext.current)
            )
        ),
        RemoteHolidaySourceImpl(NetworkModule.provideHttpClient())
    )

    CalendarHeader(
        modifier = Modifier.padding(horizontal = 10.dp),
        viewModel = CalendarViewModel(RequestHolidayUseCase(impl), GetHolidayUseCase(impl))
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarGridPreview() {
    val impl = HolidayRepositoryImpl(
        LocalHolidaySourceImpl(
            DataModule.provideHolidayDao(
                DataModule.provideHolidayDatabase(LocalContext.current)
            )
        ),
        RemoteHolidaySourceImpl(NetworkModule.provideHttpClient())
    )
    CalendarGrid(
        modifier = Modifier.padding(horizontal = 10.dp),
        viewModel = CalendarViewModel(RequestHolidayUseCase(impl), GetHolidayUseCase(impl)),
        calendarDate = currentDate,
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarDayPreview() {
    val totalGridCount = 35
    val boxHeight = if (totalGridCount <= 28) {
        67.5.dp
    } else if (totalGridCount <= 35) {
        54.dp
    } else {
        45.dp
    }

    CalendarDay(
        height = boxHeight,
        displayDate = currentDate,
        holidayItem = Holiday(currentDate, "신정", true),
        isSelected = true,
        onSelectDate = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PrevNextDayPreview() {
    val totalGridCount = 35
    val boxHeight = if (totalGridCount <= 28) {
        67.5.dp
    } else if (totalGridCount <= 35) {
        54.dp
    } else {
        45.dp
    }

    PrevNextDay(
        height = boxHeight,
        displayDate = currentDate,
        holidayItem = Holiday(currentDate, "신정", true),
    )
}

@Preview(showBackground = true)
@Composable
fun SchedulerHolidayPreview() {
    SchedulerHoliday(
        item = Holiday(currentDate, "신정", true)
    )
}