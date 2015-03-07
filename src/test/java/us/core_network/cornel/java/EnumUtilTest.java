package us.core_network.cornel.java;

import org.junit.Test;

import static org.junit.Assert.*;

public class EnumUtilTest
{


    @Test
    public void testFindEnum() throws Exception
    {
        assertSame(EnumUtil.findEnum(TestEnum.values(), "simple"), TestEnum.SIMPLE);
        assertSame(EnumUtil.findEnum(TestEnum.values(), "Simple"), TestEnum.SIMPLE);
        assertSame(EnumUtil.findEnum(TestEnum.values(), "SIMPLE"), TestEnum.SIMPLE);

        assertSame(EnumUtil.findEnum(TestEnum.values(), "with many spaces"), TestEnum.WITH_MANY_SPACES);
        assertSame(EnumUtil.findEnum(TestEnum.values(), "With Many Spaces"), TestEnum.WITH_MANY_SPACES);
    }

    @Test
    public void testGetPrettyEnumName() throws Exception
    {
        assertEquals(EnumUtil.getPrettyEnumName(TestEnum.SIMPLE), "simple");
        assertEquals(EnumUtil.getPrettyEnumName(TestEnum.WITH_MANY_SPACES), "with many spaces");
    }

    private static enum TestEnum
    {
        SIMPLE,
        WITH_MANY_SPACES;
    }
}