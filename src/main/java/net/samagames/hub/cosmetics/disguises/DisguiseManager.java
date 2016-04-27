package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.lang.model.type.NullType;

public class DisguiseManager extends AbstractCosmeticManager<DisguiseCosmetic>
{
    private static final String KEY = "disguise";

    public DisguiseManager(Hub hub)
    {
        super(hub, new DisguiseRegistry(hub));
    }

    @Override
    public void enableCosmetic(Player player, DisguiseCosmetic cosmetic, NullType useless)
    {
        MobDisguise disguise = new MobDisguise(cosmetic.getDisguiseType());
        disguise.setShowName(true);
        disguise.setViewSelfDisguise(true);

        DisguiseAPI.disguiseToAll(player, disguise);

        this.cosmeticManager.setCurrentLevel(player.getUniqueId(), KEY, cosmetic.getKey());
        player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Vous êtes maintenant déguisé !");
    }

    @Override
    public void disableCosmetic(Player player, boolean logout, NullType useless)
    {
        if(DisguiseAPI.isDisguised(player))
            DisguiseAPI.undisguiseToAll(player);

        if (!logout)
        {
            this.cosmeticManager.resetLevel(player.getUniqueId(), KEY);
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Votre déguisement disparait dans l'ombre...");
        }
    }

    @Override
    public void restoreCosmetic(Player player)
    {
        String value = this.cosmeticManager.getItemLevelForPlayer(player.getUniqueId(), KEY);

        if(value != null && !value.isEmpty())
            this.enableCosmetic(player, this.getRegistry().getElementByStorageName(value));
    }

    @Override
    public void update() { /** Not needed **/ }
}
