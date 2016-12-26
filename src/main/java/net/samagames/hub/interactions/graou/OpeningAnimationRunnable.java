package net.samagames.hub.interactions.graou;

import net.minecraft.server.v1_10_R1.WorldServer;
import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.interactions.graou.entity.EntityGraouLaser;
import net.samagames.hub.interactions.graou.entity.EntityGraouLaserTarget;
import net.samagames.tools.ItemUtils;
import net.samagames.tools.ParticleEffect;
import net.samagames.tools.ProximityUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;
import java.util.Random;

class OpeningAnimationRunnable implements Runnable
{
    private static final String[] PRESENT_TEXTURES = new String[] {
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUzYThmZDA4NTI5Nzc0NDRkOWZkNzc5N2NhYzA3YjhkMzk0OGFkZGM0M2YwYmI1Y2UyNWFlNzJkOTVkYyJ9fX0=",     // Orange
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjczYTIxMTQxMzZiOGVlNDkyNmNhYTUxNzg1NDE0MDM2YTJiNzZlNGYxNjY4Y2I4OWQ5OTcxNmM0MjEifX19",         // Red
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg1MzZhNDYxNjg0ZmM3YTYzYjU0M2M1ZGYyMzQ4Y2Q5NjhiZjU1ODM1OTFiMWJiY2M1ZjBkYjgzMTY2ZGM3In19fQ==", // Grey blue
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDNlMTY1ODU3YzRjMmI3ZDZkOGQ3MGMxMDhjYzZkNDY0ZjdmMzBlMDRkOTE0NzMxYjU5ZTMxZDQyNWEyMSJ9fX0=",     // Dark blue
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODRlMWM0MmYxMTM4M2I5ZGM4ZTY3ZjI4NDZmYTMxMWIxNjMyMGYyYzJlYzdlMTc1NTM4ZGJmZjFkZDk0YmI3In19fQ==", // Light blue
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWMzODIxZDRmNjFiMTdmODJmMGQ3YThlNTMxMjYwOGZmNTBlZGUyOWIxYjRkYzg5ODQ3YmU5NDI3ZDM2In19fQ=="      // Grey
    };

    private static final int[] PIANO_MAIN = { 1, 3, 5, 7, 1, 3, 5, 7, 2, 4, 6, 8, 2, 4, 6, 8, 3, 5, 7, 9, 3, 5, 7, 9, 4, 6, 8, 10, 4, 6, 8, 10, 5, 7, 9, 11, 6, 8, 10, 12, 7, 9, 11, 13, 8, 10, 12, 14, -1, -1, -1, -1, 15, -1, 16, -1, 17, -1, 18, 18, 18, 18, 18, 18 };
    private static final int[] PIANO_SECONDARY = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, -1, 22, -1, 23, -1, 24, 24, 24, 24, 24, 24 };
    private static final int[] SNARE_DRUM = { 1, -1, -1, -1, 24, -1, -1, -1, 3, -1, -1, -1, 24, -1, -1, -1, 5, -1, -1, -1, 24, -1, -1, -1, 7, -1, -1, -1, 24, -1, -1, -1, 9, -1, -1, -1, 17, -1, -1, -1, 17, -1, -1, -1, 17, -1, -1, -1, -1, -1, -1, -1, 24, -1, 24, -1, 24, -1, 24, 24, 24, 24, 24, 24 };
    private static final int[] BASS_GUITAR = { 0, -1, -1, -1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, 6, -1, -1, -1, -1, -1, -1, -1, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, 21, -1, 22, -1, 23, -1, -1, -1, -1, -1 };

    private final Hub hub;
    private final Graou graou;
    private final Player player;
    private final Pearl pearl;
    private final Location door;
    private final Location treasureLocation;
    private final Location openingLocation;
    private Item present;
    private ArmorStand presentHolder;

    OpeningAnimationRunnable(Hub hub, Graou graou, Player player, Pearl pearl, Location door, Location treasureLocation, Location openingLocation)
    {
        this.hub = hub;
        this.graou = graou;
        this.player = player;
        this.pearl = pearl;
        this.door = door;
        this.treasureLocation = treasureLocation;
        this.openingLocation = openingLocation;
    }

    @Override
    public void run()
    {
        this.graou.toggleHolograms(false);
        this.graou.getGraouEntity().setSitting(false);

        this.door.getBlock().setData((byte) 11);
        this.door.getBlock().getState().update(true);

        this.walk(this.treasureLocation, this::arrivedAtTreasure);
    }

    private void arrivedAtTreasure()
    {
        this.treasureLocation.getWorld().playSound(this.treasureLocation, Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
        {
            this.treasureLocation.getWorld().playSound(this.treasureLocation, Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.0F);

            final String selectedPresentTexture = PRESENT_TEXTURES[new Random().nextInt(PRESENT_TEXTURES.length)];

            this.present = this.treasureLocation.getWorld().dropItem(this.treasureLocation, ItemUtils.getCustomHead(selectedPresentTexture));
            this.graou.getGraouEntity().getBukkitEntity().setPassenger(this.present);

            this.walk(this.openingLocation, () ->
            {
                this.door.getBlock().setData((byte) 7);
                this.door.getBlock().getState().update(true);

                this.presentHolder = this.openingLocation.getWorld().spawn(this.openingLocation.clone().subtract(0.0D, 1.5D, 0.0D), ArmorStand.class);
                this.presentHolder.setVisible(false);
                this.presentHolder.setGravity(false);
                this.presentHolder.setInvulnerable(true);
                this.presentHolder.setHelmet(ItemUtils.getCustomHead(selectedPresentTexture));

                this.graou.getGraouEntity().getBukkitEntity().eject();
                this.present.remove();

                this.graou.respawn();
                this.graou.getGraouEntity().setSitting(true);

                this.placedPresent();
            });
        }, 15L);
    }

    private void placedPresent()
    {
        new Thread()
        {
            private EntityGraouLaserTarget fakeTarget;
            private EntityGraouLaser[] lasers;
            private double angle = 0.0D;

            @Override
            public void run()
            {
                for (int i = 0; i < 64; i++)
                {
                    for (Player p : OpeningAnimationRunnable.this.hub.getServer().getOnlinePlayers())
                    {
                        OpeningAnimationRunnable.this.playNote(p, OpeningAnimationRunnable.this.openingLocation, Instrument.PIANO, PIANO_MAIN[i]);
                        OpeningAnimationRunnable.this.playNote(p, OpeningAnimationRunnable.this.openingLocation, Instrument.PIANO, PIANO_SECONDARY[i]);
                        OpeningAnimationRunnable.this.playNote(p, OpeningAnimationRunnable.this.openingLocation, Instrument.SNARE_DRUM, SNARE_DRUM[i]);
                        OpeningAnimationRunnable.this.playNote(p, OpeningAnimationRunnable.this.openingLocation, Instrument.BASS_GUITAR, BASS_GUITAR[i]);
                    }

                    int wait = i;

                    if (i >> 2 >= 12)
                        wait = 12 << 2;

                    if (i == 0)
                    {
                        OpeningAnimationRunnable.this.openingLocation.getWorld().playSound(OpeningAnimationRunnable.this.openingLocation, Sound.ENTITY_CREEPER_PRIMED, 1.0F, 0.85F);

                        OpeningAnimationRunnable.this.hub.getServer().getScheduler().runTask(OpeningAnimationRunnable.this.hub, () ->
                        {
                            WorldServer world = ((CraftWorld) OpeningAnimationRunnable.this.openingLocation.getWorld()).getHandle();

                            this.fakeTarget = new EntityGraouLaserTarget(world);
                            this.fakeTarget.setPosition(OpeningAnimationRunnable.this.openingLocation.getX(), OpeningAnimationRunnable.this.openingLocation.getY(), OpeningAnimationRunnable.this.openingLocation.getZ());
                            ((Squid) this.fakeTarget.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

                            world.addEntity(this.fakeTarget, CreatureSpawnEvent.SpawnReason.CUSTOM);

                            this.lasers = new EntityGraouLaser[4];

                            for (int j = 0; j < 4; j++)
                            {
                                EntityGraouLaser laser = new EntityGraouLaser(world);

                                laser.setPosition(OpeningAnimationRunnable.this.openingLocation.getX(), OpeningAnimationRunnable.this.openingLocation.getY(), OpeningAnimationRunnable.this.openingLocation.getZ());
                                world.addEntity(laser, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                laser.setGoalTarget(this.fakeTarget, EntityTargetEvent.TargetReason.CUSTOM, false);
                                ((Guardian) laser.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

                                this.lasers[j] = laser;
                            }
                        });
                    }
                    else if (i == 48)
                    {
                        OpeningAnimationRunnable.this.hub.getServer().getScheduler().runTask(OpeningAnimationRunnable.this.hub, () ->
                        {
                            OpeningAnimationRunnable.this.openingLocation.getWorld().createExplosion(OpeningAnimationRunnable.this.openingLocation.getBlockX(), OpeningAnimationRunnable.this.openingLocation.getBlockY(), OpeningAnimationRunnable.this.openingLocation.getBlockZ(), 2.0F, false, false);
                            OpeningAnimationRunnable.this.presentHolder.remove();

                            OpeningAnimationRunnable.this.finishAnimation();
                        });
                    }
                    else if (i == 46)
                    {
                        for (EntityGraouLaser laser : this.lasers)
                            laser.getBukkitEntity().remove();

                        this.fakeTarget.die();
                    }
                    else if (i < 46)
                    {
                        int finalI = i;

                        OpeningAnimationRunnable.this.hub.getServer().getScheduler().runTask(OpeningAnimationRunnable.this.hub, () ->
                        {
                            for (int j = 0; j < 4; j++)
                                this.lasers[j].getBukkitEntity().teleport(OpeningAnimationRunnable.this.openingLocation.clone().add(Math.cos(this.angle + Math.PI * j / 2) * (finalI / 4), 6, Math.sin(this.angle + Math.PI * j / 2) * (finalI / 4)));
                        });

                        this.angle += 0.25D;

                        if (this.angle > Math.PI * 2.0D)
                            this.angle = 0.0D;

                        for (int j = 0; j < (i / 4); j++)
                            ParticleEffect.FIREWORKS_SPARK.display(0.1F, 0.1F, 0.1F, 0.5F, 5, OpeningAnimationRunnable.this.openingLocation.clone().add(0.0D, 0.25D, 0.0D), 150.0D);
                    }

                    try
                    {
                        Thread.sleep(158 - wait);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void finishAnimation()
    {
        ArmorStand holder = this.openingLocation.getWorld().spawn(this.openingLocation.clone().subtract(0.0D, 1.35D, 0.0D), ArmorStand.class);
        holder.setVisible(false);
        holder.setInvulnerable(true);
        holder.setGravity(false);
        holder.setCustomNameVisible(true);

        AbstractCosmetic unlockedCosmetic = this.hub.getInteractionManager().getGraouManager().getPearlLogic().unlockRandomizedCosmetic(this.player, this.pearl, this.openingLocation);
        Item unlockedCosmeticItem = this.openingLocation.getWorld().dropItem(this.openingLocation, unlockedCosmetic.getIcon());

        holder.setCustomName(unlockedCosmetic.getIcon().getItemMeta().getDisplayName());
        holder.setPassenger(unlockedCosmeticItem);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
        {
            unlockedCosmeticItem.remove();
            holder.remove();

            this.graou.animationFinished(OpeningAnimationRunnable.this.player, OpeningAnimationRunnable.this.pearl);
        }, 20L * 5);
    }

    private void walk(Location location, Runnable callback)
    {
        this.graou.getGraouEntity().getPathfinderGoalWalkToTile().setTileToWalk(location.getX(), location.getY(), location.getZ());

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Optional<Entity> entity = ProximityUtils.getNearbyEntities(location, 3.0D).stream()
                        .filter(e -> e.getUniqueId() != null)
                        .filter(e -> e.getUniqueId() == OpeningAnimationRunnable.this.graou.getGraouEntity().getUniqueID())
                        .findAny();

                if (entity.isPresent())
                {
                    OpeningAnimationRunnable.this.graou.getGraouEntity().getPathfinderGoalWalkToTile().cancel();
                    OpeningAnimationRunnable.this.hub.getServer().getScheduler().runTask(OpeningAnimationRunnable.this.hub, callback);

                    this.cancel();
                }
            }
        }.runTaskTimer(this.hub, 5L, 5L);
    }

    private void playNote(Player player, Location location, Instrument instrument, int note)
    {
        if (note < 0)
            return;

        player.playNote(location, instrument, new Note(note));
    }
}
