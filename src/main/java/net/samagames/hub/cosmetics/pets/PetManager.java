package net.samagames.hub.cosmetics.pets;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PetManager extends AbstractCosmeticManager<PetCosmetic>
{
    private final HashMap<UUID, LivingEntity> pets;

    public PetManager(Hub hub)
    {
        super(hub, new PetRegistry());

        this.pets = new HashMap<>();
    }

    @Override
    public void enableCosmetic(Player player, PetCosmetic cosmetic)
    {
        if(cosmetic.isOverridingUse())
            cosmetic.onUse(player);
        else
            this.enableCosmetic(player, cosmetic, null);
    }

    public void enableCosmetic(Player player, PetCosmetic cosmetic, String settings)
    {
        if (cosmetic.isOwned(player))
        {
            if (this.hadPet(player))
                this.disableCosmetic(player, false);

            LivingEntity pet = cosmetic.spawn(player);

            if(pet == null)
                return;

            this.pets.put(player.getUniqueId(), pet);

            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).set("cosmetics.pet.current", cosmetic.getDatabaseName());

            if(settings == null && SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).get("cosmetics.pet.current.settings") == null)
                return;

            cosmetic.applySettings(pet, settings);

            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).set("cosmetics.pet.current.settings", settings);
        }
        else
        {
            cosmetic.buy(player);
        }
    }

    @Override
    public void disableCosmetic(Player player, boolean logout)
    {
        if (this.pets.containsKey(player.getUniqueId()))
        {
            final LivingEntity pet = this.pets.get(player.getUniqueId());

            Bukkit.getScheduler().runTask(this.hub, () ->
            {
                pet.setHealth(0);
                pet.remove();
            });
        }

        this.pets.remove(player.getUniqueId());

        if (!logout)
        {
            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).remove("cosmetics.pet.current");
            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).remove("cosmetics.pet.current.settings");
            player.sendMessage(ChatColor.GREEN + "Votre animal disparait dans l'ombre...");
        }
    }

    @Override
    public void restoreCosmetic(Player player)
    {
        String value = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).get("cosmetics.pet.current");

        if(value != null)
            this.enableCosmetic(player, this.getRegistry().getElementByStorageName(value));
    }

    @Override
    public void update() {}

    @Override
    public String getName()
    {
        return "PetManager";
    }

    public boolean hadPet(Player player)
    {
        return this.pets.containsKey(player.getUniqueId());
    }
}
