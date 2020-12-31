package com.bradfieldcs.problem4;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

class ExponentialBackoffServiceCalculatorTest {

    @Test
    public void givenZeroCount_getSleepMilliSeconds_returnsCorrectly() {
        ExponentialBackoffCalculator calculator = new ExponentialBackoffCalculator(
            0,
            Duration.ofSeconds(1),
            new JitterCriteria(0, 50)
        );

        assertThat(calculator.getSleepMilliSeconds(), equalTo(1000L));
        long jitterMillis = calculator.getSleepMilliSecondsWithJitter();
        assertThat(jitterMillis, greaterThanOrEqualTo(1000L));
        assertThat(jitterMillis, lessThan(50_000L));
    }

    @Test
    public void givenNonZeroCount_getSleepMilliSeconds_returnsCorrectly() {
        ExponentialBackoffCalculator calculator = new ExponentialBackoffCalculator(
            3,
            Duration.ofSeconds(1),
            new JitterCriteria(0, 50)
        );

        assertThat(calculator.getSleepMilliSeconds(), equalTo(8000L));
        long jitterMillis = calculator.getSleepMilliSecondsWithJitter();
        assertThat(jitterMillis, greaterThanOrEqualTo(8000L));
        assertThat(jitterMillis, lessThan(58_000L));
    }
}
