package net.samagames.hub.time;

/**
 * File:      MoonPhaseCalculator.java
 * Author:    Angus McIntyre <angus@pobox.com>
 * Date:      31.05.96
 * Updated:   01.06.96
 *
 * Java class to calculate the phase of the moon, given a date. The
 *  'moonPhase' method is a Java port of some public domain C source which
 * was apparently originally part of the Decus C distribution (whatever
 * that was). Details of the algorithm are given below.
 *
 *  To use this in a program, create an object of class 'MoonCalculation'.
 *
 *  I'm not convinced that the algorithm is entirely accurate, but I don't
 *  know enough to confirm whether it is or not.
 *
 *  HISTORY
 *  -------
 *
 *  31.05.96    SLAM    Converted from C
 *  01.06.96    SLAM    Added 'phaseName' to return the name of a phase.
 *  Fixed leap year test in 'daysInMonth'.
 *
 *  LEGAL
 *  -----
 *
 *  This software is free. It can be used and modified in any way you
 *  choose. The author assumes no liability for any loss or damage you
 *  may incur through use of or inability to use this software. This
 *  disclaimer must appear on any modified or unmodified version of
 *  the software in which the name of the author also appears.
 *
 */

import java.util.Calendar;

public class MoonPhaseCalculator
{
    private static final int day_year[] = { -1, -1, 30, 58, 89, 119, 150, 180, 211, 241, 272, 303, 333 };

    public static MoonPhase moonPhase(Calendar date)
    {
        return moonPhase(date.get(Calendar.YEAR), getMonthNumber(date.get(Calendar.MONTH)), date.get(Calendar.DAY_OF_MONTH));
    }

    public static MoonPhase moonPhase(int year, int month, int day)
    {
        int phase;
        int cent;
        int epact;
        int diy;
        int golden;

        if (month < 0 || month > 12) month = 0;
        diy = day + day_year[month];

        if ((month > 2) && isLeapYearP(year))
            diy++;

        cent = (year / 100) + 1;
        golden = (year % 19) + 1;

        epact = ((11 * golden) + 20 + (((8 * cent) + 5) / 25) - 5 - (((3 * cent) / 4) - 12)) % 30;

        if (epact <= 0)
            epact += 30;

        if ((epact == 25 && golden > 11) || epact == 24)
            epact++;

        phase = (((((diy + epact) * 6) + 11) % 177) / 22) & 7;

        return MoonPhase.fromPhaseNumber(phase);
    }

    private static int daysInMonth(int month, int year)
    {
        int result = 31;

        switch (month)
        {
            case 4:
            case 6:
            case 9:
            case 11:
                result = 30;
                break;
            case 2:
                result = (isLeapYearP(year) ? 29 : 28);
        }

        return result;
    }

    private static boolean isLeapYearP(int year)
    {
        return ((year % 4 == 0) && ((year % 400 == 0) || (year % 100 != 0)));
    }

    private static int getMonthNumber(int calendarMonth)
    {
        switch(calendarMonth) {
            case Calendar.JANUARY:
                return 1;
            case Calendar.FEBRUARY:
                return 2;
            case Calendar.MARCH:
                return 3;
            case Calendar.APRIL:
                return 4;
            case Calendar.MAY:
                return 5;
            case Calendar.JUNE:
                return 6;
            case Calendar.JULY:
                return 7;
            case Calendar.AUGUST:
                return 8;
            case Calendar.SEPTEMBER:
                return 9;
            case Calendar.OCTOBER:
                return 10;
            case Calendar.NOVEMBER:
                return 11;
            case Calendar.DECEMBER:
                return 12;
            case Calendar.UNDECIMBER:
                return 13;
            default:
                return 0;
        }
    }
}