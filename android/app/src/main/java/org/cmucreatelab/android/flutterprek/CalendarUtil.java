package org.cmucreatelab.android.flutterprek;

import android.util.Log;

import java.util.Calendar;

public class CalendarUtil {

    public static final class BetweenRange {
        public final long from;
        public final long to;

        public BetweenRange(long from, long to) {
            this.from = from;
            this.to = to;
        }
    }


    public static Calendar[] generateWeekFromDay(Calendar calendar) {
        // specify a 7-day week as the resulting array
        final int numberOfDays = 7;
        Calendar[] result = new Calendar[numberOfDays];

        // avoid manipulating parameter
        Calendar startOfWeek = (Calendar) calendar.clone();

        // find the offset from current day and set startOfWeek to point to Monday
        int dowOffset = getDayOfWeekOffset(calendar);
        startOfWeek.add(Calendar.DAY_OF_MONTH, -dowOffset);

        // populate array with numberOfDays, starting on Monday
        for (int i=0; i<numberOfDays; i++) {
            result[i] = (Calendar) startOfWeek.clone();
            startOfWeek.add(Calendar.DAY_OF_MONTH, 1);
        }

        return result;
    }


    public static BetweenRange generateBetweenRangeOfPastSevenDays(Calendar calendar) {
        long from, to;

        to = calendar.getTimeInMillis();

        // 7 days ago
        calendar.add(Calendar.DAY_OF_YEAR, -7);

        // at midnight
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        from = calendar.getTimeInMillis();

        return new BetweenRange(from, to);
    }


    public static int getDayOfWeekOffset(Calendar calendar) {
        // find the offset from current day (with Monday as start of week)
        int dowOffset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        if (dowOffset < 0) dowOffset += 7;
        return dowOffset;
    }

//    private void foo(Calendar calendar) {
//        Calendar[] result = new Calendar[7];
//        Calendar startOfWeek = (Calendar) calendar.clone();
//        int dowOffset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
//        if (dowOffset < 0) dowOffset += 7;
//        startOfWeek.add(Calendar.DAY_OF_MONTH, -dowOffset);
//        for (int i=0; i<7; i++) {
//            result[i] = (Calendar) startOfWeek.clone();
//            startOfWeek.add(Calendar.DAY_OF_MONTH, 1);
//        }
//        Log.v(Constants.LOG_TAG, "successfully constructed Calendar array.");
//    }

}
