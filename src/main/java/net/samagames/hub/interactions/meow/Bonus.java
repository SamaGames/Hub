package net.samagames.hub.interactions.meow;

import net.samagames.api.SamaGamesAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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
class Bonus
{
    private final int id;
    private final int slot;
    private final String name;
    private final String[] description;
    private final int reloadNumber;
    private final int reloadUnit;
    private final String permissionNeeded;
    private final Consumer<Player> callback;

    Bonus(int id, int slot, String name, String[] description, int reloadNumber, int reloadUnit, String permissionNeeded, Consumer<Player> callback)
    {
        this.id = id;
        this.slot = slot;
        this.name = name;
        this.description = description;
        this.reloadNumber = reloadNumber;
        this.reloadUnit = reloadUnit;
        this.permissionNeeded = permissionNeeded;
        this.callback = callback;
    }

    public void take(Player player)
    {
        if (!this.isAbleFor(player))
            return;

        this.callback.accept(player);

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(this.reloadUnit, this.reloadNumber);

        long millis = calendar.getTime().getTime() - new Date().getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        String key = "bonus:" + player.getUniqueId().toString() + ":" + this.id;

        jedis.set(key, String.valueOf(calendar.getTime().getTime()));
        jedis.expire(key, (int) seconds);

        jedis.close();
    }

    public ItemStack getIcon(Player player)
    {
        boolean able = this.isAbleFor(player);

        ItemStack stack = new ItemStack(able ? Material.COOKED_FISH : Material.RAW_FISH, 1);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(this.name);

        List<String> lore = new ArrayList<>(Arrays.asList(this.description));
        lore.add("");

        if (!able)
        {
            long millis = new Date(this.getUnlockTime(player)).getTime() - new Date().getTime();
            long days = millis / (1000 * 60 * 60 * 24);
            long hours = (millis / (1000 * 60 * 60)) % 24;
            long minutes = (millis / (1000 * 60)) % 60;

            lore.add(ChatColor.RED + "Vous pourrez récupérer ce");
            lore.add(ChatColor.RED + "bonus dans : ");
            lore.add(ChatColor.RED + "" + days + " jour" + (days > 1 ? "s" : "") + " " + hours + " heure" + (hours > 1 ? "s" : "") + " et " + minutes + " minute" + (minutes > 1 ? "s" : "")  + ".");
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

    public int getId()
    {
        return this.id;
    }

    public int getSlot()
    {
        return this.slot;
    }

    public long getUnlockTime(Player player)
    {
        if (this.isAbleFor(player))
            return 0;

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return 0;

        String value = jedis.get("bonus:" + player.getUniqueId().toString() + ":" + this.id);

        jedis.close();

        return Long.parseLong(value);
    }

    public boolean isAbleFor(Player player)
    {
        if (this.permissionNeeded != null && !SamaGamesAPI.get().getPermissionsManager().hasPermission(player.getUniqueId(), this.permissionNeeded))
            return false;

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis == null)
            return true;

        boolean value = !jedis.exists("bonus:" + player.getUniqueId().toString() + ":" + this.id);

        jedis.close();

        return value;
    }
}
