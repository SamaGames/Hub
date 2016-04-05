package net.samagames.hub.common.players;

import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;
import org.bukkit.entity.Player;

public enum PlayerSettings
{
    PLAYERS("players", true),
    CHAT("chat", true),
    PRIVATE_MESSAGES("private-messages", true),
    NOTIFICATIONS("notifications", true),
    FRIEND_REQUESTS("friend-requests", true),
    PARTY_REQUESTS("party-requests", true),
    JUKEBOX("jukebox", true),
    INTERACTIONS("interactions", true),
    QUEUE_NOTIFICATIONS("queuenotifcations", true),

    CLICKME("clickme", true),
    CLICKME_STATS("clickme-stats", true),
    CLICKME_COINS("clickme-coins", true),
    CLICKME_STARS("clickme-stars", true),
    CLICKME_PUNCH("clickme-punch", true)
    ;

    private final String databaseKey;
    private final boolean defaultsTo;

    PlayerSettings(String databaseKey, boolean defaultsTo)
    {
        this.databaseKey = databaseKey;
        this.defaultsTo = defaultsTo;
    }

    public void updateFor(Player player, boolean value, Runnable callback)
    {
        SamaGamesAPI.get().getSettingsManager().setSetting(player.getUniqueId(), this.databaseKey, String.valueOf(value), () ->
        {
            player.sendMessage(PlayerManager.SETTINGS_TAG + ChatColor.GREEN + "Préférences enregistrées et appliquées.");
            callback.run();
        });
    }

    public boolean isEnabled(Player player)
    {
        return SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), this.databaseKey, this.defaultsTo);
    }

    public String getDatabaseKey()
    {
        return this.databaseKey;
    }
}
