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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.ljb.bumscheduler.ui.theme.DefaultBlue
import com.ljb.bumscheduler.ui.theme.DefaultRed
import com.ljb.bumscheduler.ui.theme.defaultTxtColor
import com.ljb.bumscheduler.ui.theme.grayColor
import com.ljb.bumscheduler.ui.theme.reverseTxtColor
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarScreen() {

    val scope = rememberCoroutineScope()

    val yearRange = IntRange(1970, 2100)

    // 현재 년도의 월 Page / HorizontalPager 의 page 는 0부터 시작, getMonthValue - 1을 해줘야 함
    val initialPage = (LocalDate.now().year - yearRange.first) * 12 + LocalDate.now().monthValue - 1

    val calendarPagerState = rememberPagerState(initialPage = initialPage) {
        (yearRange.last - yearRange.first) * 12 // 모든 년도의 월 수 만큼 pageCount
    }

    var currentPage by remember { mutableIntStateOf(initialPage) }   // 현재 Page
    var selectedDate by remember { mutableStateOf(LocalDate.now()) } // 선택된 Day LocalDate
    var currentMonth by remember { mutableStateOf(LocalDate.now()) } // 헤더에 보여줄 Month LocalDate

    // HorizontalPager 가 Paging 될 때 마다 호출
    LaunchedEffect(calendarPagerState.currentPage) {
        val addMonth = (calendarPagerState.currentPage - currentPage).toLong()
        currentMonth = currentMonth.plusMonths(addMonth)
        currentPage = calendarPagerState.currentPage
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
                        calendarPagerState.animateScrollToPage(initialPage)
                    }
                }
            )
        }
    )
    { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
        {
            HorizontalCalendar(
                yearRange = yearRange,
                pagerState = calendarPagerState,
                displayMonth = currentMonth,
                selectedDate = selectedDate,
                onSelectDate = { date ->
                    selectedDate = date
                }
            )
        }
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
            // AppBar 크기 변경으로 인한 자동 CenterVertically 적용이 안되어 Box 안에 IconButton 사용
            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center){
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
            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center){
                IconButton(onClick = searchClicked) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = defaultTxtColor(isSystemInDarkTheme())
                    )
                }
            }

            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center){
                // Today Button - IconButton() 에 Text() 사용할 수 없어 Box Clickable 대체
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .clickable { todayClicked.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = defaultTxtColor(isSystemInDarkTheme()),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(top = 2.dp, bottom = 3.dp, start = 3.dp, end = 3.dp),
                        text = LocalDate.now().dayOfMonth.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = defaultTxtColor(isSystemInDarkTheme()),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalCalendar(
    yearRange : IntRange,
    pagerState: PagerState,

    displayMonth : LocalDate,
    selectedDate: LocalDate,

    onSelectDate: (LocalDate) -> Unit
) {

    // 현재 보이는 페이지를 기반으로 로드할 페이지 범위를 계산 (성능 문제로 인한 보이는 페이지만 로딩)
    val currentPage = pagerState.currentPage
    val startPage = maxOf(0, currentPage - 1)
    val endPage = minOf(pagerState.pageCount - 1, currentPage + 1)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalendarHeader(displayMonth)

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { page ->

            // 현재 페이지가 로드할 범위 내에 있는지 확인
            if (page in startPage..endPage) {
                val displayDate = LocalDate.of(
                    yearRange.first + page / 12,
                    page % 12 + 1,
                    1
                )

                CalendarGrid(
                    calendarDate = displayDate,
                    selectedDate = selectedDate,
                    onSelectDate = onSelectDate
                )
            }
        }
    }
}

private const val DATE_FORMAT_MONTH = "M월"
private const val DATE_FORMAT_YEAR_MONTH = "yyyy년 M월"

@Composable
fun CalendarHeader(currentDate: LocalDate) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 현재 년도면 월만 표시
        val displayMonth = if (currentDate.year == LocalDate.now().year) {
            currentDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT_MONTH))
        } else {
            currentDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT_YEAR_MONTH))
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
    calendarDate: LocalDate,
    selectedDate: LocalDate,

    onSelectDate: (LocalDate) -> Unit
) {
    val lastDay by remember { mutableIntStateOf(calendarDate.lengthOfMonth()) }
    val days by remember { mutableStateOf(IntRange(1, lastDay).toList()) }

    //첫번째 날의 컬럼 위치 ( 1: 월요일 ~ 7: 일요일)
    val firstDayOfWeek = calendarDate.dayOfWeek.value

    //마지막 날의 컬럼 위치
    val lastDayColumn = (firstDayOfWeek + lastDay - 1) % 7

    //말일 이후 빈공간 Count / 일주일 - 마지막 날 Column, 일요일 시작이라 -1
    val afterBoxCount = 7 - lastDayColumn - 1

    //좌측 일요일 시작이라 7일때는 0으로 count
    val totalGridCount = if (firstDayOfWeek == 7)
        0
    else
        firstDayOfWeek + days.size + afterBoxCount

    // 표시 열(5주, 6주)이 달라 크기를 같게 위해 박스 크기 조절
    val boxHeight = if (totalGridCount > 35) {
        45.dp
    } else {
        54.dp
    }

    LazyVerticalGrid(columns = GridCells.Fixed(7)) {

        // 첫 날 전 채워야 할 빈칸 수 - 좌측 일요일부터 시작 7(일요일 index)이면 박스를 생성하지 않음
        if (firstDayOfWeek != 7) {
            repeat(firstDayOfWeek) {
                item {
                    EmptyDay(boxHeight)
                }
            }
        }

        items(days) { day ->
            val date = calendarDate.withDayOfMonth(day)
            val isSelected = selectedDate.compareTo(date) == 0

            CalendarDay(
                height = boxHeight,
                displayDate = date,
                isToday = date == LocalDate.now(),
                isSelected = isSelected,
                onSelectDate = onSelectDate
            )
        }

        // 마지막 날 후 채워야 할 빈칸 수
        repeat(afterBoxCount) {
            item {
                EmptyDay(boxHeight)
            }
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
                .calendarBackground(isToday, getTodayBgColor(displayDate.dayOfWeek)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayDate.dayOfMonth.toString(),
                color = getTextColor(displayDate.dayOfWeek),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EmptyDay(
    height: Dp
) {
    Box(modifier = Modifier.height(height))
}

@Composable
fun getTextColor(dayOfWeek: DayOfWeek): Color {
    return when (dayOfWeek) {
        DayOfWeek.SUNDAY -> DefaultRed
        DayOfWeek.SATURDAY -> DefaultBlue
        else -> defaultTxtColor(isSystemInDarkTheme())
    }
}

@Composable
fun getTodayBgColor(dayOfWeek: DayOfWeek): Color {
    return when (dayOfWeek) {
        DayOfWeek.SUNDAY -> DefaultRed
        DayOfWeek.SATURDAY -> DefaultBlue
        else -> grayColor(isSystemInDarkTheme())
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
        yearRange = IntRange(1970, 2100),
        pagerState = rememberPagerState(initialPage = 2) { 3 },
        displayMonth = LocalDate.now(),
        selectedDate = LocalDate.now(),
        onSelectDate = { }
    )
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
        onSelectDate = { date ->
            selectedDate = date
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarDayPreview() {
    val bigGrid = false
    val boxHeight = if (bigGrid) { 45.dp } else { 54.dp }

    CalendarDay(
        height = boxHeight,
        displayDate = LocalDate.now(),
        isToday = true,
        isSelected = true,
        onSelectDate = {}
    )
}