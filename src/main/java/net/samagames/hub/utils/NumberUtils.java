package net.samagames.hub.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class NumberUtils
{
    private static final DecimalFormat decimalFormat;

    static
    {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(' ');

        decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat.setGroupingSize(3);
        decimalFormat.setMaximumFractionDigits(64);
    }

    public static String format(long number)
    {
        return decimalFormat.format(number);
    }
}
