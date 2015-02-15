package us.core_network.cornel.java;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberUtilTest
{

    @Test
    public void testIsInteger() throws Exception
    {
        assertFalse(NumberUtil.isInteger("notNumber"));
        assertFalse(NumberUtil.isInteger("10.2"));
        assertTrue(NumberUtil.isInteger("10"));
    }

    @Test
    public void testIsDouble() throws Exception
    {
        assertFalse(NumberUtil.isDouble("notNumber"));
        assertTrue(NumberUtil.isDouble("10.2"));
        assertTrue(NumberUtil.isDouble("10"));
    }
}