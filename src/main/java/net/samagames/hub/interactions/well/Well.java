package net.samagames.hub.interactions.well;

import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.tools.holograms.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 27/10/2016
 */
class Well extends AbstractInteraction
{
    private final Location cauldronLocation;
    private final Hologram hologram;

    Well(Hub hub, Location cauldronLocation, Location standsLocation)
    {
        super(hub);

        this.cauldronLocation = cauldronLocation;

        this.hologram = new Hologram(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Puit magique", ChatColor.LIGHT_PURPLE + "Créez des perles avec vos poussières d'étoile");
        this.hologram.generateLines(standsLocation);
    }

    @Override
    public void onDisable()
    {

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

    }

    @Override
    public void stop(Player player)
    {

    }

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
