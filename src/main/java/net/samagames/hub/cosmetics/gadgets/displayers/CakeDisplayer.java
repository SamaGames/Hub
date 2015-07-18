package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.hub.utils.SimpleBlock;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CakeDisplayer extends AbstractDisplayer
{
	private final Random random;
	private int loopId;
	private Location centerLoc;

	public CakeDisplayer(Player player)
	{
		super(player);

		this.random = new Random();

		this.baseLocation = player.getLocation().getBlock().getLocation().clone().add(0.0D, 1.0D, 0.0D);
		this.addBlockToUse(this.baseLocation.clone().add(0.0D, -1.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 2));
		this.addBlockToUse(this.baseLocation, new SimpleBlock(Material.CAKE_BLOCK));

		centerLoc = this.baseLocation.clone().add(0.5D, 0.5D, 0.5D);
	}

	@Override
	public void display()
	{
	    for (Location block : this.blocksUsed.keySet())
		{
            block.getBlock().setType(this.blocksUsed.get(block).getType());
            block.getBlock().setData(this.blocksUsed.get(block).getData());
        }
        
		FireworkEffect effect = FireworkEffect.builder().flicker(false).trail(false).with(FireworkEffect.Type.BURST).withColor(Color.RED).withFade(Color.PURPLE).withFlicker().build();

		for (int i = 0; i <= 2; i++)
			FireworkUtils.launchfw(this.centerLoc, effect);

		getNearbyPlayers(this.centerLoc, 5).stream().filter(entity -> entity instanceof Player).forEach(entity -> entity.setVelocity(new Vector(0, 2, 0)));

		this.loopId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Hub.getInstance(), new Runnable() {
            int times = 0;

            @Override
            public void run() {
                for (int i = 0; i <= 5; i++)
                    centerLoc.getWorld().playEffect(centerLoc.clone().add((random.nextDouble() * 5) - 2.5, (random.nextDouble() * 4) - 1, (random.nextDouble() * 5) - 2.5), Effect.FLYING_GLYPH, 5);

                for (Entity entity : getNearbyPlayers(centerLoc, 5)) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        double pw = 8;
                        double xa = centerLoc.getX();
                        double ya = centerLoc.getZ();
                        double xb = player.getLocation().getX();
                        double yb = player.getLocation().getZ();
                        double m = (ya - yb) / (xa - xb);
                        double p = ya - m * xa;
                        double alpha = Math.atan(m * xa + p);
                        double xc1 = xb + pw * Math.cos(alpha);
                        double xc2 = xb - pw * Math.cos(alpha);
                        double yc;
                        double xc;

                        if (Math.abs(xa - xc1) > Math.abs(xa - xc2)) {
                            yc = m * xc1 + p;
                            xc = xc1;
                        } else {
                            yc = m * xc2 + p;
                            xc = xc2;
                        }

                        double a = xc - player.getLocation().getX();
                        double b = yc - player.getLocation().getZ();

                        if (b > 10)
                            b = 10;
                        else if (b < -10)
                            b = -10;

                        if (a != 0 && b != 0) {
                            Vector v = new Vector(a / 10, (player.getLocation().getY() - centerLoc.getY()) / 10 + 1, b / 10);
                            player.setVelocity(v);
                            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 2F);

                            int effect = random.nextInt(3);

                            switch (effect) {
                                case 0:
                                    break;
                                case 1:
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 0));
                                    break;
                                case 2:
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
                                    break;
                                case 3:
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120, 4));
                                    break;
                            }
                        }
                    }
                }

                if (times == 10 || times == 20 || times == 30 || times == 40 || times == 50 || times == 60) {
                    baseLocation.getWorld().getBlockAt(baseLocation)
                            .setData((byte) (baseLocation.getWorld().getBlockAt(baseLocation).getData() + 1));
                    centerLoc.getWorld().playSound(centerLoc, Sound.BURP, 2, 1);
                } else if (times == 70) {
                    baseLocation.getWorld().getBlockAt(baseLocation).setType(Material.AIR);
                    baseLocation.getWorld().playSound(baseLocation, Sound.BURP, 2, 1);
                } else if (times >= 80) {
                    FireworkEffect.Builder builder = FireworkEffect.builder();
                    FireworkEffect effect = builder.flicker(false).trail(false).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.BLUE).withFade(Color.PURPLE).withFlicker().build();
                    FireworkUtils.launchfw(baseLocation, effect);

                    restore();
                    end();
                    callback();
                }

                times++;
            }
        }, 5L, 5L);
	}

    @Override
    public void handleInteraction(Entity who, Entity with) {}

	public List<Entity> getNearbyPlayers(Location where, int range)
	{
		List<Entity> found = where.getWorld().getEntities().stream().filter(entity -> distance(where, entity.getLocation()) <= range && entity instanceof Player).collect(Collectors.toList());
		return found;
	}

	public double distance(Location loc1, Location loc2)
	{
		return Math.sqrt((loc1.getX() - loc2.getX()) * (loc1.getX() - loc2.getX()) + (loc1.getY() - loc2.getY()) * (loc1.getY() - loc2.getY()) + (loc1.getZ() - loc2.getZ()) * (loc1.getZ() - loc2.getZ()));
	}

	@Override
	public boolean canUse() {
		return true;
	}

	private void callback() {
		Bukkit.getScheduler().cancelTask(this.loopId);
	}

}
