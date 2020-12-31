package com.bradfieldcs.problem2;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RangeCountUtilityTest {

    @Test
    public void givenRangeAndValue_getCountLinearly_returnsCorrectly() {
        List<Integer> array = List.of(1, 1, 2, 4, 5, 5, 7, 9);
        int value = 5;

        MatcherAssert.assertThat(
            RangeCountUtility.getCountLinearly(array, value),
            equalTo(2)
        );
    }

    @Test
    public void givenRangeAndValue_getCountViaBinarySearch_returnsCorrectly() {
        List<Integer> array = List.of(1, 1, 2, 4, 5, 5, 7, 9);
        int value = 5;

        assertThat(
            RangeCountUtility.getCountByBinarySearch(array, value),
            equalTo(2)
        );

    }

    @Test
    public void testBenchmarks() {
        RangeCountUtility.logBenchmarkedResults(10);
        RangeCountUtility.logBenchmarkedResults(100);
        RangeCountUtility.logBenchmarkedResults(1000);
        RangeCountUtility.logBenchmarkedResults(10_000);
        RangeCountUtility.logBenchmarkedResults(100_000);
        RangeCountUtility.logBenchmarkedResults(1_000_000);
        RangeCountUtility.logBenchmarkedResults(10_000_000);
    }
}
