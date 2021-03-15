package co.anitrend.arch.extension.util.date.contract

import android.annotation.TargetApi
import android.os.Build
import co.anitrend.arch.extension.util.attribute.SeasonType
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Contract for date helper
 *
 * @since v1.2.0
 */
abstract class AbstractSupportDateHelper {

    /**
     * @return current seasons name
     */
    abstract val currentSeason: SeasonType

    /**
     * Gets the current year + delta, if the season for the year is winter later in the year
     * then the result would be the current year plus the delta
     *
     * @return current year with a given delta
     */
    abstract fun getCurrentYear(delta: Int = 0): Int

    /**
     * [ISO 8601](https://www.iso.org/iso-8601-date-and-time-format.html) date format pattern, this default pattern
     * targets API level [Build.VERSION_CODES.N] and up
     *
     * @see [SimpleDateFormat](https://developer.android.com/reference/java/text/SimpleDateFormat.html)
     */
    val defaultInputDatePattern
        @TargetApi(Build.VERSION_CODES.N)
        get() = "yyyy-MM-dd'T'HH:mm:ssXXX"

    /**
     * @see [SimpleDateFormat](https://developer.android.com/reference/java/text/SimpleDateFormat.html)
     */
    val defaultOutputDatePattern
        get() =  "yyyy-MM-dd HH:mm:ss"

    /**
     * Helper utility for converting unix timestamp to a date string format
     *
     * @param unixTimeStamp Unix based time stamp of type [Long] measured in milliseconds,
     * see [System.currentTimeMillis]
     * @param outputDatePattern pattern that will be used create the output,
     * by default this is set to [defaultOutputDatePattern]
     * @param targetTimeZone  time zone that the output will be converted to,
     * by default the timezone is set to whatever the current device is set to
     *
     * @return date string following the output pattern of [outputDatePattern]
     *
     * @see [DateTimeFormatter](https://developer.android.com/reference/java/time/format/DateTimeFormatter)
     */
    fun convertFromUnixTimeStamp(
        unixTimeStamp: Long,
        locale: Locale = Locale.getDefault(),
        outputDatePattern: String = defaultOutputDatePattern,
        targetTimeZone: TimeZone = TimeZone.getDefault()
    ): String {
        val originDate = Instant.ofEpochMilli(unixTimeStamp)

        val outputDateFormat = DateTimeFormatter.ofPattern(
            outputDatePattern, locale
        ).withZone(ZoneId.of(targetTimeZone.id))

        return outputDateFormat.format(originDate)
    }

    /**
     * Helper utility for converting dates in string format to unix time stamp
     *
     * @param originDate input date which needs to be converted
     * @param inputPattern pattern representing the [originDate],
     * the default is set to [defaultInputDatePattern]
     * @param targetTimeZone  time zone that the output will be converted to,
     * by default the timezone is set to whatever the current device is set to
     *
     * @return Unix based time stamp of type [Long] measured in milliseconds
     *
     * @see [DateTimeFormatter](https://developer.android.com/reference/java/time/format/DateTimeFormatter)
     */
    fun convertToUnixTimeStamp(
        originDate: String,
        locale: Locale = Locale.getDefault(),
        inputPattern: String = defaultInputDatePattern,
        targetTimeZone: TimeZone = TimeZone.getDefault()
    ): Long {
        val dateFormatter = DateTimeFormatter.ofPattern(
            inputPattern, locale
        ).withZone(ZoneId.of(targetTimeZone.id))

        val parsedDate = dateFormatter.parse(originDate)
        val zonedDateTime = ZonedDateTime.from(parsedDate).toOffsetDateTime()

        return zonedDateTime.toInstant().toEpochMilli()
    }

    /**
     * Helper utility for converting dates in string format to unix time stamp
     *
     * @param originDate input date which needs to be converted
     * @param dateTimeFormatter date time format representing the [originDate]
     * @param targetTimeZone  time zone that the output will be converted to,
     * by default the timezone is set to whatever the current device is set to
     *
     * @return Unix based time stamp of type [Long] measured in milliseconds
     *
     * @see [DateTimeFormatter](https://developer.android.com/reference/java/time/format/DateTimeFormatter)
     */
    fun convertToUnixTimeStamp(
        originDate: String,
        locale: Locale = Locale.getDefault(),
        dateTimeFormatter: DateTimeFormatter,
        targetTimeZone: TimeZone = TimeZone.getDefault()
    ): Long {
        val parsedDate = dateTimeFormatter
            .withZone(ZoneId.of(targetTimeZone.id))
            .parse(originDate)
        val zonedDateTime = ZonedDateTime.from(parsedDate).toOffsetDateTime()

        return zonedDateTime.toInstant().toEpochMilli()
    }

    /**
     * Helper utility for converting dates in string format from one date type to the other
     *
     * @param originDate input date which needs to be converted
     * @param inputPattern pattern representing the [originDate],
     * the default is set to [defaultInputDatePattern]
     * @param outputDatePattern pattern that will be used create the output,
     * by default this is set to [defaultOutputDatePattern]
     * @param targetTimeZone  time zone that the output will be converted to,
     * by default the timezone is set to whatever the current device is set to
     *
     * @return date string following the output pattern of [outputDatePattern]
     *
     * @see [DateTimeFormatter](https://developer.android.com/reference/java/time/format/DateTimeFormatter)
     */
    fun convertToTimeStamp(
        originDate: String,
        locale: Locale = Locale.getDefault(),
        inputPattern: String = defaultInputDatePattern,
        outputDatePattern: String = defaultOutputDatePattern,
        targetTimeZone: TimeZone = TimeZone.getDefault()
    ): String {
        val dateFormatter = DateTimeFormatter.ofPattern(
            inputPattern, locale
        ).withZone(ZoneId.of(targetTimeZone.id))

        val parsedDate = dateFormatter.parse(originDate)
        val zonedDateTime = ZonedDateTime.from(parsedDate).toOffsetDateTime()

        val outputDateFormat = DateTimeFormatter.ofPattern(
            outputDatePattern, locale
        )
        return zonedDateTime.format(outputDateFormat)
    }

    /**
     * Helper utility for converting dates in string format from one date type to the other
     *
     * @param originDate input date which needs to be converted
     * @param dateTimeFormatter time format representing the [originDate]
     * @param outputDatePattern pattern that will be used create the output,
     * by default this is set to [defaultOutputDatePattern]
     * @param targetTimeZone  time zone that the output will be converted to,
     * by default the timezone is set to whatever the current device is set to
     *
     * @return date string following the output pattern of [outputDatePattern]
     *
     * @see [DateTimeFormatter](https://developer.android.com/reference/java/time/format/DateTimeFormatter)
     */
    fun convertToTimeStamp(
        originDate: String,
        locale: Locale = Locale.getDefault(),
        dateTimeFormatter: DateTimeFormatter,
        outputDatePattern: String = defaultOutputDatePattern,
        targetTimeZone: TimeZone = TimeZone.getDefault()
    ): String {
        val parsedDate = dateTimeFormatter
            .withZone(ZoneId.of(targetTimeZone.id))
            .parse(originDate)

        val zonedDateTime = ZonedDateTime.from(parsedDate).toOffsetDateTime()

        val outputDateFormat = DateTimeFormatter.ofPattern(
            outputDatePattern, locale
        )
        return zonedDateTime.format(outputDateFormat)
    }
}
