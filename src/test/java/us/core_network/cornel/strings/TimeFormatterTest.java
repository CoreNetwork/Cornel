package us.core_network.cornel.strings;

import java.sql.Time;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeFormatterTest
{
    @Test
    public void testFormatting() throws Exception
    {
        String string = "<Months> month<S> <Weeks> week<s> <Days> day<S> <Hours> hour<S> <Minutes> Minutes<S> <Seconds> second<S>";
        TimeFormatter timeFormatter = new TimeFormatter(string, 0);
    }
}