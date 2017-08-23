package net.samagames.hub.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
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
