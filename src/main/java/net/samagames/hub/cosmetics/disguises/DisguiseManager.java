package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DisguiseManager extends AbstractCosmeticManager<DisguiseCosmetic>
{
    public DisguiseManager(Hub hub)
    {
        super(hub, new DisguiseRegistry());
    }

    @Override
    public void enableCosmetic(Player player, DisguiseCosmetic cosmetic)
    {
        if (cosmetic.isOwned(player))
        {
            MobDisguise disguise = new MobDisguise(cosmetic.getDisguiseType());
            disguise.getEntity().setCustomName(player.getName());
            disguise.getEntity().setCustomNameVisible(true);
            disguise.setViewSelfDisguise(false);

            DisguiseAPI.disguiseToAll(player, disguise);

            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).set("cosmetics.disguise.current", cosmetic.getDatabaseName());
            player.sendMessage(ChatColor.GREEN + "Vous êtes maintenant déguisé !");
        }
        else
        {
            cosmetic.buy(player);
        }
    }

    @Override
    public void disableCosmetic(Player player, boolean logout)
    {
        if(DisguiseAPI.isDisguised(player))
            DisguiseAPI.undisguiseToAll(player);

        if (!logout)
        {
            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).remove("cosmetics.disguise.current");
            player.sendMessage(ChatColor.GREEN + "Votre déguisement disparait dans l'ombre...");
        }
    }

    @Override
    public void restoreCosmetic(Player player)
    {
        String value = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).get("cosmetics.disguise.current");

        if(value != null)
            this.enableCosmetic(player, this.getRegistry().getElementByStorageName(value));
    }

    @Override
    public void update() {}

    @Override
    public String getName() { return "ParticleManager"; }
}
