package net.samagames.hub.interactions.meow;

import net.samagames.api.SamaGamesAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.TimeUnit;

class Bonus
{
    private final int id;
    private final int slot;
    private final String name;
    private final String[] description;
    private final int stars;
    private final int reloadNumber;
    private final int reloadUnit;
    private final String permissionNeeded;

    Bonus(int id, int slot, String name, String[] description, int stars, int reloadNumber, int reloadUnit, String permissionNeeded)
    {
        this.id = id;
        this.slot = slot;
        this.name = name;
        this.description = description;
        this.stars = stars;
        this.reloadNumber = reloadNumber;
        this.reloadUnit = reloadUnit;
        this.permissionNeeded = permissionNeeded;
    }

    public void take(UUID uuid)
    {
        if (!this.isAbleFor(uuid))
            return;

        SamaGamesAPI.get().getPlayerManager().getPlayerData(uuid).creditStars(this.stars, "Récupération de la récompense : " + this.name, false);

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(this.reloadNumber, this.reloadUnit);
        
        long millis = calendar.getTime().getTime() - new Date().getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        String key = "bonus:" + uuid.toString() + ":" + this.id;

        jedis.set(key, String.valueOf(millis));
        jedis.expire(key, (int) seconds);

        Bukkit.broadcastMessage("Expire in " + seconds + " seconds");

        jedis.close();
    }

    public ItemStack getIcon(UUID uuid)
    {
        boolean able = this.isAbleFor(uuid);

        ItemStack stack = new ItemStack(able ? Material.COOKED_FISH : Material.RAW_FISH, 1);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(this.name);

        List<String> lore = new ArrayList<>(Arrays.asList(this.description));
        lore.add("");

        if (!able)
        {
            lore.add(ChatColor.RED + "Vous ne pourrez récupérer votre");
            lore.add(ChatColor.RED + "bonus seulement dans :");
            lore.add("");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(this.getUnlockTime(uuid));
            calendar.add(Calendar.MONTH, 1);

            long timeInMillis = calendar.getTimeInMillis() - new Date().getTime();

            long days = TimeUnit.MILLISECONDS.toDays(timeInMillis);
            timeInMillis -= TimeUnit.DAYS.toMillis(days);
            long hours = TimeUnit.MILLISECONDS.toHours(timeInMillis);
            timeInMillis -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis);
            timeInMillis -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis);

            String line = "";

            if (days > 0)
                line += days + " jour" + (days > 1 ? "s" : "") + " ";

            if (hours > 0)
                line += hours + " heure" + (hours > 1 ? "s" : "") + " ";

            if (minutes > 0)
                line += minutes + " minute" + (minutes > 1 ? "s" : "") + " ";

            if (seconds > 0)
                line += seconds + " seconde" + (seconds > 1 ? "s" : "");

            lore.add(ChatColor.RED + line);
        }
        else
        {
            lore.add(ChatColor.GREEN + "Récupérer votre bonus dès");
            lore.add(ChatColor.GREEN + "maintenant !");
        }

        meta.setLore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    public long getUnlockTime(UUID uuid)
    {
        if (this.isAbleFor(uuid))
            return 0;

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return 0;

        String value = jedis.get("bonus:" + uuid.toString() + ":" + this.id);

        jedis.close();

        return Long.parseLong(value);
    }

    public int getId()
    {
        return this.id;
    }

    public int getSlot()
    {
        return this.slot;
    }

    public boolean isAbleFor(UUID uuid)
    {
        if (this.permissionNeeded != null && !SamaGamesAPI.get().getPermissionsManager().hasPermission(uuid, this.permissionNeeded))
            return false;

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return true;

        boolean value = !jedis.exists("bonus:" + uuid.toString() + ":" + this.id);

        jedis.close();

        return value;
    }
}
