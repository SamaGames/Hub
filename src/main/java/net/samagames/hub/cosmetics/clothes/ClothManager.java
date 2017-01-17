package net.samagames.hub.cosmetics.clothes;

import net.minecraft.server.v1_10_R1.EntityGuardian;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.lang.model.type.NullType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 15/01/2017
 */
public class ClothManager extends AbstractCosmeticManager<ClothCosmetic>
{
    private final Map<UUID, ClothPreviewTask> previewers;

    public ClothManager(Hub hub)
    {
        super(hub, new ClothRegistry(hub));

        this.previewers = new HashMap<>();

        this.hub.getEntityManager().registerEntity("ClothCamera", 68, EntityGuardian.class, EntityClothCamera.class);
    }

    @Override
    public void enableCosmetic(Player player, ClothCosmetic cosmetic, ClickType clickType)
    {
        if (!cosmetic.isOwned(player) && clickType == ClickType.RIGHT)
            this.startPreview(player, cosmetic);
        else
            super.enableCosmetic(player, cosmetic, null);
    }

    @Override
    public void enableCosmetic(Player player, ClothCosmetic cosmetic, ClickType clickType, NullType useless)
    {
        cosmetic.getSlot().equip(player, cosmetic.getPiece());
        player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Vous brillez de mille feux avec votre nouvel habit !");
    }

    @Override
    public void disableCosmetic(Player player, boolean logout, NullType useless)
    {
        if (this.previewers.containsKey(player.getUniqueId()))
            this.stopPreview(player);
    }

    @Override
    public void update() { /** Not needed **/ }

    public void startPreview(Player player, ClothCosmetic cosmetic)
    {
        ClothPreviewTask clothPreviewTask = new ClothPreviewTask(this.hub, player.getLocation());

        this.hub.getGuiManager().closeGui(player);

        player.setGameMode(GameMode.SPECTATOR);
        player.setSpectatorTarget(clothPreviewTask.getCamera().getBukkitEntity());

        this.hub.getPlayerManager().addHider(player);
        this.previewers.put(player.getUniqueId(), clothPreviewTask);
    }

    public void stopPreview(Player player)
    {
        if (this.previewers.containsKey(player.getUniqueId()))
        {
            player.setSpectatorTarget(null);
            player.setGameMode(GameMode.ADVENTURE);

            this.previewers.get(player.getUniqueId()).stop();
            this.previewers.remove(player.getUniqueId());

            this.hub.getPlayerManager().removeHider(player);
        }
    }

    public boolean isPreviewing(Player player)
    {
        return this.previewers.containsKey(player.getUniqueId());
    }
}
