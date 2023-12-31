package com.ljb.bumscheduler.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ljb.bumscheduler.DlogUtil
import com.ljb.bumscheduler.MyTag
import com.ljb.bumscheduler.currentDate
import com.ljb.bumscheduler.formatMonth
import com.ljb.bumscheduler.formatYearMonth
import com.ljb.bumscheduler.initialPage
import com.ljb.bumscheduler.yearRange
import com.ljb.bumscheduler.ui.theme.DefaultBlue
import com.ljb.bumscheduler.ui.theme.DefaultRed
import com.ljb.bumscheduler.ui.theme.defaultTxtColor
import com.ljb.bumscheduler.ui.theme.grayColor
import com.ljb.bumscheduler.ui.theme.reverseTxtColor
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarScreen() {

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(initialPage = initialPage) {
        (yearRange.last - yearRange.first) * 12 // 모든 년도의 월 수 만큼 pageCount
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
                    scope.launch {
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

            var headerMonth by remember { mutableStateOf(currentDate) }     // 헤더에 보여줄 Month LocalDate

            CalendarHeader(
                modifier = Modifier.padding(horizontal = 10.dp),
                monthDate = headerMonth
            ).also {
                DlogUtil.d(MyTag, "Recomposition CalendarScreen CalendarHeader")
            }

            var selectedDate by remember { mutableStateOf(headerMonth) }    // 선택된 Day LocalDate

            HorizontalCalendar(
                pagerState = pagerState,
                selectedDate = selectedDate,
                changeMonth = { headerMonth = it },
                onSelectDate = { selectedDate = it }
            ).also {
                DlogUtil.d(MyTag, "Recomposition CalendarScreen HorizontalCalendar")
            }

            HorizontalScheduler(
                monthDate = headerMonth,
                selectedDate = selectedDate,
                onSelectDate = { selectedDate = it },
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
        //colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Magenta),
        title = {},
        navigationIcon = {
            // AppBar 크기 변경으로 인한 자동 CenterVertically 적용이 안되어 Box 로 wrap
            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                IconButton(onClick = menuClicked) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = defaultTxtColor(isSystemInDarkTheme())
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
                        tint = defaultTxtColor(isSystemInDarkTheme())
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
                            .border(
                                width = 1.dp,
                                color = defaultTxtColor(isSystemInDarkTheme()),
                                shape = RoundedCornerShape(4.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 1.dp),
                            text = currentDate.dayOfMonth.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = defaultTxtColor(isSystemInDarkTheme()),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalCalendar(
    pagerState: PagerState,
    selectedDate: LocalDate,

    changeMonth: (LocalDate) -> Unit,
    onSelectDate: (LocalDate) -> Unit,
) {
    // HorizontalPager 가 Paging 될 때 마다 호출
    LaunchedEffect(pagerState.currentPage) {
        val pagedMonth = LocalDate.of(
            yearRange.first + pagerState.currentPage / 12,
            pagerState.currentPage % 12 + 1,
            1
        )
        onSelectDate(pagedMonth)
        changeMonth(pagedMonth)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalScheduler(
    monthDate: LocalDate,
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
){
    val days = (1..monthDate.lengthOfMonth()).toList().map{
        monthDate.withDayOfMonth(it)
    }

    val schedulerState = rememberPagerState(initialPage = selectedDate.dayOfMonth - 1) { days.size }

    LaunchedEffect(schedulerState) {
        snapshotFlow { schedulerState.currentPage }.collect { currentPage ->
            val date = days[currentPage]
            onSelectDate(date)
        }
    }

    HorizontalPager(
        state = schedulerState
    ){ page ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(text = "${days[page]}")
        }
    }

    LaunchedEffect(selectedDate){
        schedulerState.scrollToPage(selectedDate.dayOfMonth - 1)
    }
}

@Composable
fun CalendarHeader(
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

@Composable
fun CalendarGrid(
    modifier: Modifier,
    calendarDate: LocalDate,
    selectedDate: LocalDate,

    onSelectDate: (LocalDate) -> Unit
) {
    val lastDay = calendarDate.lengthOfMonth()

    // 현재 Month 의 Day LocalDate
    val days = IntRange(1, lastDay).toList().map { day ->
        calendarDate.withDayOfMonth(day)
    }

    // 첫번째 날의 컬럼 위치 ( 1: 월요일 ~ 7: 일요일)
    val firstDayColumn = calendarDate.dayOfWeek.value
    val lastDayColumn = (firstDayColumn + lastDay - 1) % 7

    // 다음 달 Day / 일요일 시작이라 -1
    val afterBoxCount = 7 - lastDayColumn - 1

    // 총 달력의 Count
    var totalGridCount = days.size
    if (firstDayColumn != 7) totalGridCount += firstDayColumn
    if (afterBoxCount != 7) totalGridCount += afterBoxCount

    // 표시 Week 크기를 같게 위해 박스 크기 조절 (270)
    val boxHeight = if (totalGridCount <= 35){
        54.dp
    } else {
        45.dp
    }

    // 이전 달 Day
    val prevMonthDay = if (firstDayColumn < 7){
        val prevMonth = calendarDate.minusMonths(1)
        val prevLastDay = prevMonth.lengthOfMonth()
        (prevLastDay - firstDayColumn + 1 .. prevLastDay).toList().map {
            prevMonth.withDayOfMonth(it)
        }
    } else
        emptyList()


    val nextMonth = calendarDate.plusMonths(1)
    val nextMonthDay = if (afterBoxCount != 0){
        (1 .. afterBoxCount).toList().map {
            nextMonth.withDayOfMonth(it)
        }
    } else if (totalGridCount <= 28) {      // 현재 달이 4주 일때는 다음 달 날짜를 7개 더 추가
        (1 .. 7).toList().map {
            nextMonth.withDayOfMonth(it)
        }
    } else
        emptyList()

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(7)
    ) {

        items(prevMonthDay) { day ->
            PrevNextDay(
                height = boxHeight,
                displayDate = day,
                isToday = false,
                onSelectDate = {}
            )
        }

        items(days) { day ->
            CalendarDay(
                height = boxHeight,
                displayDate = day,
                isToday = day == currentDate,
                isSelected = selectedDate.compareTo(day) == 0,
                onSelectDate = onSelectDate
            )
        }

        items(nextMonthDay){ day ->
            PrevNextDay(
                height = boxHeight,
                displayDate = day,
                isToday = false,
                onSelectDate = {}
            )
        }
    }
}

@Composable
fun CalendarDay(
    height: Dp,
    displayDate: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    onSelectDate: (LocalDate) -> Unit
) {
    val dayOfWeek = displayDate.dayOfWeek

    val todayBgColor = when (dayOfWeek) {
        DayOfWeek.SUNDAY -> DefaultRed
        DayOfWeek.SATURDAY -> DefaultBlue
        else -> grayColor(isSystemInDarkTheme())
    }

    val textColor = if(isToday){
        reverseTxtColor(isSystemInDarkTheme())
    } else {
        when (dayOfWeek) {
            DayOfWeek.SUNDAY -> DefaultRed
            DayOfWeek.SATURDAY -> DefaultBlue
            else -> defaultTxtColor(isSystemInDarkTheme())
        }
    }

    Box(
        modifier = Modifier
            .height(height)
            .clip(shape = RoundedCornerShape(10.dp))
            .calendarBorder(isSelected)                    // 선택 Day Gray Border
            .noRippleClickable { onSelectDate(displayDate) },
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
                text = displayDate.dayOfMonth.toString(),
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PrevNextDay(
    height: Dp,
    displayDate: LocalDate,
    isToday: Boolean,
    onSelectDate: (LocalDate) -> Unit,
) {
    val dayOfWeek = displayDate.dayOfWeek

    val textColor = if(isToday){
        reverseTxtColor(isSystemInDarkTheme())
    } else {
        when (dayOfWeek) {
            DayOfWeek.SUNDAY -> DefaultRed
            DayOfWeek.SATURDAY -> DefaultBlue
            else -> defaultTxtColor(isSystemInDarkTheme())
        }
    }

    Box(
        modifier = Modifier
            .height(height)
            .clip(shape = RoundedCornerShape(10.dp))
            .noRippleClickable { onSelectDate(displayDate) },
        contentAlignment = Alignment.TopCenter
    ) {

        Box(
            modifier = Modifier
                .width(18.dp)
                .wrapContentHeight()
                .padding(top = 2.dp)
                .clip(shape = RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayDate.dayOfMonth.toString(),
                color = textColor.copy(alpha = 0.3f),
                fontSize = 12.sp,
                //fontWeight = FontWeight.Bold
            )
        }
    }

    Box(modifier = Modifier.height(height))
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


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun HorizontalCalendarPreview() {
    HorizontalCalendar(
        pagerState = rememberPagerState(initialPage = 2) { 3 },
        selectedDate = currentDate,
        changeMonth = { },
        onSelectDate = { }
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarHeaderPreview() {
    CalendarHeader(
        Modifier.padding(horizontal = 10.dp),
        currentDate
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarGridPreview() {
    var selectedDate by remember { mutableStateOf(currentDate) }
    CalendarGrid(
        modifier = Modifier.padding(horizontal = 10.dp),
        currentDate,
        selectedDate,
        onSelectDate = { date ->
            selectedDate = date
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarDayPreview() {
    val totalGridCount = 35
    val boxHeight = if (totalGridCount <= 28) {
        67.5.dp
    } else if (totalGridCount <= 35){
        54.dp
    } else {
        45.dp
    }

    CalendarDay(
        height = boxHeight,
        displayDate = currentDate,
        isToday = true,
        isSelected = true,
        onSelectDate = {}
    )
}

@Preview( showBackground = true)
@Composable
fun PrevNextDayPreview(){
    val totalGridCount = 35
    val boxHeight = if (totalGridCount <= 28) {
        67.5.dp
    } else if (totalGridCount <= 35){
        54.dp
    } else {
        45.dp
    }

    PrevNextDay(
        height = boxHeight,
        displayDate = currentDate,
        isToday = true,
        onSelectDate = {}
    )
}