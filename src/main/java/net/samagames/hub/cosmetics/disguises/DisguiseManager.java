package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.lang.model.type.NullType;

public class DisguiseManager extends AbstractCosmeticManager<DisguiseCosmetic>
{
    public DisguiseManager(Hub hub)
    {
        super(hub, new DisguiseRegistry(hub));
    }

    @Override
    public void enableCosmetic(Player player, DisguiseCosmetic cosmetic, ClickType clickType, boolean login, NullType useless)
    {
        MobDisguise disguise = new MobDisguise(cosmetic.getDisguiseType());
        disguise.setShowName(true);
        disguise.setViewSelfDisguise(false);

        DisguiseAPI.disguiseToAll(player, disguise);

        if (!login)
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Vous êtes maintenant déguisé !");
    }

    @Override
    public void disableCosmetic(Player player, DisguiseCosmetic cosmetic, boolean logout, boolean replace, NullType useless)
    {
        if (DisguiseAPI.isDisguised(player))
            DisguiseAPI.undisguiseToAll(player);

        if (!logout && !replace)
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Votre déguisement disparait dans l'ombre...");
    }

    @Override
    public void update() { /** Not needed **/ }

    @Override
    public boolean restrictToOne()
    {
        return true;
    }
}
