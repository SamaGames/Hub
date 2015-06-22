package net.samagames.hub.time;

import com.maxmind.geoip2.model.CityResponse;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

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
    private UUID playerUUID;
    private int currentDayOfYear;

    public UpdateTimeTask(Player player) throws Exception
    {
        this.playerUUID = player.getUniqueId();
        this.world = Hub.getInstance().getHubWorld();

        String ip = SamaGamesAPI.get().getProxyDataManager().getProxiedPlayer(player.getUniqueId()).getIp();
        InetAddress ipAddress = InetAddress.getByName(ip);

        CityResponse response = Hub.getInstance().getTimeManager().getDatabaseReader().city(ipAddress);

        this.timezone = TimeZone.getTimeZone(response.getLocation().getTimeZone());
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

        Bukkit.getPlayer(this.playerUUID).setPlayerTime(inGameTime, false);
    }
}