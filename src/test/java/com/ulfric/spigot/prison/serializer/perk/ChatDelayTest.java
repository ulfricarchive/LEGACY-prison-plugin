package com.ulfric.spigot.prison.serializer.perk;

import com.ulfric.verify.Verify;
import org.junit.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RunWith(JUnitPlatform.class)
public class ChatDelayTest {

    private static final long DELAY_MILLIS = 3000;

    @Test
    public void delayTest()
    {
        Instant current = Instant.now();

        Instant no = Instant.now().minusMillis(2000);
        Instant yes = Instant.now().minusMillis(4000);

        Verify.that(ChronoUnit.MILLIS.between(no, current) > ChatDelayTest.DELAY_MILLIS).isFalse();
        Verify.that(ChronoUnit.MILLIS.between(yes, current) > ChatDelayTest.DELAY_MILLIS).isTrue();
    }

}
