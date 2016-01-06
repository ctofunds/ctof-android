package com.ctofunds.android.utility;

import android.content.res.Resources;

import com.ctofunds.android.R;

/**
 * Created by qianhao.zhou on 1/6/16.
 */
public final class TimeUtils {

    private final String[] timeUnit;
    private final int[] timeUnitInterval;

    public TimeUtils(Resources res) {
        timeUnit = res.getStringArray(R.array.time_unit);
        timeUnitInterval = res.getIntArray(R.array.time_unit_interval);
    }

    public final String toString(long time) {
        int unitIndex = 0;
        while (unitIndex < timeUnitInterval.length) {
            if (time >= timeUnitInterval[unitIndex]) {
                time = time / timeUnitInterval[unitIndex];
                unitIndex++;
            } else {
                break;
            }
        }
        return time + timeUnit[unitIndex];
    }
}
