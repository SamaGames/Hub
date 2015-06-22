package net.samagames.hub.time;

import net.samagames.hub.Hub;
import org.bukkit.World;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Author: AmauryPi
 */
public class UpdateTimeTask implements Runnable
{
    private final double secondsIn24Hours = 86400D;
    private final double ticksPerMinecraftDay = 24000D;

    private Calendar thisDay6AM;
    private TimeZone timezone;
    private World world;
    private MoonPhase currentPhase;
    private int currentDayOfYear;

    public UpdateTimeTask()
    {
        this.world = Hub.getInstance().getHubWorld();
        this.timezone = TimeZone.getTimeZone("Europe/Paris");

        Calendar now = Calendar.getInstance(this.timezone);

        this.thisDay6AM = Calendar.getInstance(this.timezone);
        this.thisDay6AM.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
        this.currentPhase = MoonPhaseCalculator.moonPhase(now);
        this.currentDayOfYear = now.get(Calendar.DAY_OF_YEAR);
    }


    @Override
    public void run()
    {
        Calendar now = Calendar.getInstance(this.timezone);

        if(now.get(Calendar.DAY_OF_YEAR) != this.currentDayOfYear)
        {
            this.currentPhase = MoonPhaseCalculator.moonPhase(now);
            this.currentDayOfYear = now.get(Calendar.DAY_OF_YEAR);
        }

        double secondsSince6AMThisDay = (now.getTimeInMillis() - thisDay6AM.getTimeInMillis()) / 1000.0;

        long inGameTime = (long) (((secondsSince6AMThisDay / secondsIn24Hours) * ticksPerMinecraftDay) % ticksPerMinecraftDay);
        if(inGameTime < 0) inGameTime += (long) ticksPerMinecraftDay;

        inGameTime += this.currentPhase.getTicksToGetThisPhase();

        this.world.setFullTime(inGameTime);
    }
}