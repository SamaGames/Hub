package net.samagames.hub.interactions.magicchests;

import net.minecraft.server.v1_9_R1.*;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.tools.ParticleEffect;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

class MagicChest extends AbstractInteraction
{
    private static final String TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Coffre Magique" + ChatColor.DARK_AQUA + "] ";

    private final static int[] PIANO_MAIN = {1, 3, 5, 7, 1, 3, 5, 7, 2, 4, 6, 8, 2, 4, 6, 8, 3, 5, 7, 9, 3, 5, 7, 9, 4, 6, 8, 10, 4, 6, 8, 10, 5, 7, 9, 11, 6, 8, 10, 12, 7, 9, 11, 13, 8, 10, 12, 14, -1, -1, -1, -1, 15, -1, 16, -1, 17, -1, 18, 18, 18, 18, 18, 18};
    private final static int[] PIANO_SECONDARY = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, -1, 22, -1, 23, -1, 24, 24, 24, 24, 24, 24};
    private final static int[] SNARE_DRUM = {1, -1, -1, -1, 24, -1, -1, -1, 3, -1, -1, -1, 24, -1, -1, -1, 5, -1, -1, -1, 24, -1, -1, -1, 7, -1, -1, -1, 24, -1, -1, -1, 9, -1, -1, -1, 17, -1, -1, -1, 17, -1, -1, -1, 17, -1, -1, -1, -1, -1, -1, -1, 24, -1, 24, -1, 24, -1, 24, 24, 24, 24, 24, 24};
    private final static int[] BASS_GUITAR = {0, -1, -1, -1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, 6, -1, -1, -1, -1, -1, -1, -1, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, 21, -1, 22, -1, 23, -1, -1, -1, -1, -1};

    private final static int LENGTH = 16 * 4;

    private final Location location;
    private final Random random;
    private final List<UUID> opening;

    MagicChest(Hub hub, Location location)
    {
        super(hub);

        this.location = location;
        this.random = new Random();
        this.opening = new ArrayList<>();
    }

    @Override
    public void onDisable()
    {
        while (!this.opening.isEmpty())
        {
            /** Lock **/
        }
    }

    @Override
    public void play(Player player)
    {
        this.open(player);
    }

    @Override
    public void stop(Player player) { /** Not needed **/ }

    private void open(Player player)
    {
        this.opening.add(player.getUniqueId());

        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            for(int i = 0; i < LENGTH; i++)
            {
                for(Player p : this.hub.getServer().getOnlinePlayers())
                {
                    this.play(p, this.location, Instrument.PIANO, PIANO_MAIN[i]);
                    this.play(p, this.location, Instrument.PIANO, PIANO_SECONDARY[i]);
                    this.play(p, this.location, Instrument.SNARE_DRUM, SNARE_DRUM[i]);
                    this.play(p, this.location, Instrument.BASS_GUITAR, BASS_GUITAR[i]);

                    if(i == 52)
                    {
                        player.sendMessage(TAG + ChatColor.YELLOW + "Vous avez trouvé un " + ChatColor.GOLD + ChatColor.BOLD + "Gadget Tramposlime");
                        this.sendChestPacket(player, true);

                        this.hub.getServer().getScheduler().runTaskLater(this.hub, () -> sendChestPacket(player, false), 5 * 20L);
                    }
                }

                int wait = i;

                if(i >> 2 >= 12)
                    wait = 12 << 2;

                if(i == 52)
                    new ItemBombTask(this.hub, this.location);

                if(i < 35)
                {
                    double speed = 1 + i / 10;

                    for(int k = 0; k < Math.floor(speed * speed) + 2; k++)
                        ParticleEffect.ENCHANTMENT_TABLE.display(new Vector((0.5 - this.random.nextDouble()) * speed, (0.5 - this.random.nextDouble()) * speed, (0.5 - this.random.nextDouble()) * speed), (float) (speed * 2), this.location.clone().add((0.5 - this.random.nextDouble()) * 2 + 0.5, (0.5 - this.random.nextDouble()) * 2 + 0.5, (0.5 - this.random.nextDouble()) * 2 + 0.5), 40);
                }

                if(i >= 54 && i < 58)
                {
                    double speed = 0.5;

                    for(int k = 0; k < 5; k++)
                        ParticleEffect.FIREWORKS_SPARK.display(new Vector((0.5 - this.random.nextDouble()) * speed, (0.5 - this.random.nextDouble()) * speed, (0.5 - this.random.nextDouble()) * speed), 1F, this.location.clone().add((0.5 - this.random.nextDouble()) * 2 + 0.5, (0.5 - this.random.nextDouble()) * 2 + 0.5, (0.5 - this.random.nextDouble()) * 2 + 0.5), 40);
                }

                if(i == 52)
                {
                    this.hub.getServer().getScheduler().runTask(this.hub, () ->
                    {
                        ArmorStand itemHolder = createArmorStandHologram(this.location.clone().add(0.5, 0.6, 0.5), "");

                        org.bukkit.entity.Item item = this.location.getWorld().dropItem(this.location, new ItemStack(Material.SLIME_BLOCK));
                        item.setPickupDelay(1000);
                        itemHolder.setPassenger(item);

                        ArmorStand treasure = createArmorStandHologram(this.location.clone().add(0.5, 0.75, 0.5), ChatColor.AQUA + "Trésor :");
                        ArmorStand treasureName = createArmorStandHologram(this.location.clone().add(0.5, 0.45, 0.5), ChatColor.GOLD + "" + ChatColor.BOLD + "Gadget Tramposlime");

                        this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
                        {
                            itemHolder.getPassenger().remove();
                            itemHolder.remove();
                            treasure.remove();
                            treasureName.remove();
                        }, 5 * 20L);
                    });

                }

                try
                {
                    Thread.sleep(158 - wait);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }

                this.opening.remove(player.getUniqueId());
            }
        });
    }

    private void play(Player player, Location location, Instrument instrument, int note)
    {
        if(note < 0)
            return;

        player.playNote(location, instrument, new Note(note));
    }


    private void sendChestPacket(Player player, boolean open)
    {
        MinecraftKey key = new MinecraftKey("minecraft:chest");
        Block block = Block.REGISTRY.get(key);
        PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(new BlockPosition(this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ()), block, 1, open ? 1 : 0);
        sendPacket(player, packet);
    }

    private void sendPacket(Player player, Packet packet)
    {
        ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packet);
    }

    private ArmorStand createArmorStandHologram(Location location, String name)
    {
        ArmorStand armorStand = (ArmorStand) this.location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        if(name != null && name.length() != 0)
        {
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(name);
        }

        return armorStand;
    }

    @Override
    public boolean hasPlayer(Player player)
    {
        return this.opening.contains(player.getUniqueId());
    }
}
