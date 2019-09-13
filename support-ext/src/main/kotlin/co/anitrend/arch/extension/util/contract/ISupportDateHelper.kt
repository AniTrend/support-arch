package co.anitrend.arch.extension.util.contract

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Contract for date helper
 *
 * @since v1.2.0
 */
interface ISupportDateHelper {

    /**
     * [ISO 8601](https://www.iso.org/iso-8601-date-and-time-format.html) date format pattern, this default pattern
     * targets API level [Build.VERSION_CODES.N] and up
     *
     * @see [SimpleDateFormat](https://developer.android.com/reference/java/text/SimpleDateFormat.html)
     * @since 1.2.0
     */
    val defaultInputDatePattern
        @TargetApi(Build.VERSION_CODES.N)
        get() = "yyyy-MM-dd'T'HH:mm:ssXXX"

    /**
     * @see [SimpleDateFormat](https://developer.android.com/reference/java/text/SimpleDateFormat.html)
     */
    val defaultOutputDatePattern
        get() =  "yyyy-MM-dd HH:mm:ss"

    /** [Three Ten Android Backport](https://github.com/JakeWharton/ThreeTenABP)
     * Should handle initialization of ThreeTenABP if you are overriding the interface
     * and wish to make use of the default methods
     *
     * @param context application context
     */
    fun initializeThreeTenBackPort(context: Context)

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
     * @see [SimpleDateFormat](https://developer.android.com/reference/java/text/SimpleDateFormat.html)
     */
    fun convertFromUnixTimeStamp(
        unixTimeStamp: Long,
        outputDatePattern: String = defaultOutputDatePattern,
        targetTimeZone: TimeZone = TimeZone.getDefault()
    ): String {

        val originDate = Date(unixTimeStamp)

        val convertedDate = SimpleDateFormat(
            outputDatePattern,
            Locale.getDefault()
        )

        with (convertedDate) {
            timeZone = targetTimeZone
        }

        return convertedDate.format(originDate)
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
     * @see [SimpleDateFormat](https://developer.android.com/reference/java/text/SimpleDateFormat.html)
     */
    fun convertToUnixTimeStamp(
        originDate: String,
        inputPattern: String = defaultInputDatePattern,
        targetTimeZone: TimeZone = TimeZone.getDefault()
    ): Long? {
        val dateFormatter = SimpleDateFormat(
            inputPattern,
            Locale.getDefault()
        )

        with (dateFormatter) {
            timeZone = targetTimeZone
        }

        val convertedDate = dateFormatter.parse(originDate)

        return convertedDate?.time
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
     * @see [SimpleDateFormat](https://developer.android.com/reference/java/text/SimpleDateFormat.html)
     */
    fun convertToTimeStamp(
        originDate: String,
        inputPattern: String = defaultInputDatePattern,
        outputDatePattern: String = defaultOutputDatePattern,
        targetTimeZone: TimeZone = TimeZone.getDefault()
    ): String? {
        val dateFormatter = DateTimeFormatter.ofPattern(
            inputPattern, Locale.getDefault()
        ).withZone(ZoneId.of(targetTimeZone.id))

        val convertedDate = dateFormatter.parse(originDate)
        val zonedDateTime = ZonedDateTime.from(convertedDate).toOffsetDateTime()

        return convertedDate?.let {
            val outputSampleDateFormat = DateTimeFormatter.ofPattern(
                outputDatePattern, Locale.getDefault()
            )

            zonedDateTime.format(outputSampleDateFormat)
        }
    }
}
