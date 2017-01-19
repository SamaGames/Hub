package net.samagames.hub.cosmetics.common;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.lang.model.type.NullType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

public abstract class AbstractCosmeticManager<COSMETIC extends AbstractCosmetic>
{
    protected final Hub hub;
    protected ConcurrentMap<UUID, List<COSMETIC>> equipped;
    private AbstractCosmeticRegistry<COSMETIC> registry;

    public AbstractCosmeticManager(Hub hub, AbstractCosmeticRegistry<COSMETIC> registry)
    {
        this.hub = hub;
        this.registry = registry;

        try
        {
            hub.getCosmeticManager().log(Level.INFO, "Loading registry " + this.getClass().getSimpleName() + "...");
            this.registry.register();
        }
        catch (Exception e)
        {
            hub.getCosmeticManager().log(Level.SEVERE, "Failed to load the registry " + this.getClass().getSimpleName() + "!");
            e.printStackTrace();
        }

        this.equipped = new ConcurrentHashMap<>();
    }

    public abstract void enableCosmetic(Player player, COSMETIC cosmetic, ClickType clickType, NullType useless);
    public abstract void disableCosmetic(Player player, COSMETIC cosmetic, boolean logout, NullType useless);

    public abstract void update();

    public abstract boolean restrictToOne();

    public void enableCosmetic(Player player, COSMETIC cosmetic, ClickType clickType)
    {
        if (cosmetic.isOwned(player))
        {
            if (cosmetic.getAccessibility().canAccess(player))
            {
                if (this.equipped.containsKey(player.getUniqueId()) && this.equipped.get(player.getUniqueId()).stream().filter(c -> c.compareTo(cosmetic) > 0).findAny().isPresent())
                {
                    player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous utilisez déjà ce cosmétique.");
                }
                else
                {
                    if (this.restrictToOne())
                        this.disableCosmetics(player, false);

                    this.enableCosmetic(player, cosmetic, clickType, null);

                    if (!this.equipped.containsKey(player.getUniqueId()))
                        this.equipped.put(player.getUniqueId(), new ArrayList<>());

                    this.equipped.get(player.getUniqueId()).add(cosmetic);

                    if (this.restrictToOne())
                        this.resetCurrents(player);

                    try
                    {
                        SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem(cosmetic.getStorageId(), true);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

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
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous ne possédez pas ce cosmétique. Tentez de le débloquer auprès de Graou !");
        }
    }

    public void disableCosmetic(Player player, COSMETIC cosmetic, boolean logout)
    {
        if (this.equipped.containsKey(player.getUniqueId()) && this.equipped.get(player.getUniqueId()).contains(cosmetic))
        {
            this.disableCosmetic(player, cosmetic, logout, null);
            this.equipped.get(player.getUniqueId()).remove(cosmetic);

            if (!logout)
            {
                try
                {
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem(cosmetic.getStorageId(), false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                AbstractGui gui = (AbstractGui) this.hub.getGuiManager().getPlayerGui(player);

                if (gui != null)
                    gui.update(player);
            }
        }
    }

    public void disableCosmetics(Player player, boolean logout)
    {
        if (this.getEquippedCosmetics(player) == null)
            return;

        for (COSMETIC cosmetic : this.getEquippedCosmetics(player))
            this.disableCosmetic(player, cosmetic, logout);
    }

    public void restoreCosmetic(Player player)
    {
        this.registry.getElements().keySet().stream().filter(storageId -> SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(storageId) != null).forEach(storageId ->
        {
            try
            {
                if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).isSelectedItem(storageId))
                    this.enableCosmetic(player, this.getRegistry().getElementByStorageId(storageId), ClickType.LEFT);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public void resetCurrents(Player player)
    {
        this.registry.getElements().keySet().stream().filter(storageId -> SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(storageId) != null).forEach(storageId ->
        {
            try
            {
                SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem(storageId, false);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public List<COSMETIC> getEquippedCosmetics(Player player)
    {
        if (this.equipped.containsKey(player.getUniqueId()))
        {
            if (!this.equipped.get(player.getUniqueId()).isEmpty())
                return this.equipped.get(player.getUniqueId());
            else
                return null;
        }
        else
        {
            return null;
        }
    }

    public AbstractCosmeticRegistry<COSMETIC> getRegistry()
    {
        return this.registry;
    }

    public boolean isCosmeticEquipped(Player player, AbstractCosmetic cosmetic)
    {
        return this.equipped.get(player.getUniqueId()).stream().filter(c -> c.compareTo(cosmetic) > 0).findAny().isPresent();
    }
}
