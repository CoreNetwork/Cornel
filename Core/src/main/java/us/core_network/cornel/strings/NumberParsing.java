package us.core_network.cornel.strings;

public class NumberParsing
{
    /**
     * @param text Text to test against.
     * @return <code>true</code> if specified text can be parsed into {@link java.lang.Integer}.
     */
    public static Boolean isInteger(String text)
    {
        try
        {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * @param text Text to test against.
     * @return <code>true</code> if specified text can be parsed into {@link java.lang.Double}.
     */
    public static Boolean isDouble(String text)
    {
        try
        {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }
}
