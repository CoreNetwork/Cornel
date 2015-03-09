package us.core_network.cornel.strings;

import java.sql.Time;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeFormatterTest
{
    @Test
    public void testFormatting() throws Exception
    {
        TimeFormatter.TimeFormatterProperties properties = new TimeFormatter.TimeFormatterProperties();
        TimeFormatter timeFormatter = new TimeFormatter(properties);

        assertEquals(timeFormatter.format(0), "0 seconds");
        assertEquals(timeFormatter.format(59), "59 seconds");
        assertEquals(timeFormatter.format(60), "1 minute");
        assertEquals(timeFormatter.format(61), "1 minute, 1 second");
        assertEquals(timeFormatter.format(7 * 24 * 60 * 60), "1 week");
        assertEquals(timeFormatter.format(59 * 24 * 60 * 60 + 1), "1 month, 4 weeks, 1 day, 1 second");

        assertEquals(timeFormatter.format(7 * 24 * 60 * 60 + 30 * 24 * 60 * 60), "1 month, 1 week");
        assertEquals(timeFormatter.format(7 * 24 * 60 * 60 + 30 * 24 * 60 * 60 + 1), "1 month, 1 week, 1 second");

        properties.setCommaSeparated(false);
        assertEquals(timeFormatter.format(7 * 24 * 60 * 60 + 30 * 24 * 60 * 60 + 1), "1 month 1 week 1 second");

        properties.setAccuracy(2);
        assertEquals(timeFormatter.format(7 * 24 * 60 * 60 + 30 * 24 * 60 * 60 + 1), "1 month 1 week");
        properties.setAccuracy(1);
        assertEquals(timeFormatter.format(7 * 24 * 60 * 60 + 30 * 24 * 60 * 60 + 1), "1 month");
        assertEquals(timeFormatter.format(59 * 24 * 60 * 60 + 1), "2 months");

        properties.setAccuracy(6);
        properties.setLargestUnit(TimeFormatter.UNIT_WEEKS);
        assertEquals(timeFormatter.format(59 * 24 * 60 * 60 + 1), "8 weeks 3 days 1 second");
    }
}