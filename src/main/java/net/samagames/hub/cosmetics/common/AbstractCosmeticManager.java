package net.samagames.hub.cosmetics.common;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.AbstractShopsManager;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.lang.model.type.NullType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractCosmeticManager<COSMETIC extends AbstractCosmetic>
{
    protected final Hub hub;
    protected final AbstractShopsManager cosmeticManager;
    private AbstractCosmeticRegistry<COSMETIC> registry;
    private Map<UUID, COSMETIC> equipped;

    public AbstractCosmeticManager(Hub hub, AbstractCosmeticRegistry<COSMETIC> registry)
    {
        this.hub = hub;
        this.cosmeticManager = SamaGamesAPI.get().getShopsManager();

        this.registry = registry;
        this.registry.register();

        this.equipped = new HashMap<>();
    }

    public abstract void enableCosmetic(Player player, COSMETIC cosmetic, NullType useless);
    public abstract void disableCosmetic(Player player, boolean logout, NullType useless);
    public abstract void restoreCosmetic(Player player);

    public abstract void update();

    public void enableCosmetic(Player player, COSMETIC cosmetic)
    {
        if (cosmetic.isOwned(player))
        {
            if (cosmetic.getAccessibility().canAccess(player))
            {
                if (this.equipped.containsKey(player.getUniqueId()) && this.equipped.get(player.getUniqueId()).compareTo(cosmetic) > 0)
                {
                    player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous utilisez déjà cette cosmétique.");
                }
                else
                {
                    this.enableCosmetic(player, cosmetic, null);
                    this.equipped.put(player.getUniqueId(), cosmetic);

                    AbstractGui gui = (AbstractGui) this.hub.getGuiManager().getPlayerGui(player);

                    if (gui != null)
                        gui.update(player);
                }
            }
            else
            {
                player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous n'avez pas le grade nécessaire pour utiliser cette cosmétique.");
            }
        }
        else
        {
            cosmetic.buy(player, false);
        }
    }

    public void disableCosmetic(Player player, boolean logout)
    {
        if (this.equipped.containsKey(player.getUniqueId()))
        {
            this.disableCosmetic(player, logout, null);
            this.equipped.remove(player.getUniqueId());

            if (!logout)
            {
                AbstractGui gui = (AbstractGui) this.hub.getGuiManager().getPlayerGui(player);

                if (gui != null)
                    gui.update(player);
            }
        }
    }

    public COSMETIC getEquippedCosmetic(Player player)
    {
        if (this.equipped.containsKey(player.getUniqueId()))
            return this.equipped.get(player.getUniqueId());
        else
            return null;
    }

    public AbstractCosmeticRegistry<COSMETIC> getRegistry()
    {
        return this.registry;
    }
}
