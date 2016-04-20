package net.samagames.hub.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class NumberUtils
{
    private static final DecimalFormat DECIMAL_FORMAT;

    public static String format(long number)
    {
        return DECIMAL_FORMAT.format(number);
    }

    static
    {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(' ');

        DECIMAL_FORMAT = new DecimalFormat();
        DECIMAL_FORMAT.setDecimalFormatSymbols(decimalFormatSymbols);
        DECIMAL_FORMAT.setGroupingSize(3);
        DECIMAL_FORMAT.setMaximumFractionDigits(64);
    }
}
