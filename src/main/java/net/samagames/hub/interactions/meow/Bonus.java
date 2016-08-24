package net.samagames.hub.interactions.meow;

import net.samagames.api.SamaGamesAPI;
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
        calendar.add(this.reloadUnit, this.reloadNumber);

        long millis = calendar.getTime().getTime() - new Date().getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        String key = "bonus:" + uuid.toString() + ":" + this.id;

        jedis.set(key, String.valueOf(millis));
        jedis.expire(key, (int) seconds);

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
            long millis = new Date(this.getUnlockTime(uuid)).getTime() - new Date().getTime();
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long hours = TimeUnit.MILLISECONDS.toHours(millis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

            lore.add(ChatColor.RED + "Vous pourrez récupérer ce bonus");
            lore.add(ChatColor.RED + "dans : " + days + " jour" + (days > 1 ? "s" : "") + " " + hours + " heure" + (hours > 1 ? "s" : "") + " et " + minutes + " minute" + (minutes > 1 ? "s" : "")  + ".");
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
