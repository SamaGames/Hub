package net.samagames.hub.cosmetics.common;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.AbstractShopsManager;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.entity.Player;

public abstract class AbstractCosmeticManager<T extends AbstractCosmetic> extends AbstractManager
{
    protected final AbstractShopsManager cosmeticManager;
    private AbstractCosmeticRegistry<T> registry;

    public AbstractCosmeticManager(Hub hub, AbstractCosmeticRegistry<T> registry)
    {
        super(hub);

        this.cosmeticManager = SamaGamesAPI.get().getShopsManager("cosmetic");
        this.registry = registry;
        this.registry.register();
    }

    public abstract void enableCosmetic(Player player, T cosmetic);
    public abstract void disableCosmetic(Player player, boolean logout);
    public abstract void restoreCosmetic(Player player);

    public abstract void update();

    public AbstractCosmeticRegistry<T> getRegistry()
    {
        return this.registry;
    }
}
