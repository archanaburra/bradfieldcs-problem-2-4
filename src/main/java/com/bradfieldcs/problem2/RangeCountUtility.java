package com.bradfieldcs.problem2;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class RangeCountUtility {

    private static final Logger LOG = Logger.getLogger(RangeCountUtility.class.getName());

    private RangeCountUtility() {
        // utility class
    }

    public static void logBenchmarkedResults(int sizeOfArray) {
        List<Integer> sortedList = generateRandomSortedList(sizeOfArray);
        int valueToSearchFor = new Random().nextInt(sizeOfArray);

        // A monotonic clock, so good for measuring duration
        Instant startLinear = Instant.now();
        int linearCount = getCountLinearly(sortedList, valueToSearchFor);
        Instant endLinear = Instant.now();

        LOG.info(String.format(
            "Found %s counts of %s using linear search in %s nanoseconds with size of array %s",
            linearCount,
            valueToSearchFor,
            Duration.between(startLinear, endLinear).toNanos(),
            sizeOfArray
        ));

        Instant startBinary = Instant.now();
        int binaryCount = getCountByBinarySearch(sortedList, valueToSearchFor);
        Instant endBinary = Instant.now();

        LOG.info(String.format(
            "Found %s counts of %s using binary search in %s nanoseconds with size of array %s",
            binaryCount,
            valueToSearchFor,
            Duration.between(startBinary, endBinary).toNanos(),
            sizeOfArray
        ));
    }

    // Best case: O(1)
    // Average case: O(n/2)
    // Worst case: O(n)
    public static int getCountLinearly(List<Integer> sortedArray, int value) {

        int count = 0;
        for (int entry : sortedArray) {
            if (entry == value) {
                count++;
            } else if (entry > value) {
                break;
            }
        }

        return count;
    }

    // Best case: O(1)
    // Average/Worst case: O(log(n))
    public static int getCountByBinarySearch(List<Integer> sortedArray, int valueToSearch) {
        return getCountByBinarySearch(sortedArray, valueToSearch, 0, sortedArray.size() - 1);
    }

    private static int getCountByBinarySearch(List<Integer> sortedArray,
                                              int valueToSearch,
                                              int minIndex,
                                              int maxIndex) {

        if (minIndex > maxIndex) {
            // Not present in list
            return 0;
        }

        int midpoint = (maxIndex + minIndex) / 2;

        if (sortedArray.get(midpoint) == valueToSearch) {
            // Found it! Now let's count all the neighbors
            int count = 1;
            int belowMidpoint = midpoint - 1;
            while (sortedArray.get(belowMidpoint) == valueToSearch) {
                count++;
                belowMidpoint--;
            }

            int aboveMidpoint = midpoint + 1;
            while (sortedArray.get(aboveMidpoint) == valueToSearch) {
                count++;
                aboveMidpoint++;
            }

            return count;
        } else if (valueToSearch < sortedArray.get(midpoint)) {
            // Need to go lower in the list recursively
            return getCountByBinarySearch(sortedArray, valueToSearch, minIndex, midpoint - 1);
        } else {
            // Need to go higher in the list recursively
            return getCountByBinarySearch(sortedArray, valueToSearch, midpoint + 1, maxIndex);
        }
    }

    private static List<Integer> generateRandomSortedList(int sizeOfArray) {
        ArrayList<Integer> result = new ArrayList<>(sizeOfArray);
        Random random = new Random();
        int startingValue = random.nextInt(3);

        for (int i = 0; i < sizeOfArray; i++) {
            int newValue = startingValue + random.nextInt(2);
            result.add(i, newValue);
            startingValue = newValue;
        }

        return result;
    }
}
