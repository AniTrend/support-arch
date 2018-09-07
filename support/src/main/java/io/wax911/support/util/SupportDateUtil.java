package io.wax911.support.util;

import com.annimon.stream.Collectors;
import com.annimon.stream.IntStream;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.wax911.support.base.attribute.SeasonType;
import io.wax911.support.base.attribute.TimeTargetType;

public class SupportDateUtil {

    private static final String seasons[] = {
            SeasonType.WINTER, SeasonType.WINTER,
            SeasonType.SPRING, SeasonType.SPRING, SeasonType.SPRING,
            SeasonType.SUMMER, SeasonType.SUMMER, SeasonType.SUMMER,
            SeasonType.FALL, SeasonType.FALL, SeasonType.FALL,
            SeasonType.WINTER
    };

    /**
     * Gets current season title
     * <br/>
     *
     * @return Season name
     */
    public static @SeasonType String getCurrentSeason(){
        int month = Calendar.getInstance().get(Calendar.MONTH);
        return SeasonType.Seasons[month];
    }

    /**
     * Gets the current season title for menu
     * <br/>
     *
     * @return Season name
     */
    public static int getMenuSelect(){
        String value = seasons[Calendar.getInstance().get(Calendar.MONTH)];
        return SupportUtil.constructListFrom(SeasonType.Seasons).indexOf(value);
    }

    /**
     * Gets the current year + delta, if the season for the year is winter later in the year
     * then the result would be the current year plus the delta
     * <br/>
     *
     * @return current year with a given delta
     */
    public static int getCurrentYear(int delta){
        if(Calendar.getInstance().get(Calendar.MONTH) >= 11 && getCurrentSeason().equals(SeasonType.WINTER))
            return Calendar.getInstance().get(Calendar.YEAR) + delta;
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * Converts unix time representation into current readable time
     * <br/>
     *
     * @return A time format of dd MMM yyyy
     */
    public static @Nullable String convertDate(long value) {
        try {
            if(value != 0)
                return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(value*1000L));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a range of years from the given begin year to the end delta
     * @param start Starting year
     * @param endDelta End difference plus or minus the current year
     */
    public static List<Integer> getYearRanges(int start, int endDelta) {
        List<Integer> yearRanges = IntStream.rangeClosed(start, getCurrentYear(0) + endDelta)
                .boxed().collect(Collectors.toList());
        return yearRanges;
    }

    /**
     * Returns the current month
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * Returns the current date
     */
    public static int getDate() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * Checks if the time given has a difference greater than or equal to the target time
     * <br/>
     * @param conversionTarget type of comparison between the epoch time and target
     * @param epochTime time to compare against the current system clock
     * @param target unit to compare against
     */
    public static boolean timeDifferenceSatisfied(@TimeTargetType int conversionTarget, long epochTime, int target) {
        long currentTime = System.currentTimeMillis();
        TimeUnit defaultSystemUnit = TimeUnit.MILLISECONDS;
        switch (conversionTarget) {
            case TimeTargetType.TIME_UNIT_DAYS:
                return defaultSystemUnit.toDays(currentTime - epochTime) >= target;
            case TimeTargetType.TIME_UNIT_HOURS:
                return defaultSystemUnit.toHours(currentTime - epochTime) >= target;
            case TimeTargetType.TIME_UNIT_MINUTES:
                return defaultSystemUnit.toMinutes(currentTime - epochTime) >= target;
            case TimeTargetType.TIME_UNITS_SECONDS:
                return defaultSystemUnit.toSeconds(currentTime - epochTime) >= target;
        }
        return false;
    }
}
