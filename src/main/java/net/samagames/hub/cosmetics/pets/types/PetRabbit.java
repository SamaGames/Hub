package net.samagames.hub.cosmetics.pets.types;

import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.cosmetics.pets.nms.CosmeticRabbit;
import org.bukkit.entity.Rabbit;
import org.bukkit.inventory.ItemStack;

public class PetRabbit extends PetCosmetic<Rabbit>
{
    public PetRabbit(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        super(databaseName, displayName, icon, description, CosmeticRabbit.class);
    }

    @Override
    public void applySettings(Rabbit spawned, String settings)
    {
        spawned.setRabbitType(Rabbit.Type.WHITE);
        spawned.setAdult();
    }
}
