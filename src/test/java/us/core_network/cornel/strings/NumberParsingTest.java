package us.core_network.cornel.strings;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberParsingTest
{

    @Test
    public void testIsInteger() throws Exception
    {
        assertFalse(NumberParsing.isInteger("notNumber"));
        assertFalse(NumberParsing.isInteger("10.2"));
        assertTrue(NumberParsing.isInteger("10"));
    }

    @Test
    public void testIsDouble() throws Exception
    {
        assertFalse(NumberParsing.isDouble("notNumber"));
        assertTrue(NumberParsing.isDouble("10.2"));
        assertTrue(NumberParsing.isDouble("10"));
    }
}