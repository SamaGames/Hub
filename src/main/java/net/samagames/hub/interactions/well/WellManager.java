package net.samagames.hub.interactions.well;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.pearls.CraftingPearl;
import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 27/10/2016
 */
public class WellManager extends AbstractInteractionManager<Well> implements Listener
{
    private final ScheduledFuture craftingCheckTask;

    public WellManager(Hub hub)
    {
        super(hub, "well");

        this.hub.getServer().getPluginManager().registerEvents(this, this.hub);

        this.craftingCheckTask = this.hub.getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            for (Player player : this.hub.getServer().getOnlinePlayers())
            {
                this.checkCrafts(player, false);

                if (this.hub.getGuiManager().getPlayerGui(player) != null && this.hub.getGuiManager().getPlayerGui(player) instanceof GuiWell)
                    this.hub.getGuiManager().getPlayerGui(player).update(player);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable()
    {
        super.onDisable();

        this.craftingCheckTask.cancel(true);
    }

    @Override
    public void onLogin(Player player)
    {
        super.onLogin(player);

        this.checkCrafts(player, true);
        this.interactions.forEach(well -> well.onLogin(player));
    }

    @Override
    public void onLogout(Player player)
    {
        super.onLogout(player);

        this.interactions.forEach(well -> well.onLogout(player));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CAULDRON)
        {
            Optional<Well> optional = this.interactions.stream().filter(well -> normalize(well.getCauldronLocation()).equals(normalize(event.getClickedBlock().getLocation()))).findAny();

            if (optional.isPresent())
                optional.get().play(event.getPlayer());
        }
    }

    @Override
    public void loadConfiguration(JsonArray rootJson)
    {
        if (rootJson.size() == 0)
            return;

        for (int i = 0; i < rootJson.size(); i++)
        {
            JsonObject wellJson = rootJson.get(i).getAsJsonObject();

            Location cauldronLocation = LocationUtils.str2loc(wellJson.get("cauldron").getAsString());
            Location standsLocation = LocationUtils.str2loc(wellJson.get("stands").getAsString());

            Well well = new Well(this.hub, cauldronLocation, standsLocation);

            this.interactions.add(well);
            this.log(Level.INFO, "Registered Well at '" + wellJson.get("cauldron").getAsString());
        }
    }

    public void startPearlCrafting(Player player, int[] numbers)
    {
        int[] expectedNumbers = this.generateRandomNumbers();
        int pearlStars = 1;
        int craftingTime;

        if (Arrays.equals(numbers, new int[] {24, 6, 20, 14})) // 24 June 2014
        {
            pearlStars = 5;
            craftingTime = 1;
        }
        else
        {
            int differenceSum = 0;

            for (int i = 0; i < numbers.length; i++)
                differenceSum += Math.abs(numbers[i] - expectedNumbers[i]);

            if (differenceSum < 2)
                pearlStars = 5;
            else if (differenceSum < 6)
                pearlStars = 4;
            else if (differenceSum < 12)
                pearlStars = 3;
            else if (differenceSum < 18)
                pearlStars = 2;

            long groupId = SamaGamesAPI.get().getPermissionsManager().getPlayer(player.getUniqueId()).getGroupId();

            if (groupId > 3)
                groupId = 4;

            if (groupId == 1)
                craftingTime = 60 * 6;
            else if (groupId == 2)
                craftingTime = 60 * 3;
            else if (groupId == 3)
                craftingTime = 60;
            else
                craftingTime = 10;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, craftingTime);

        CraftingPearl craftingPearl = new CraftingPearl(UUID.randomUUID(), pearlStars, calendar.getTime().getTime());

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return;

        jedis.set("crafting-pearls:" + player.getUniqueId().toString() + ":" + craftingPearl.getUUID().toString(), new Gson().toJson(craftingPearl));
        jedis.close();

        SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).decreasePowders(64);
        this.hub.getScoreboardManager().update(player);

        player.sendMessage(Well.TAG + ChatColor.GREEN + "Votre perle est en cours de création. Elle sera prête dans " + ChatColor.GOLD + (craftingTime / 60 >= 1 ? (craftingTime / 60) + " heure" + (craftingTime / 60 > 1 ? "s" : "") : craftingTime + " minute" + (craftingTime > 1 ? "s" : "")) + ChatColor.GREEN + ". Vous pouvez continuer de jouer pendant ce temps, vous serez informé quand elle sera prête.");
    }

    public void finalizePearlCrafting(Player player, UUID craftingPearlUUID)
    {
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return;

        CraftingPearl craftingPearl = null;

        if (jedis.exists("crafting-pearls:" + player.getUniqueId().toString() + ":" + craftingPearlUUID.toString()))
            craftingPearl = new Gson().fromJson(jedis.get("crafting-pearls:" + player.getUniqueId().toString() + ":" + craftingPearlUUID.toString()), CraftingPearl.class);

        if (craftingPearl == null)
        {
            jedis.close();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        Pearl craftedPearl = new Pearl(craftingPearl.getUUID(), craftingPearl.getStars(), calendar.getTime().getTime());
        this.addPearlToPlayer(player, craftedPearl);

        jedis.del("crafting-pearls:" + player.getUniqueId().toString() + ":" + craftingPearlUUID.toString());

        jedis.close();

        this.hub.getInteractionManager().getGraouManager().update(player);
    }

    public void addPearlToPlayer(Player player, Pearl pearl)
    {
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return;

        jedis.set("pearls:" + player.getUniqueId().toString() + ":" + pearl.getUUID().toString(), new Gson().toJson(pearl));
        jedis.expire("pearls:" + player.getUniqueId().toString() + ":" + pearl.getUUID().toString(), (int) TimeUnit.MILLISECONDS.toSeconds(pearl.getExpiration()));

        jedis.close();
    }

    private void checkCrafts(Player player, boolean silent)
    {
        this.getPlayerCraftingPearls(player.getUniqueId()).stream().filter(craftingPearl -> craftingPearl.getCreation() < System.currentTimeMillis()).forEach(craftingPearl ->
        {
            this.finalizePearlCrafting(player, craftingPearl.getUUID());

            if (!silent)
                player.sendMessage(ChatColor.GREEN + "\u25C9 Une perle de " + ChatColor.AQUA + "niveau " + craftingPearl.getStars() + ChatColor.GREEN + " a terminée de se créer ! Echangez la auprès de " + ChatColor.GOLD + "Graou" + ChatColor.GREEN + " ! \u25C9");
        });
    }

    public int[] generateRandomNumbers()
    {
        int[] parts = new int[4];
        int n = 10;
        int sum = 0;

        Random random = new Random();

        for (int i = 0; i < 4; i++)
            parts[i] = random.nextInt(100);

        for (int i = 0; i < 4; i++)
        {
            parts[i] = (int) Math.floor(parts[i] * n / 100 + 10);
            sum += parts[i];
        }

        int diff = 64 - sum;
        int i = 0;

        while (diff != 0)
        {
            parts[i] += diff > 0 ? 1 : -1;
            diff += diff > 0 ? -1 : 1;
            i = (i + 1) % 4;
        }

        return parts;
    }

    public List<CraftingPearl> getPlayerCraftingPearls(UUID player)
    {
        List<CraftingPearl> craftingPearls = new ArrayList<>();
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return craftingPearls;

        for (String key : jedis.keys("crafting-pearls:" + player.toString() + ":*"))
            craftingPearls.add(new Gson().fromJson(jedis.get(key), CraftingPearl.class));

        jedis.close();

        return craftingPearls;
    }

    private static Location normalize(Location location)
    {
        return new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
