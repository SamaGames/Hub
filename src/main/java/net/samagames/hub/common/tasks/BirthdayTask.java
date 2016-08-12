package net.samagames.hub.common.tasks;

import net.samagames.hub.Hub;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.*;

/**
 * Created by Rigner for project Hub.
 */
class BirthdayTask extends AbstractTask
{
    private static final ChatColor[] COLORS = {ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.BLUE, ChatColor.DARK_RED,
            ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.DARK_AQUA, ChatColor.AQUA, ChatColor.GREEN,
            ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW};
    private static final int DELAY = 50;
    private static double[] MY_COS;

    private List<BirthdayInfo> infos;
    private int time;
    private Random random;

    BirthdayTask(Hub hub)
    {
        super(hub);

        this.time = 0;
        this.infos = new ArrayList<>();
        /*                                                  UUID                                MONTH  DAY     PARTICLE                    COLOR            SOUND                          SPECIAL       USERNAME     */
        this.infos.add(new BirthdayInfo(UUID.fromString("29b2b527-1b59-45df-b7b0-d5ab20d8731a"), 7,     7,   Particle.SLIME,             Color.GREEN,    Sound.ENTITY_CAT_AMBIENT,        true));      //IamBlueSlime
        this.infos.add(new BirthdayInfo(UUID.fromString("ad345a5e-5ae3-45bf-aba4-94f4102f37c0"), 3,     19,  Particle.REDSTONE,          Color.RED,      Sound.ENTITY_CHICKEN_AMBIENT,    true));      //Silvanosky
        this.infos.add(new BirthdayInfo(UUID.fromString("c1f45796-d2f9-4622-9475-2afe58324dee"), 2,     17,  Particle.END_ROD,           Color.GRAY,     Sound.ENTITY_SILVERFISH_AMBIENT, true));      //Rigner
        this.infos.add(new BirthdayInfo(UUID.fromString("70481513-fe72-4184-8da2-4c16b92ea98a"), 9,     22,  Particle.DAMAGE_INDICATOR,  Color.RED));                                                  //Reelwens
        this.infos.add(new BirthdayInfo(UUID.fromString("c091f5f0-7505-4188-a070-02c76cb22bba"), 12,    21,  Particle.CLOUD,             Color.WHITE));                                                //Azuxul
        this.infos.add(new BirthdayInfo(UUID.fromString("83748c1b-fd1d-4823-865c-f68bdf106c43"), 7,     16,  Particle.SMOKE_NORMAL,      Color.GRAY));                                                 //LordFinn
        this.infos.add(new BirthdayInfo(UUID.fromString("8502878d-22ba-4a93-bcbd-9319caa9b555"), 10,    9,   Particle.PORTAL,            Color.PURPLE));                                               //Aweser
        this.infos.add(new BirthdayInfo(UUID.fromString("a4ab5d2c-1046-4317-9977-fe410fd2fd36"), 2,     25,  Particle.PORTAL,            Color.PURPLE));                                               //Aritche
        this.infos.add(new BirthdayInfo(UUID.fromString("3a78ccd1-ac6a-4095-8393-cab56d5fbbd5"), 7,     31,  Particle.CRIT,              Color.YELLOW));                                               //NicoKweiik
        this.infos.add(new BirthdayInfo(UUID.fromString("a0737ed1-23c8-46e9-a2ad-3a7f09b3009e"), 4,     15,  Particle.WATER_BUBBLE,      Color.BLUE));                                                 //Yannismx
        this.infos.add(new BirthdayInfo(UUID.fromString("dee5c414-a530-42e5-acd5-1b7a97268f76"), 5,     4,   Particle.DAMAGE_INDICATOR,  Color.BLUE));                                                 //Yumeshi
        this.infos.add(new BirthdayInfo(UUID.fromString("a32704a7-2349-4849-b541-e4f74bb1adb8"), 8,     18,  Particle.TOWN_AURA,         Color.OLIVE));                                                //Flaz_
        this.infos.add(new BirthdayInfo(UUID.fromString("ade6b560-ef07-4cb9-844d-0347333c7d65"), 2,     24,  Particle.SNOW_SHOVEL,       Color.WHITE));                                                //Eisen
        this.infos.add(new BirthdayInfo(UUID.fromString("f79c6c7f-fe45-4502-8f2a-de9f92dcaade"), 8,     12,  Particle.DRIP_WATER,        Color.BLUE));                                                 //TB1144
        this.infos.add(new BirthdayInfo(UUID.fromString("5492f7fe-9fb2-4ed5-ba48-b64cf7446c71"), 1,     8,   Particle.FLAME,             Color.ORANGE));                                               //Piryreis
        this.infos.add(new BirthdayInfo(UUID.fromString("963edc85-c620-4753-a6ef-2b29254dbd03"), 1,     10,  Particle.SNOWBALL,          Color.GRAY));                                                 //Simplement
        this.infos.add(new BirthdayInfo(UUID.fromString("9ac4c3db-e802-4c55-96ca-13d2429cd415"), 4,     17,  Particle.DRIP_LAVA,         Color.RED));                                                  //Vindict
        this.infos.add(new BirthdayInfo(UUID.fromString("5420b265-c7d0-4873-8605-762b9c3e9f9a"), 1,     29,  Particle.NOTE,              Color.NAVY));                                                 //KigenPrice
        this.infos.add(new BirthdayInfo(UUID.fromString("1196a0ee-26b8-4ffa-879f-6cdf8a3d18e8"), 10,    29,  Particle.WATER_WAKE,        Color.BLUE));                                                 //Cubiik
        this.infos.add(new BirthdayInfo(UUID.fromString("378715c8-479b-4c93-993b-813771694c60"), 1,     19,  Particle.ENCHANTMENT_TABLE, Color.TEAL));                                                 //Loucasio
        this.infos.add(new BirthdayInfo(UUID.fromString("a75ca2e3-99e2-4918-8837-b2da7410aa92"), 8,     8,   Particle.SPELL_WITCH,       Color.MAROON));                                               //Apaiz
        this.infos.add(new BirthdayInfo(UUID.fromString("23f80ac3-8988-465b-bd30-d239a5d48d90"), 12,    9,   Particle.REDSTONE,          Color.RED));                                                  //lecancre
        this.infos.add(new BirthdayInfo(UUID.fromString("7b38beed-0488-480c-bc26-fa9c5a1d01e1"), 11,    2,   Particle.BARRIER,           Color.AQUA));                                                 //BillGatesardeche
        this.infos.add(new BirthdayInfo(UUID.fromString("f5dbcb02-0c55-48ee-b7b2-77d3dbd00337"), 9,     19,  Particle.SNOW_SHOVEL,       Color.GRAY));                                                 //Adinium
        this.infos.add(new BirthdayInfo(UUID.fromString("218ea7ef-523c-445e-a8f4-474290df7442"), 3,     25,  Particle.SUSPENDED,         Color.BLUE));                                                 //MinRin_25
        this.infos.add(new BirthdayInfo(UUID.fromString("802a15d5-e6da-497b-8046-c103248b4761"), -1,    -1,  Particle.SNOWBALL,          Color.BLUE));                                                 //Pizzarty
        this.infos.add(new BirthdayInfo(UUID.fromString("8e2a0ebd-297c-40c0-949d-dd57bff76d3c"), 2,     13,  Particle.VILLAGER_ANGRY,    Color.PURPLE));                                               //ArkaCyr
        this.infos.add(new BirthdayInfo(UUID.fromString("2a97caba-5f40-41ca-b72d-8a03e88ca5fd"), 5,     10,  Particle.DRIP_LAVA,         Color.ORANGE));                                               //Azrix
        this.infos.add(new BirthdayInfo(UUID.fromString("b1652003-6509-4d6d-8b46-905ae6b43738"), 5,     28,  Particle.WATER_SPLASH,      Color.AQUA));                                                 //Adrigogo
        this.infos.add(new BirthdayInfo(UUID.fromString("cbaf838c-a82e-4702-a11d-8f84c5a7454f"), 11,    21,  Particle.SMOKE_NORMAL,      Color.BLACK));                                                //Tigger_San
        this.infos.add(new BirthdayInfo(UUID.fromString("5f52f8fb-0a83-4ff0-a970-3cafaa45b52c"), 8,     12,  Particle.HEART,             Color.BLUE));                                                 //Lettoh
        this.infos.add(new BirthdayInfo(UUID.fromString("4a8938d2-fd0d-43d8-bb57-d58e733959db"), 12,    6,   Particle.LAVA,              Color.RED));                                                  //Zokwo

        this.task = this.hub.getServer().getScheduler().runTaskTimer(hub, this, 2L, 2L);
        this.random = new Random();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run()
    {
        Date date = new Date(System.currentTimeMillis());
        this.infos.forEach(info ->
        {
            Player player = null;
            if (date.getMonth() + 1 == info.month && date.getDate() == info.day && (player = this.hub.getServer().getPlayer(info.uuid)) != null)
            {
                if (!info.special)
                    player.getWorld().spawnParticle(info.particle, player.getLocation().add(0D, 1D, 0D), 3, 0.3D, 1D, 0.3D, 0.1D);
                else
                    player.getWorld().spawnParticle(info.particle, player.getLocation().add(0D, 1D, 0D).add(BirthdayTask.MY_COS[this.time + BirthdayTask.DELAY], BirthdayTask.MY_COS[this.time], BirthdayTask.MY_COS[this.time + BirthdayTask.DELAY * 2]), 2, 0D, 0D, 0D, 0D);
                if (this.time == 0)
                {
                    Firework fw = (Firework)player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                    FireworkMeta fwm = fw.getFireworkMeta();
                    fwm.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withTrail().withFlicker().withColor(info.color).build());
                    fw.setFireworkMeta(fwm);
                }
                if (this.time % BirthdayTask.DELAY / 4 == 0 && info.sound != null)
                    player.getWorld().playSound(player.getLocation(), info.sound, 0.5F, 0.5F);
                if (info.armorStand == null || info.armorStand.isDead())
                {
                    info.armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0D, 0.5D, 0D), EntityType.ARMOR_STAND);
                    info.armorStand.setVisible(false);
                    info.armorStand.setCustomNameVisible(true);
                    info.armorStand.setGravity(false);
                    this.hub.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "C'est l'anniversaire de " + player.getName() + ", n'oubliez pas de lui souhaiter ! :D");
                }
                info.armorStand.teleport(player.getLocation().add(0D, 0.5D, 0D));
                info.armorStand.setCustomName(BirthdayTask.COLORS[random.nextInt(BirthdayTask.COLORS.length)] + "Joyeux anniversaire " + player.getName());
            }
            else if (info.armorStand != null)
            {
                info.armorStand.remove();
                info.armorStand = null;
            }
        });
        this.time++;
        this.time %= BirthdayTask.DELAY;
    }

    private static class BirthdayInfo
    {
        private UUID uuid;
        private int month;
        private int day;
        private Particle particle;
        private ArmorStand armorStand;
        private Color color;
        private Sound sound;
        private boolean special;

        BirthdayInfo(UUID uuid, int month, int day, Particle particle, Color color)
        {
            this(uuid, month, day, particle, color, null);
        }

        BirthdayInfo(UUID uuid, int month, int day, Particle particle, Color color, Sound sound)
        {
            this(uuid, month, day, particle, color, sound, false);
        }

        BirthdayInfo(UUID uuid, int month, int day, Particle particle, Color color, Sound sound, boolean special)
        {
            this.uuid = uuid;
            this.month = month;
            this.day = day;
            this.particle = particle;
            this.armorStand = null;
            this.color = color;
            this.sound = sound;
            this.special = special;
        }
    }

    static
    {
        BirthdayTask.MY_COS = new double[BirthdayTask.DELAY * 3];
        for (int i = 0; i < BirthdayTask.DELAY; i++)
        {
            BirthdayTask.MY_COS[i] = Math.cos(Math.PI * (double)i / (double)BirthdayTask.DELAY);
            BirthdayTask.MY_COS[i + BirthdayTask.DELAY] = Math.cos(6D * Math.PI * (double)i / (double)BirthdayTask.DELAY);
            BirthdayTask.MY_COS[i + BirthdayTask.DELAY * 2] = Math.sin(6D * Math.PI * (double)i / (double)BirthdayTask.DELAY);
        }
    }
}
