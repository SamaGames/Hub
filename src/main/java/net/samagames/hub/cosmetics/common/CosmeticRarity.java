package net.samagames.hub.cosmetics.common;

import org.bukkit.ChatColor;

public enum CosmeticRarity
{
    COMMON("Commun", ChatColor.GREEN),
    RARE("Rare", ChatColor.BLUE),
    EPIC("Epic", ChatColor.DARK_PURPLE),
    LEGENDARY("Légendaire", ChatColor.GOLD),
    STAFF("Réservé à l'équipe", ChatColor.RED),
    ;

    private final String name;
    private final ChatColor color;

    CosmeticRarity(String name, ChatColor color)
    {
        this.name = name;
        this.color = color;
    }

    public String getName()
    {
        return this.name;
    }

    public ChatColor getColor()
    {
        return this.color;
    }
}
