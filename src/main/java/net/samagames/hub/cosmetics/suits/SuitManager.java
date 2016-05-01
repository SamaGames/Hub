package net.samagames.hub.cosmetics.suits;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.entity.Player;

import javax.lang.model.type.NullType;

public class SuitManager extends AbstractCosmeticManager<SuitCosmetic> {

    public SuitManager(Hub hub) {
        super(hub, new SuitRegistry(hub));
    }

    @Override
    public void update() {

    }

    @Override
    public void restoreCosmetic(Player player) {
        String value = this.cosmeticManager.getItemLevelForPlayer(player.getUniqueId(), "suit");

        if(value != null && !value.isEmpty())
            this.enableCosmetic(player, this.getRegistry().getElementByStorageName(value));
    }

    @Override
    public void enableCosmetic(Player player, SuitCosmetic cosmetic, NullType useless) {

    }

    @Override
    public void disableCosmetic(Player player, boolean logout, NullType useless) {

    }
}
