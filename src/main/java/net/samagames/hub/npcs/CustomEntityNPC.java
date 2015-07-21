package net.samagames.hub.npcs;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.samagames.api.SamaGamesAPI;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CustomEntityNPC extends EntityPlayer
{
    private final Location location;

    public CustomEntityNPC(NPC npc, Location location)
    {
        this(((CraftWorld) location.getWorld()).getHandle(), new GameProfile(npc.getOwner(), SamaGamesAPI.get().getUUIDTranslator().getName(npc.getOwner())), npc.getArmor(), npc.getItemInHand(), location);
    }

    public CustomEntityNPC(WorldServer world, GameProfile profile, ItemStack[] armor, ItemStack itemInHand, Location location)
    {
        super(world.getMinecraftServer(), world, profile, new PlayerInteractManager(world));

        net.minecraft.server.v1_8_R3.ItemStack[] armorNMS = new net.minecraft.server.v1_8_R3.ItemStack[4];
        armorNMS[0] = CraftItemStack.asNMSCopy(armor[0]);
        armorNMS[1] = CraftItemStack.asNMSCopy(armor[1]);
        armorNMS[2] = CraftItemStack.asNMSCopy(armor[2]);
        armorNMS[3] = CraftItemStack.asNMSCopy(armor[3]);

        this.location = location;
        this.inventory.armor = armorNMS;
        this.inventory.items[0] = CraftItemStack.asNMSCopy(itemInHand);
    }
    
    @Override
    public void g(float sideMot, float forMot)
    {
        this.k(0.0F);
        this.motY = 0.0D;
        this.motX = 0.0D;
        this.motZ = 0.0D;
        this.yaw = this.location.getYaw();
        this.pitch = this.location.getPitch();
        this.aI = this.location.getYaw();
        this.aJ = this.location.getYaw();

        super.g(sideMot, forMot);
    }

    @Override
    public void move(double d0, double d1, double d2) {}

    @Override
    public void g(double x, double y, double z)
    {
        Vector vector = this.getBukkitEntity().getVelocity();
        super.g(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    protected String bp() {
        return "";
    }

    @Override
    protected String bo() {
        return "";
    }
    
    @Override
    public void b(NBTTagCompound nbttagcompound) {}

    @Override
    public boolean c(NBTTagCompound nbttagcompound)
    {
        return false;
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {}

    @Override
    public boolean d(NBTTagCompound nbttagcompound)
    {
        return false;
    }

    @Override
    public void e(NBTTagCompound nbttagcompound) {}
}
