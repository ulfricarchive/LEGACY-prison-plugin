package com.ulfric.spigot.prison.serializer.util.serializer;

import com.ulfric.verify.Verify;
import org.junit.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.StringJoiner;
import java.util.regex.Pattern;

@RunWith(JUnitPlatform.class)
public class LocationSerializerTest {

    private static final Pattern SEPARATOR = Pattern.compile("#");

    @Test
    public void joinerTest()
    {
        StringJoiner joiner = new StringJoiner(LocationSerializerTest.SEPARATOR.pattern());

        joiner.add("a");
        joiner.add("b");
        joiner.add("c");

        Verify.that(joiner.toString()).isEqualTo("a#b#c");
    }

}
