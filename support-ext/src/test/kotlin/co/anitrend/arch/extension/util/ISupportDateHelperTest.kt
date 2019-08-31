package co.anitrend.arch.extension.util

import co.anitrend.arch.extension.util.contract.ISupportDateHelper
import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ISupportDateHelperTest {

    private val supportDateHelper : ISupportDateHelper by lazy {
        SupportDateHelper()
    }

    /**
     * Skipping these tests for now due to
     * [Simple Date Error](https://stackoverflow.com/questions/44588429/unknown-pattern-character-x-when-using-simpledateformat/44588864)
     */
    fun `date conversion from iso 8601 to short date timestamp`() {
        val targetTimeZone = "GMT+2"
        val input = "2019-01-09T07:30:00-08:00"

        val actual = supportDateHelper.convertToTimeStamp(
            originDate = input,
            inputPattern = "yyyy-MM-dd'T'HH:mm:ssXXX",
            outputDatePattern = "yyyy-MM-dd HH:mm:ss",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = "2019-01-09 17:30:00"

        Assert.assertEquals(expected, actual)
    }


    /**
     * Skipping these tests for now due to
     * [Simple Date Error](https://stackoverflow.com/questions/44588429/unknown-pattern-character-x-when-using-simpledateformat/44588864)
     */
    fun `date conversion from iso RCF 822 to short date timestamp`() {
        val targetTimeZone = "GMT+2"
        val input = "2019-01-09T07:30:00-0800"

        val actual = supportDateHelper.convertToTimeStamp(
            originDate = input,
            inputPattern = "yyyy-MM-dd'T'HH:mm:ssZ",
            outputDatePattern = "yyyy-MM-dd HH:mm:ss",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = "2019-01-09 17:30:00"

        Assert.assertEquals(expected, actual)
    }


    /**
     * Skipping these tests for now due to
     * [Simple Date Error](https://stackoverflow.com/questions/44588429/unknown-pattern-character-x-when-using-simpledateformat/44588864)
     */
    fun `date conversion from RFC 822 format to short date timestamp`() {
        val targetTimeZone = "GMT+2"
        val input = "Sat, 22 Jun 2019 15:00:00 GMT"

        val actual = supportDateHelper.convertToTimeStamp(
            originDate = input,
            inputPattern = "EEE, dd MMM yyyy HH:mm:ss ZZZ",
            outputDatePattern = "yyyy-MM-dd HH:mm:ss",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = "2019-06-22 17:00:00"

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `date conversion from RFC 822 format to unix timestamp`() {
        val targetTimeZone = "GMT+2"
        val input = "Sat, 22 Jun 2019 15:00:00 GMT"

        val actual = supportDateHelper.convertToUnixTimeStamp(
            originDate = input,
            inputPattern = "EEE, dd MMM yyyy HH:mm:ss ZZZ",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = 1561215600000

        Assert.assertEquals(expected, actual)
    }

    /**
     * Skipping these tests for now due to
     * [Simple Date Error](https://stackoverflow.com/questions/44588429/unknown-pattern-character-x-when-using-simpledateformat/44588864)
     */
    fun `date conversion from iso 8601 to unix timestamp`() {
        val targetTimeZone = "GMT+2"
        val input = "2019-01-09T07:30:00-08:00"

        val actual = supportDateHelper.convertToUnixTimeStamp(
            originDate = input,
            inputPattern = "yyyy-MM-dd'T'HH:mm:ssXXX",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = 1547047800000

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `date conversion from iso RCF 822 to unix timestamp`() {
        val targetTimeZone = "GMT+2"
        val input = "2019-01-09T07:30:00-0800"

        val actual = supportDateHelper.convertToUnixTimeStamp(
            originDate = input,
            inputPattern = "yyyy-MM-dd'T'HH:mm:ssZ",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = 1547047800000

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `unix timestamp conversion from iso 8601`() {
        val targetTimeZone = "GMT+2"
        val input = 1561215600000

        val actual = supportDateHelper.convertFromUnixTimeStamp(
            unixTimeStamp = input,
            outputDatePattern = "yyyy-MM-dd HH:mm:ss",
            targetTimeZone = TimeZone.getTimeZone(targetTimeZone)
        )

        val expected = "2019-06-22 17:00:00"

        Assert.assertEquals(expected, actual)
    }
}