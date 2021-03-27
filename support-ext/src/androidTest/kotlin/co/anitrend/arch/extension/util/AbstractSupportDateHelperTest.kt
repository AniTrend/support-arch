package co.anitrend.arch.extension.util

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.arch.extension.util.date.SupportDateHelper
import com.jakewharton.threetenabp.AndroidThreeTen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class AbstractSupportDateHelperTest {

    private val dateHelper : AbstractSupportDateHelper = SupportDateHelper()

    private val context by lazy {
        InstrumentationRegistry.getInstrumentation().context
    }

    @Before
    fun setUp() {
        AndroidThreeTen.init(context)
    }

    @Test
    fun date_conversion_from_iso_8601_to_short_date_timestamp() {
        val targetTimeZone = "GMT+2"
        val input = "2019-01-09T07:30:00-08:00"

        val actual = dateHelper.convertToTimeStamp(
            originDate = input,
            inputPattern = "yyyy-MM-dd'T'HH:mm:ssXXX",
            outputDatePattern = "yyyy-MM-dd HH:mm:ss",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = "2019-01-09 17:30:00"

        assertEquals(expected, actual)
    }

    @Test
    fun date_conversion_from_iso_RCF_822_to_short_date_timestamp() {
        val targetTimeZone = "GMT+2"
        val input = "2019-01-09T07:30:00-0800"

        val actual = dateHelper.convertToTimeStamp(
            originDate = input,
            inputPattern = "yyyy-MM-dd'T'HH:mm:ssZ",
            outputDatePattern = "yyyy-MM-dd HH:mm:ss",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = "2019-01-09 17:30:00"

        assertEquals(expected, actual)
    }
    
    @Test
    fun date_conversion_from_RFC_822_format_to_short_date_timestamp() {
        val targetTimeZone = "GMT+2"
        val input = "Sat, 22 Jun 2019 15:00:00 GMT"

        val actual = dateHelper.convertToTimeStamp(
            originDate = input,
            locale = Locale.US,
            dateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME,
            //inputPattern = "EEE, dd MMM yyyy HH:mm:ss z",
            outputDatePattern = "yyyy-MM-dd HH:mm:ss",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = "2019-06-22 17:00:00"

        assertEquals(expected, actual)
    }

    @Test
    fun date_conversion_from_RFC_822_format_to_unix_timestamp() {
        val targetTimeZone = "GMT+2"
        val input = "Sat, 22 Jun 2019 15:00:00 GMT"

        val actual = dateHelper.convertToUnixTimeStamp(
            originDate = input,
            locale = Locale.US,
            dateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME,
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = 1561215600000

        assertEquals(expected, actual)
    }

    @Test
    fun date_conversion_from_iso_8601_to_unix_timestamp() {
        val targetTimeZone = "GMT+2"
        val input = "2019-01-09T07:30:00-08:00"

        val actual = dateHelper.convertToUnixTimeStamp(
            originDate = input,
            inputPattern = "yyyy-MM-dd'T'HH:mm:ssXXX",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = 1547047800000

        assertEquals(expected, actual)
    }

    @Test
    fun date_conversion_from_iso_RCF_822_to_unix_timestamp() {
        val targetTimeZone = "GMT+2"
        val input = "2019-01-09T07:30:00-0800"

        val actual = dateHelper.convertToUnixTimeStamp(
            originDate = input,
            inputPattern = "yyyy-MM-dd'T'HH:mm:ssZ",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = 1547047800000

        assertEquals(expected, actual)
    }

    @Test
    fun unix_timestamp_conversion_from_iso_8601() {
        val targetTimeZone = "GMT+2"
        val input = 1561215600000

        val actual = dateHelper.convertFromUnixTimeStamp(
            unixTimeStamp = input,
            outputDatePattern = "yyyy-MM-dd HH:mm:ss",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = "2019-06-22 17:00:00"

        assertEquals(expected, actual)
    }
}