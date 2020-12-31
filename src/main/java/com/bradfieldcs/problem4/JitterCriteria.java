package com.bradfieldcs.problem4;

class JitterCriteria {
    private final double minInclusiveSeconds;
    private final double maxExclusiveSeconds;

    JitterCriteria(double minInclusiveSeconds, double maxExclusiveSeconds) {
        this.minInclusiveSeconds = minInclusiveSeconds;
        this.maxExclusiveSeconds = maxExclusiveSeconds;
    }

    public long getRandomJitterMillis() {
        return (long) ((Math.random() * (maxExclusiveSeconds - minInclusiveSeconds)) + minInclusiveSeconds) * 1000;
    }
}
