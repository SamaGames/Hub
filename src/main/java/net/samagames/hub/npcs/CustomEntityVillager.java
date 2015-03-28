package net.samagames.hub.npcs;

import net.minecraft.server.v1_8_R1.EntityVillager;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.World;
import org.bukkit.Location;

public class CustomEntityVillager extends EntityVillager
{
    private final Location location;

    public CustomEntityVillager(World world, Location location)
    {
        super(world);
        this.location = location;
    }
    
    @Override
    public void g(float sideMot, float forMot)
    {
        this.j(0.0F);
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
    public void b(NBTTagCompound nbttagcompound) {}

    @Override
    public boolean c(NBTTagCompound nbttagcompound) {
        return false;
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {}

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {
        return false;
    }

    @Override
    public void e(NBTTagCompound nbttagcompound) {}
}
