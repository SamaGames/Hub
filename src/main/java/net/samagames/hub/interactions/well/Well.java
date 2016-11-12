package net.samagames.hub.interactions.well;

import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.tools.ParticleEffect;
import net.samagames.tools.holograms.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 27/10/2016
 */
class Well extends AbstractInteraction
{
    public static final String TAG = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "Puit magique" + ChatColor.DARK_PURPLE + "] " + ChatColor.RESET;

    private final Location cauldronLocation;
    private final Hologram hologram;
    private final BukkitTask particleTask;

    Well(Hub hub, Location cauldronLocation, Location standsLocation)
    {
        super(hub);

        this.cauldronLocation = cauldronLocation;

        this.hologram = new Hologram(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Puit magique", ChatColor.LIGHT_PURPLE + "Créez des perles avec vos poussières d'étoile");
        this.hologram.generateLines(standsLocation);

        final Location particleCenter = cauldronLocation.clone().add(0.5D, 0.5D, 0.5D);
        this.particleTask = hub.getServer().getScheduler().runTaskTimer(hub, () -> ParticleEffect.CRIT_MAGIC.display(0.5F, 0.5F, 0.5F, 0.35F, 10, particleCenter, 100.0D), 5L, 5L);
    }

    @Override
    public void onDisable()
    {
        this.particleTask.cancel();
    }

    public void onLogin(Player player)
    {
        this.hologram.addReceiver(player);
    }

    public void onLogout(Player player)
    {
        this.hologram.removeReceiver(player);
    }

    @Override
    public void play(Player player)
    {
        this.hub.getGuiManager().openGui(player, new GuiWell(this.hub, this));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1.0F, 1.0F);
    }

    @Override
    public void stop(Player player) { /** Not needed **/ }

    public Location getCauldronLocation()
    {
        return this.cauldronLocation;
    }

    @Override
    public boolean hasPlayer(Player player)
    {
        return this.hub.getGuiManager().getPlayerGui(player) instanceof GuiWell;
    }
}
