package net.mcskirmish.event.update;

import net.mcskirmish.util.UtilTime;

public enum UpdateType {

    TEN_MIN(600000L),
    FIVE_MIN(300000L),
    THREE_MIN(180000L),
    TWO_MIN(120000L),
    MIN(60000L),
    TEN_SEC(10000L),
    FIVE_SEC(5000L),
    TWO_SEC(2000L),
    SEC(1000L),
    HALF_SEC(500L),
    QUARTER_SEC(250L),
    TWO_TICK(100L),
    TICK(49L);

    private final long periodMillis;
    private long lastUpdate = System.currentTimeMillis();

    UpdateType(long periodMillis) {
        this.periodMillis = periodMillis;
    }

    public boolean hasElapsed() {
        if (UtilTime.elapsed(lastUpdate, periodMillis)) {
            this.lastUpdate = System.currentTimeMillis();
            return true;
        }

        return false;
    }

    public long getPeriodMillis() {
        return periodMillis;
    }

}