package modularTurrets.tileentity.turrets;

import modularTurrets.tileentity.turretBase.TurretBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TurretHead extends TileEntity {

    public Entity target;
    public int ticks;
    public float rotationXY;
    public float rotationXZ;
    public float baseFitRotationX;
    public float baseFitRotationZ;
    public int turretTier;
    public TurretBase base;
    public boolean hasSetSide = false;

    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, var1);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1) {
        par1.setFloat("rotationXY", rotationXY);
        par1.setFloat("rotationXZ", rotationXZ);
        par1.setInteger("ticksBeforeFire", ticks);
        super.writeToNBT(par1);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1) {
        super.readFromNBT(par1);
        this.rotationXY = par1.getFloat("rotationXY");
        this.rotationXZ = par1.getFloat("rotationXZ");
        this.ticks = par1.getInteger("ticksBeforeFire");
    }

    public void setSide() {
        if (hasSetSide) {
            return;
        }

        if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TurretBase) {
            this.baseFitRotationX = 1.56F;
            this.baseFitRotationZ = 1.565F;
            this.hasSetSide = true;
            return;
        }

        if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TurretBase) {
            this.baseFitRotationX = 1.56F;
            this.baseFitRotationZ = 4.705F;
            this.hasSetSide = true;
            return;
        }

        if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TurretBase) {
            this.baseFitRotationX = 1.56F;
            this.baseFitRotationZ = 3.145F;
            this.hasSetSide = true;
            return;
        }

        if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TurretBase) {
            this.baseFitRotationX = 1.56F;
            this.baseFitRotationZ = 0F;
            this.hasSetSide = true;
            return;
        }

        if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TurretBase) {
            this.baseFitRotationX = 3.145F;
            this.baseFitRotationZ = 0F;
            this.hasSetSide = true;
            return;
        }

        if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TurretBase) {
            this.baseFitRotationX = 0F;
            this.baseFitRotationZ = 0F;
            this.hasSetSide = true;
        }
    }

    public TurretBase getBase() {
        return TurretHeadUtils.getTurretBase(worldObj, xCoord, yCoord, zCoord);
    }

    public Entity getTarget() {
	return target;
    }

    public void setTarget(Entity target) {
	this.target = target;
    }

    public int getTicks() {
	return ticks;
    }

    public void setTicks(int ticks) {
	this.ticks = ticks;
    }

    public float getRotationXY() {
	return rotationXY;
    }

    public void setRotationXY(float rotationXY) {
	this.rotationXY = rotationXY;
    }

    public float getRotationXZ() {
	return rotationXZ;
    }

    public void setRotationXZ(float rotationXZ) {
	this.rotationXZ = rotationXZ;
    }

}
