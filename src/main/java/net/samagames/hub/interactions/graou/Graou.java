package net.samagames.hub.interactions.graou;

import net.minecraft.server.v1_10_R1.PathEntity;
import net.minecraft.server.v1_10_R1.WorldServer;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.tools.holograms.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

class Graou extends AbstractInteraction
{
    protected static final String TAG = ChatColor.GOLD + "[" + ChatColor.YELLOW + "Graou" + ChatColor.GOLD + "] " + ChatColor.RESET;
    private static final String GRAOU_NAME = ChatColor.GOLD + "" + ChatColor.BOLD + "Graou";

    private final Map<UUID, Hologram> holograms;
    private final EntityGraou graouEntity;
    private final Location catLocation;
    private final Location[] treasureLocations;
    private final Location[] openingLocations;
    private BukkitTask animationTask;
    private UUID playerUsing;

    Graou(Hub hub, Location catLocation, Location[] treasureLocations, Location[] openingLocations)
    {
        super(hub);

        this.holograms = new HashMap<>();

        WorldServer world = ((CraftWorld) catLocation.getWorld()).getHandle();

        this.graouEntity = new EntityGraou(world);
        this.graouEntity.setPosition(catLocation.getX(), catLocation.getY(), catLocation.getZ());
        world.addEntity(this.graouEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        catLocation.getChunk().load(true);

        hub.getServer().getScheduler().runTaskLater(hub, () -> this.graouEntity.postInit(catLocation.getYaw(), catLocation.getPitch()), 20L);

        this.catLocation = catLocation;
        this.treasureLocations = treasureLocations;
        this.openingLocations = openingLocations;
        this.animationTask = null;
        this.playerUsing = null;
    }

    @Override
    public void onDisable()
    {
        if (this.animationTask != null)
            this.animationTask.cancel();

        this.graouEntity.die();
    }

    public void onLogin(Player player)
    {
        if (this.holograms.containsKey(player.getUniqueId()))
            this.onLogout(player);

        int perls = 0;

        // Count boxes

        Hologram hologram;

        if (perls > 0)
            hologram = new Hologram(GRAOU_NAME, ChatColor.GOLD + "" + perls + ChatColor.YELLOW + " perle" + (perls > 1 ? "s" : "") + " à échanger");
        else
            hologram = new Hologram(GRAOU_NAME);

        hologram.generateLines(this.graouEntity.getBukkitEntity().getLocation().clone().add(0.0D, 0.75D, 0.0D));
        hologram.addReceiver(player);

        this.holograms.put(player.getUniqueId(), hologram);

        if (perls > 0)
        {
            player.sendMessage(TAG + ChatColor.RED + "Vous avez " + ChatColor.GOLD + perls + ChatColor.RED + " perle" + (perls > 1 ? "s" : "") + " à échanger ! Venez me voir :)");
            player.playSound(this.graouEntity.getBukkitEntity().getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.5F);
        }
    }

    public void onLogout(Player player)
    {
        if (this.playerUsing != null && this.playerUsing == player.getUniqueId())
        {
            this.playerUsing = null;

            this.animationTask.cancel();
            this.animationTask = null;

            this.respawn();
        }

        if (this.holograms.containsKey(player.getUniqueId()))
        {
            Hologram hologram = this.holograms.get(player.getUniqueId());
            hologram.removeLinesForPlayers();
            hologram.removeReceiver(player);

            this.holograms.remove(player.getUniqueId());
        }
    }

    @Override
    public void play(Player player)
    {
        if (this.animationTask != null)
        {
            player.sendMessage(TAG + ChatColor.RED + "Je suis actuellement occupé avec un autre joueur.");
            return;
        }

        this.hub.getGuiManager().openGui(player, new GuiGraou(this.hub, this));
        player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);

        if (!SamaGamesAPI.get().getAchievementManager().isUnlocked(player.getUniqueId(), 19))
            SamaGamesAPI.get().getAchievementManager().getAchievementByID(19).unlock(player.getUniqueId());
    }

    @Override
    public void stop(Player player) { /** Not needed **/ }

    public void update(Player player)
    {
        int perls = 0;

        // Count boxes

        Hologram hologram = this.holograms.get(player.getUniqueId());

        if (perls > 0)
            hologram.change(GRAOU_NAME, ChatColor.GOLD + "" + perls + ChatColor.YELLOW + " perle" + (perls > 1 ? "s" : "") + " à échanger");
        else
            hologram.change(GRAOU_NAME);

        hologram.sendLines(player);
    }

    public void openBox(Player player)
    {
        if (this.animationTask != null)
        {
            player.sendMessage(TAG + ChatColor.RED + "Je suis actuellement occupé avec un autre joueur.");
            return;
        }

        this.playerUsing = player.getUniqueId();

        this.graouEntity.getBukkitEntity().getWorld().playSound(this.graouEntity.getBukkitEntity().getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.5F);
        this.animationTask = this.hub.getServer().getScheduler().runTask(this.hub, new OpeningAnimationRunnable(this.hub, this, player, this.treasureLocations, this.openingLocations));
    }

    public void animationFinished(Player player)
    {
        this.animationTask.cancel();
        this.animationTask = null;

        this.stop(player);
    }

    public void respawn()
    {
        this.graouEntity.getNavigation().a((PathEntity) null, 1.0F);
        this.graouEntity.setPositionRotation(this.catLocation.getX(), this.catLocation.getY(), this.catLocation.getZ(), this.catLocation.getYaw(), this.catLocation.getPitch());
        this.graouEntity.positionChanged = true;

        this.graouEntity.setSitting(true);
    }

    public void toggleHolograms(boolean visible)
    {
        if (visible)
            this.holograms.values().forEach(Hologram::sendLinesForPlayers);
        else
            this.holograms.values().forEach(Hologram::removeLinesForPlayers);
    }

    public EntityGraou getGraouEntity()
    {
        return this.graouEntity;
    }

    @Override
    public boolean hasPlayer(Player player)
    {
        return this.hub.getGuiManager().getPlayerGui(player) instanceof GuiGraou;
    }
}
