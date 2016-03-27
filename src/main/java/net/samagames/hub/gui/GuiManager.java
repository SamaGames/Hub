package net.samagames.hub.gui;

import net.samagames.api.gui.AbstractGui;
import net.samagames.api.gui.IGuiManager;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GuiManager extends AbstractManager implements IGuiManager
{
    private final ConcurrentMap<UUID, AbstractGui> playersGui;

    public GuiManager(Hub hub)
    {
        super(hub);

        this.playersGui = new ConcurrentHashMap<>();
    }

    @Override
    public void onDisable()
    {
        this.hub.getServer().getOnlinePlayers().forEach(this::onLogout);
    }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player)
    {
        if (this.playersGui.containsKey(player.getUniqueId()))
        {
            this.playersGui.get(player.getUniqueId()).onClose(player);
            this.playersGui.remove(player.getUniqueId());
        }
    }

    @Override
    public void openGui(Player player, AbstractGui gui)
    {
        if(this.playersGui.containsKey(player.getUniqueId()))
        {
            player.closeInventory();
            this.playersGui.remove(player.getUniqueId());
        }

        this.playersGui.put(player.getUniqueId(), gui);
        gui.display(player);
    }

    @Override
    public void closeGui(Player player)
    {
        if (this.playersGui.containsKey(player.getUniqueId()))
        {
            player.closeInventory();
            this.playersGui.remove(player.getUniqueId());
        }
    }

    @Override
    public void removeClosedGui(Player player)
    {
        if (this.playersGui.containsKey(player.getUniqueId()))
            this.playersGui.remove(player.getUniqueId());
    }

    @Override
    public AbstractGui getPlayerGui(HumanEntity humanEntity)
    {
        return this.getPlayerGui(humanEntity.getUniqueId());
    }

    @Override
    public AbstractGui getPlayerGui(UUID uuid)
    {
        return this.playersGui.containsKey(uuid) ? this.playersGui.get(uuid) : null;
    }

    @Override
    public ConcurrentHashMap<UUID, AbstractGui> getPlayersGui()
    {
        return (ConcurrentHashMap<UUID, AbstractGui>) this.playersGui;
    }
}
