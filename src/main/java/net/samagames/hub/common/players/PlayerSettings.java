package net.samagames.hub.common.players;

import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import org.bukkit.entity.Player;

public enum PlayerSettings
{
    PLAYERS("players", true, (player, value) ->
    {
        if (value)
            PlayerManager.HUB.getPlayerManager().removeHider(player);
        else
            PlayerManager.HUB.getPlayerManager().addHider(player);
    }),
    CHAT("chat", true, (player, value) ->
    {
        if (value)
            PlayerManager.HUB.getChatManager().enableChatFor(player);
        else
            PlayerManager.HUB.getChatManager().disableChatFor(player);
    }),
    PRIVATE_MESSAGES("private-messages", true, null),
    NOTIFICATIONS("notifications", true, null),
    FRIEND_REQUESTS("friend-requests", true, null),
    PARTY_REQUESTS("party-requests", true, null),
    JUKEBOX("jukebox", true, (player, value) ->
    {
        if (value)
            PlayerManager.HUB.getCosmeticManager().getJukeboxManager().addPlayer(player);
        else
            PlayerManager.HUB.getCosmeticManager().getJukeboxManager().removePlayer(player);
    }),
    INTERACTIONS("interactions", true, null),
    QUEUE_NOTIFICATIONS("queuenotifcations", true, null),

    CLICKME("clickme", true, null),
    CLICKME_STATS("clickme-stats", true, null),
    CLICKME_COINS("clickme-coins", true, null),
    CLICKME_STARS("clickme-stars", true, null),
    CLICKME_PUNCH("clickme-punch", true, null)
    ;

    private final String databaseKey;
    private final boolean defaultsTo;
    private final SettingCallback callback;

    PlayerSettings(String databaseKey, boolean defaultsTo, SettingCallback callback)
    {
        this.databaseKey = databaseKey;
        this.defaultsTo = defaultsTo;
        this.callback = callback;
    }

    public void updateFor(Player player, boolean value, Runnable callback)
    {
        SamaGamesAPI.get().getSettingsManager().setSetting(player.getUniqueId(), this.databaseKey, String.valueOf(value), () ->
        {
            if (this.callback != null)
                this.callback.run(player, value);

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

    private interface SettingCallback
    {
        void run(Player player, boolean value);
    }
}
