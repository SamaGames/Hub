package net.samagames.hub.interactions.meow;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.tools.holograms.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftItem;
import org.bukkit.entity.Item;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

class Meow extends AbstractInteraction
{
    protected static final String TAG = ChatColor.GOLD + "[" + ChatColor.YELLOW + "Meow" + ChatColor.GOLD + "] " + ChatColor.RESET;
    private static final String MEOW_NAME = ChatColor.GOLD + "" + ChatColor.BOLD + "Meow";

    private final Map<UUID, Hologram> holograms;
    private final Ocelot ocelot;
    private final Random random;
    private BukkitTask thankYouTask;
    private boolean lock;

    Meow(Hub hub, Location location)
    {
        super(hub);

        this.holograms = new HashMap<>();

        this.ocelot = location.getWorld().spawn(location, Ocelot.class);
        this.ocelot.setCatType(Ocelot.Type.SIAMESE_CAT);
        this.ocelot.teleport(location);
        this.ocelot.setSitting(true);

        this.random = new Random();
        this.thankYouTask = null;
        this.lock = false;
    }

    @Override
    public void onDisable()
    {
        this.thankYouTask.cancel();
        this.ocelot.remove();
    }

    public void onLogin(Player player)
    {
        if (this.holograms.containsKey(player.getUniqueId()))
            this.onLogout(player);

        int fishes = 1;

        Hologram hologram;

        if (fishes > 0)
            hologram = new Hologram(MEOW_NAME, ChatColor.GOLD + "" + fishes + ChatColor.YELLOW + " poisson" + (fishes > 1 ? "s" : "") + " disponible" + (fishes > 1 ? "s" : ""));
        else
            hologram = new Hologram(MEOW_NAME);

        hologram.generateLines(this.ocelot.getLocation().clone().add(0.0D, 1.5D, 0.0D));
        hologram.addReceiver(player);

        this.holograms.put(player.getUniqueId(), hologram);

        if (fishes > 0)
        {
            player.sendMessage(TAG + ChatColor.YELLOW + "Vous avez " + ChatColor.GOLD + fishes + ChatColor.YELLOW + " poisson" + (fishes > 1 ? "s" : "") + " à récupérer ! Venez me voir :)");
            player.playSound(ocelot.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.5F);
        }
    }

    public void onLogout(Player player)
    {
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
        this.hub.getGuiManager().openGui(player, new GuiMeow(this.hub, this));
    }

    @Override
    public void stop(Player player) { /** Not needed **/ }

    public void update(Player player)
    {
        int fishes = 1;

        Hologram hologram = this.holograms.get(player.getUniqueId());

        if (fishes > 0)
            hologram.change(MEOW_NAME, ChatColor.GOLD + "" + fishes + ChatColor.YELLOW + " poisson" + (fishes > 1 ? "s" : "") + " disponible" + (fishes > 1 ? "s" : ""));
        else
            hologram.change(MEOW_NAME);
    }

    public void playThankYou()
    {
        if (this.lock)
            return;

        this.ocelot.getWorld().playSound(ocelot.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.5F);

        this.thankYouTask = new BukkitRunnable()
        {
            int times = 0;

            @Override
            public void run()
            {
                Item fish = ocelot.getWorld().dropItem(ocelot.getLocation(), new ItemStack(Material.RAW_FISH, 1));
                fish.setVelocity(new Vector(random.nextFloat() * 2 - 1, 1.5F, random.nextFloat() * 2 - 1));

                try
                {
                    GadgetManager.AGE_FIELD.set(((CraftItem) fish).getHandle(), 5860);
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }

                ocelot.getWorld().playSound(ocelot.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0F, 2.0F);

                this.times++;

                if (this.times == 20)
                {
                    lock = false;
                    this.cancel();
                }
            }
        }.runTaskTimer(this.hub, 10L, 10L);
    }

    @Override
    public boolean hasPlayer(Player player)
    {
        return this.hub.getGuiManager().getPlayerGui(player) instanceof GuiMeow;
    }
}
