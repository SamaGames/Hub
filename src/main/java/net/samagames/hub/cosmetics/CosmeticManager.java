package net.samagames.hub.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.disguises.DisguiseManager;
import org.bukkit.entity.Player;

public class CosmeticManager extends AbstractManager
{
    private final DisguiseManager disguiseManager;

    public CosmeticManager(Hub hub)
    {
        super(hub);

        this.disguiseManager = new DisguiseManager(hub);
    }

    @Override
    public void onDisable()
    {

    }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player)
    {

    }

    public DisguiseManager getDisguiseManager()
    {
        return this.disguiseManager;
    }

    public boolean isEquipped(Player player, AbstractCosmetic cosmetic)
    {
        boolean equipped = false;

        if (this.disguiseManager.getEquippedCosmetic(player) != null && cosmetic.compareTo(this.disguiseManager.getEquippedCosmetic(player)) == 1)
            equipped = true;

        return equipped;
    }
}
