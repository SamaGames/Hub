package net.samagames.hub.utils;

import com.google.common.collect.Maps;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;

import java.util.Map;

public class ColorsConverter
{
    private static Map<DyeColor, String> dyeChatMap;

    static
    {
        dyeChatMap = Maps.newHashMap();
        dyeChatMap.put(DyeColor.BLACK, ChatColor.DARK_GRAY + "Gris foncé");
        dyeChatMap.put(DyeColor.BLUE, ChatColor.DARK_BLUE + "Bleu foncé");
        dyeChatMap.put(DyeColor.BROWN, ChatColor.GOLD + "Marron");
        dyeChatMap.put(DyeColor.CYAN, ChatColor.AQUA + "Cyan");
        dyeChatMap.put(DyeColor.GRAY, ChatColor.GRAY + "Gris");
        dyeChatMap.put(DyeColor.GREEN, ChatColor.DARK_GREEN + "Vert");
        dyeChatMap.put(DyeColor.LIGHT_BLUE, ChatColor.BLUE + "Bleu clair");
        dyeChatMap.put(DyeColor.LIME, ChatColor.GREEN + "Vert clair");
        dyeChatMap.put(DyeColor.MAGENTA, ChatColor.LIGHT_PURPLE + "Magenta");
        dyeChatMap.put(DyeColor.ORANGE, ChatColor.GOLD + "Orange");
        dyeChatMap.put(DyeColor.PINK, ChatColor.LIGHT_PURPLE + "Rose");
        dyeChatMap.put(DyeColor.PURPLE, ChatColor.DARK_PURPLE + "Violet");
        dyeChatMap.put(DyeColor.RED, ChatColor.DARK_RED + "Rouge");
        dyeChatMap.put(DyeColor.SILVER, ChatColor.GRAY + "Argent");
        dyeChatMap.put(DyeColor.WHITE, ChatColor.WHITE + "Blanc");
        dyeChatMap.put(DyeColor.YELLOW, ChatColor.YELLOW + "Jaune");
    }

    public static String dyeToChat(DyeColor dclr)
    {
        if (dyeChatMap.containsKey(dclr))
            return dyeChatMap.get(dclr);

        return ChatColor.MAGIC + "Surprise...";
    }
}
