package com.bradfieldcs.problem4;

import java.time.Duration;

class ExponentialBackoffCalculator {

    private static final double MULTIPLIER_BASE = 2.0;

    private final int count;
    private final Duration initialBackoffPeriod;
    private final JitterCriteria jitterCriteria;

    ExponentialBackoffCalculator(int count,
                                 Duration initialBackoffPeriod,
                                 JitterCriteria jitterCriteria) {
        this.count = count;
        this.initialBackoffPeriod = initialBackoffPeriod;
        this.jitterCriteria = jitterCriteria;
    }

    public long getSleepMilliSeconds() {
        return (long) (Math.pow(MULTIPLIER_BASE, count) * initialBackoffPeriod.toMillis());
    }

    public long getSleepMilliSecondsWithJitter() {
        return getSleepMilliSeconds() + jitterCriteria.getRandomJitterMillis();
    }
}
