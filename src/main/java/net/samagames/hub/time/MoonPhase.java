package net.samagames.hub.time;

/**
 * Author: AmauryPi
 */
public enum MoonPhase
{
    NEW(4 * 24000l),
    WAXING_CRESCENT(5 * 24000l),
    FIRST_QUARTER(6 * 24000l),
    WAXING_GIBBOUS(7 * 24000l),
    FULL(0l),
    WANING_GIBBOUS(24000l),
    THIRD_QUARTER(2 * 24000l),
    WANING_CRESCENT(3 * 24000l);

    private final long ticksToGetThisPhase;

    MoonPhase(long ticksToGetThisPhase)
    {
        this.ticksToGetThisPhase = ticksToGetThisPhase;
    }

    public long getTicksToGetThisPhase()
    {
        return this.ticksToGetThisPhase;
    }

    public static MoonPhase fromPhaseNumber(int number)
    {
        if(number >= 0 && number <= 7) return MoonPhase.values()[number];
        else                           return MoonPhase.NEW;
    }
}