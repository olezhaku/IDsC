package com.olezhaku.idsc.utils

import java.util.Calendar
import java.util.TimeZone
import kotlin.random.Random


data class BuildDate(
    val year: String,
    val month: String,
    val day: String,
    val hour: String,
    val minute: String,
    val second: String,
    val buildDate: String,
    val buildTime: String,
    val buildDateStr: String,
    val buildUtc: String,
    val gitBuildTime: String,
    val securityPatch: String
)

private val MONTH_NAMES = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)

private val WEEKDAY_NAMES = listOf(
    "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
)

private fun pad(value: Int) = value.toString().padStart(2, '0')

private fun getBuildIdDatePart(buildId: String): String {
    val datePart = buildId.split(".").getOrNull(1).orEmpty()

    require(Regex("^\\d{6}$").matches(datePart)) {
        "Unsupported build id format: $buildId"
    }

    return datePart
}

fun generateBuildDate(buildId: String): BuildDate {
    val datePart = getBuildIdDatePart(buildId)

    val year = "20${datePart.substring(0, 2)}"
    val month = datePart.substring(2, 4)
    val day = datePart.substring(4, 6)

    val hour = pad(Random.nextInt(24))
    val minute = pad(Random.nextInt(60))
    val second = pad(Random.nextInt(60))

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        set(Calendar.YEAR, year.toInt())
        set(Calendar.MONTH, month.toInt() - 1)
        set(Calendar.DAY_OF_MONTH, day.toInt())
        set(Calendar.HOUR_OF_DAY, hour.toInt())
        set(Calendar.MINUTE, minute.toInt())
        set(Calendar.SECOND, second.toInt())
    }

    val weekday = WEEKDAY_NAMES[calendar.get(Calendar.DAY_OF_WEEK) - 1]
    val monthName = MONTH_NAMES[month.toInt() - 1]

    return BuildDate(
        year = year,
        month = month,
        day = day,
        hour = hour,
        minute = minute,
        second = second,
        buildDate = "$year$month$day",
        buildTime = "$hour$minute$second",
        buildDateStr = "$weekday $monthName $day $hour:$minute:$second CST $year",
        buildUtc = (calendar.timeInMillis / 1000).toString(),
        gitBuildTime = "$year$month$day$hour$minute$second",
        securityPatch = "$year-$month-05"
    )
}
