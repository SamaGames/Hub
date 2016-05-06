package net.samagames.hub.cosmetics.common;

import net.samagames.api.SamaGamesAPI;
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
    private AbstractCosmeticRegistry<COSMETIC> registry;
    private Map<UUID, COSMETIC> equipped;

    public AbstractCosmeticManager(Hub hub, AbstractCosmeticRegistry<COSMETIC> registry)
    {
        this.hub = hub;

        this.registry = registry;
        this.registry.register();

        this.equipped = new HashMap<>();
    }

    public abstract void enableCosmetic(Player player, COSMETIC cosmetic, NullType useless);
    public abstract void disableCosmetic(Player player, boolean logout, NullType useless);

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

                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) cosmetic.getStorageId()).setSelected(true);

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
            AbstractCosmetic cosmetic = this.getEquippedCosmetic(player);

            this.disableCosmetic(player, logout, null);
            this.equipped.remove(player.getUniqueId());

            if (!logout)
            {
                SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) cosmetic.getStorageId()).setSelected(false);

                AbstractGui gui = (AbstractGui) this.hub.getGuiManager().getPlayerGui(player);

                if (gui != null)
                    gui.update(player);
            }
        }
    }

    public void restoreCosmetic(Player player)
    {
        for (long storageId : this.registry.getElements().keySet())
        {
            if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) storageId) != null)
            {
                if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) storageId).isSelected());
                {
                    this.enableCosmetic(player, this.getRegistry().getElementByStorageId(storageId));
                    break;
                }
            }
        }
    }

    public void resetCurrents(Player player)
    {
        for (long storageId : this.registry.getElements().keySet())
            if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) storageId) != null)
                SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) storageId).setSelected(false);
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
