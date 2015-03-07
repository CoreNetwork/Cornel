package us.core_network.cornel.strings;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeFormatter
{
    public static final int UNIT_SECONDS = 0;
    public static final int UNIT_MINUTES = 1;
    public static final int UNIT_HOURS = 2;
    public static final int UNIT_DAYS = 3;
    public static final int UNIT_WEEKS = 4;
    public static final int UNIT_MONTHS = 5;

    private static final int[] PER_SECOND = { 1, 60, 60 * 60, 60 * 60 * 24, 60 * 60 * 24 * 7, 60 * 60 * 24 * 30 };

    private TimeFormatterProperties properties;

    /**
     * @param properties Properties that shape the output of this TimeFormatter.
     */
    public TimeFormatter(TimeFormatterProperties properties)
    {
        this.properties = properties;
    }

    public String format(int seconds)
    {
        StringBuilder finalStringBuilder = new StringBuilder();

        int usedUnits = 0;
        for (int i = properties.largestUnit; i >= 0; i--)
        {
            double dividedAmount = (double) seconds / PER_SECOND[i];
            int roundedAmount;
            if (usedUnits == properties.accuracy - 1) //If unit is last one displayed, we need to round it to achieve biggest possible accuracy.
                roundedAmount = (int) Math.round(dividedAmount);
            else
                roundedAmount = (int) Math.floor(dividedAmount);

            if (roundedAmount == 0 && (i > 0 || usedUnits != 0)) //If we get to the end, still display 0 seconds.
                continue;

            usedUnits++;

            String text = properties.unitFormatStrings[i];
            text = text.replace("<Value>", Integer.toString(roundedAmount));
            if (roundedAmount == 1)
                text = text.replace("<S>", "");
            else
                text = text.replace("<S>", "s");

            finalStringBuilder.append(text);

            if (usedUnits == properties.accuracy)
            {
                break;
            }
            else
            {
                seconds = seconds % PER_SECOND[i];
                if (seconds == 0)
                    continue;

                if (properties.commaSeparated)
                    finalStringBuilder.append(", ");
                else
                    finalStringBuilder.append(' ');
            }
        }

        return finalStringBuilder.toString();
    }

    /**
     * Assign different set of properties to this TimeFormatter.
     * @param properties New properties.
     */
    public void setProperties(TimeFormatterProperties properties)
    {
        this.properties = properties;
    }

    public String formatDate(long cas)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(cas * 1000);
		int dan = c.get(Calendar.DAY_OF_MONTH);

		SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");

		return dan + getDayOfMonthSuffix(dan) + " " + monthFormat.format(new Date(cas * 1000));

	}

	private String getDayOfMonthSuffix(final int n) {
	    if (n >= 11 && n <= 13) {
	        return "th";
	    }
	    switch (n % 10) {
	        case 1:  return "st";
	        case 2:  return "nd";
	        case 3:  return "rd";
	        default: return "th";
	    }
	}

    public static class TimeFormatterProperties
    {
        int accuracy = 5;
        int largestUnit = UNIT_MONTHS;
        boolean commaSeparated = true;
        String[] unitFormatStrings = { "<Value> second<S>", "<Value> minute<S>", "<Value> hour<S>", "<Value> day<S>", "<Value> week<S>", "<Value> month<S>"};

        TimeFormatterProperties()
        {
        }

        /**
         * @param accuracy Accuracy of the TimeFormatter, meaning how many units will TimeFormatter generate. For example if your time is in months and this number is set to <code>2</code>, it will only display months and weeks. Set it to <code>6</code> to use maximum possible accuracy (there are 6 units from months to seconds).
         */
        public void setAccuracy(int accuracy)
        {
            if (largestUnit < 0 || largestUnit > 6)
                throw new IllegalArgumentException("Invalid accuracy. Must be between 1 and 6.");

            this.accuracy = accuracy;
        }

        /**
         * Set largest displayed unit. Formatter won't display any unit larger than this.
         * For example if yor largest unit is hours and your time input lasts 3 days, formatter will display <code>72 hours</code> because hours are largest allowed unit.
         * @param largestUnit Unit ID, use <code>UNIT_</code> constants in {@link us.core_network.cornel.strings.TimeFormatter}.
         */
        public void setLargestUnit(int largestUnit)
        {
            if (largestUnit < 0 || largestUnit > 5)
                throw new IllegalArgumentException("Unknown unit type");

            this.largestUnit = largestUnit;
        }

        /**
         * @param commaSeparated <code>true</code> if units should be comma separated, otherwise they will be space separated.
         */
        public void setCommaSeparated(boolean commaSeparated)
        {
            this.commaSeparated = commaSeparated;
        }

        /**
         * Set format string for specific unit.
         * @param unit Unit ID, use <code>UNIT_</code> constants in {@link us.core_network.cornel.strings.TimeFormatter}.
         * @param formatString Format string for this unit. Can contain <code>&lt;Value&gt;</code> (which will be replaced by number of these units) and <code>&lt;S&gt;</code> (where letter <code>s</code> will be inserted if number is plural). This string should end with a space.
         */
        public void setUnitFormatString(int unit, String formatString)
        {
            if (unit < 0 || unit > 5)
                throw new IllegalArgumentException("Unknown unit type");

            this.unitFormatStrings[unit] = formatString;
        }
    }
}
