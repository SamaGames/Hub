package net.samagames.hub.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.disguises.DisguiseManager;
import net.samagames.hub.cosmetics.jukebox.JukeboxManager;
import net.samagames.hub.cosmetics.pets.PetManager;
import org.bukkit.entity.Player;

public class CosmeticManager extends AbstractManager
{
    private final DisguiseManager disguiseManager;
    private final JukeboxManager jukeboxManager;
    private final PetManager petManager;

    public CosmeticManager(Hub hub)
    {
        super(hub);

        this.disguiseManager = new DisguiseManager(hub);
        this.jukeboxManager = new JukeboxManager(hub);
        this.petManager = new PetManager(hub);
    }

    @Override
    public void onDisable() { /** Not needed **/ }

    @Override
    public void onLogin(Player player)
    {
        this.jukeboxManager.onLogin(player);
    }

    @Override
    public void onLogout(Player player)
    {
        this.jukeboxManager.onLogout(player);

        this.disguiseManager.disableCosmetic(player, true);
        this.jukeboxManager.disableCosmetic(player, true);
        this.petManager.disableCosmetic(player, true);
    }

    public DisguiseManager getDisguiseManager()
    {
        return this.disguiseManager;
    }

    public JukeboxManager getJukeboxManager()
    {
        return this.jukeboxManager;
    }

    public PetManager getPetManager()
    {
        return this.petManager;
    }

    public boolean isEquipped(Player player, AbstractCosmetic cosmetic)
    {
        boolean equipped = false;

        if (this.disguiseManager.getEquippedCosmetic(player) != null && cosmetic.compareTo(this.disguiseManager.getEquippedCosmetic(player)) == 1)
            equipped = true;
        else if (this.petManager.getEquippedCosmetic(player) != null && cosmetic.compareTo(this.petManager.getEquippedCosmetic(player)) == 1)
            equipped = true;

        return equipped;
    }
}
