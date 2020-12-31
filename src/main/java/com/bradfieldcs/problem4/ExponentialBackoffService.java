package com.bradfieldcs.problem4;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExponentialBackoffService {

    private static final Logger LOG = Logger.getLogger(ExponentialBackoffService.class.getName());

    private static final int COUNT_MAX = 25;
    private static final int MINUTES_MAX = 2;
    private static final int SUCCESS_CHANGE_RANDOMIZER = 3; // There's 1/3 chance that this operation succeeds

    public void runExponentialBackoff() {
        Runnable fallibleOperation = new Runnable() {
            @Override
            public void run() {
                if (new Random().nextInt(SUCCESS_CHANGE_RANDOMIZER) != 0) {
                    throw new IllegalStateException("Could not complete operation!");
                }
            }
        };

        BiPredicate<Instant, Integer> endCriteria = (startTime, count) -> {
            long minutesThusFar = Duration.between(startTime, Instant.now()).toMinutes();
            if (minutesThusFar > MINUTES_MAX) {
                LOG.log(Level.WARNING, "Aborting. Operation has taken this many minutes: " + minutesThusFar);
                return true;
            }

            if (count > COUNT_MAX) {
                LOG.log(Level.WARNING, "Aborting. Operation has taken this many iterations: " + count);
                return true;
            }

            return false;
        };

        try {
            exponentialBackoff(fallibleOperation, Duration.ofSeconds(1), endCriteria, new JitterCriteria(-.5, .5));
        } catch (InterruptedException e) {
            throw new RuntimeException(
                "Thread interrupted, could not finish operation, what a shame!",
                e
            );
        }
    }

    public void exponentialBackoff(Runnable fallibleOperation,
                                   Duration backoffPeriod,
                                   BiPredicate<Instant, Integer> endCriteria,
                                   JitterCriteria jitterCriteria) throws InterruptedException {
        Instant startTime = Instant.now();
        int count = 0;

        while (true) {
            try {
                fallibleOperation.run();
                LOG.log(Level.INFO, "Success! Completed operation!");
                break;
            } catch (Exception e) {
                if (endCriteria.test(startTime, count)) {
                    LOG.log(Level.SEVERE, "We ran out of tries! We will not retry program, consider it failed.");
                } else {
                    LOG.log(Level.WARNING, "Failed! Going to retry in: ");
                    Thread.sleep(new ExponentialBackoffCalculator(count, backoffPeriod, jitterCriteria).getSleepMilliSecondsWithJitter());
                }
            }
            count++;
        }
    }
}
