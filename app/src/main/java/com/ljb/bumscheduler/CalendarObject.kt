package com.ljb.bumscheduler

import java.time.LocalDate
import java.time.format.DateTimeFormatter

val formatMonth: DateTimeFormatter = DateTimeFormatter.ofPattern("M월")
val formatYearMonth: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 M월")

val yearRange = IntRange(1970, 2100)
val currentDate: LocalDate = LocalDate.now()

val initialPage = (currentDate.year - yearRange.first) * 12 + currentDate.monthValue - 1
val allMonth: Int = (yearRange.last - yearRange.first) * 12


// 1970 ~ 2100 년 까지의 모든 월의 List
fun getYearMonth(): List<LocalDate> {
    val startMonth = LocalDate.of(1970, 1, 1)
    val endMonth = LocalDate.of(2100, 12, 1)

    return generateSequence(startMonth) { it.plusMonths(1) }
        .takeWhile { it.isBefore(endMonth) || it == endMonth }
        .toList()
}

// 인자 month 의 모든 날의 List
fun getMonthDay(month: LocalDate): List<LocalDate> {
    val startDay = month.withDayOfMonth(1)
    val endDay = month.withDayOfMonth(month.lengthOfMonth())

    return generateSequence(startDay) { it.plusDays(1) }
        .takeWhile { it.isBefore(endDay) || it == endDay }
        .toList()
}