package openmodularturrets.tileentity.turrets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import openmodularturrets.entity.projectiles.TurretProjectile;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.reference.Reference;
import openmodularturrets.tileentity.turretbase.TurretBase;
import openmodularturrets.util.TurretHeadUtil;

import java.util.Random;

public abstract class TurretHead extends TileEntity implements ITickable {
    int ticks;
    int targetingTicks;
    public float rotationXY;
    public float rotationXZ;
    public float baseFitRotationX;
    public float baseFitRotationZ;
    int turretTier;
    public TurretBase base;
    private boolean hasSetSide = false;
    public Entity target = null;
    public float rotationAnimation = 0.00F;
    public boolean shouldConceal = false;
    private boolean playedDeploy = false;
    private int ticksWithoutTarget;

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.pos, 2, var1);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound var1 = pkt.getNbtCompound();
        readFromNBT(var1);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1) {
        par1.setFloat("rotationXY", rotationXY);
        par1.setFloat("rotationXZ", rotationXZ);
        par1.setInteger("ticksBeforeFire", ticks);
        par1.setBoolean("shouldConceal", shouldConceal);
        super.writeToNBT(par1);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1) {
        super.readFromNBT(par1);
        this.rotationXY = par1.getFloat("rotationXY");
        this.rotationXZ = par1.getFloat("rotationXZ");
        this.shouldConceal = par1.getBoolean("shouldConceal");
    }

    void setSide() {
        if (hasSetSide) {
            return;
        }

        if (worldObj.getTileEntity(this.pos.east()) instanceof TurretBase) {
            this.baseFitRotationX = 1.56F;
            this.baseFitRotationZ = 1.565F;
            this.hasSetSide = true;
            return;
        }

        if (worldObj.getTileEntity(this.pos.west()) instanceof TurretBase) {
            this.baseFitRotationX = 1.56F;
            this.baseFitRotationZ = 4.705F;
            this.hasSetSide = true;
            return;
        }

        if (worldObj.getTileEntity(this.pos.south()) instanceof TurretBase) {
            this.baseFitRotationX = 1.56F;
            this.baseFitRotationZ = 3.145F;
            this.hasSetSide = true;
            return;
        }

        if (worldObj.getTileEntity(this.pos.north()) instanceof TurretBase) {
            this.baseFitRotationX = 1.56F;
            this.baseFitRotationZ = 0F;
            this.hasSetSide = true;
            return;
        }

        if (worldObj.getTileEntity(this.pos.up()) instanceof TurretBase) {
            this.baseFitRotationX = 3.145F;
            this.baseFitRotationZ = 0F;
            this.hasSetSide = true;
            return;
        }

        if (worldObj.getTileEntity(this.pos.down()) instanceof TurretBase) {
            this.baseFitRotationX = 0F;
            this.baseFitRotationZ = 0F;
            this.hasSetSide = true;
        }
    }

    Entity getTargetWithMinRange() {
        return TurretHeadUtil.getTargetWithMinimumRange(base, worldObj, base.getyAxisDetect(), this.pos,
                                                        getTurretRange() + TurretHeadUtil.getRangeUpgrades(base), this);
    }

    Entity getTargetWithoutEffect() {
        return TurretHeadUtil.getTargetWithoutSlowEffect(base, worldObj, base.getyAxisDetect(), this.pos,
                                                         getTurretRange() + TurretHeadUtil.getRangeUpgrades(base),
                                                         this);
    }

    private Entity getTarget() {
        return TurretHeadUtil.getTarget(base, worldObj, base.getyAxisDetect(), this.pos,
                                        getTurretRange() + TurretHeadUtil.getRangeUpgrades(base), this);
    }

    protected abstract int getTurretRange();

    TurretBase getBase() {
        return TurretHeadUtil.getTurretBase(worldObj, this.pos);
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

    private float getDistanceToEntity(Entity p_70032_1_) {
        float f = (float) (this.getPos().getX() - p_70032_1_.posX);
        float f1 = (float) (this.getPos().getY() - p_70032_1_.posY);
        float f2 = (float) (this.getPos().getZ() - p_70032_1_.posZ);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    protected abstract int getTurretPowerUsage();

    protected abstract int getTurretFireRate();

    protected abstract double getTurretAccuracy();

    protected abstract boolean requiresAmmo();

    protected abstract boolean requiresSpecificAmmo();

    protected abstract Item getAmmo();

    protected abstract TurretProjectile createProjectile(World world, Entity target, ItemStack ammo);

    protected abstract String getLaunchSoundEffect();

    boolean chebyshevDistance(Entity target, TurretBase base) {
        return MathHelper.abs_max(MathHelper.abs_max(target.posX - this.getPos().getX(), target.posY - this.getPos().getY()),
                                  target.posZ - this.getPos().getZ()) > (getTurretRange() + TurretHeadUtil.getRangeUpgrades(
                base));
    }

    private int getPowerRequiredForNextShot() {
        return Math.round(this.getTurretPowerUsage() * (1 - TurretHeadUtil.getEfficiencyUpgrades(
                base)) * (1 + TurretHeadUtil.getScattershotUpgrades(base)));
    }

    private void updateRotationAnimation() {
        if (rotationAnimation >= 360F) {
            rotationAnimation = 0F;
        }
        rotationAnimation = rotationAnimation + 0.03F;
    }

    @Override
    public void update() {

        setSide();
        this.base = getBase();

        if (worldObj.isRemote) {
            updateRotationAnimation();
            return;
        }

        if (ticks % 5 == 0) {
            worldObj.markBlockForUpdate(this.pos);
        }

        ticks++;

        //Base checks
        if (base == null || base.getBaseTier() < this.turretTier) {
            this.getWorld().destroyBlock(this.pos, true);
        } else {
            if (base.isAttacksPlayers() && base.isActive() && ConfigHandler.globalCanTargetPlayers) {
                TurretHeadUtil.warnPlayers(base, base.getWorld(), base.getyAxisDetect(), this.pos, getTurretRange());
            }

            //Real time tick updates
            TurretHeadUtil.updateSolarPanelAddon(base);
            concealmentChecks();

            if (!base.isActive()) {
                return;
            }

            //turret tick rate;
            if (target == null && targetingTicks < ConfigHandler.getTurretTargetSearchTicks()) {
                targetingTicks++;
                return;
            }
            targetingTicks = 0;

            // power check
            if ((base.getEnergyStored(EnumFacing.DOWN) < getPowerRequiredForNextShot())) {
                return;
            }

            // is there a target, and Has it died in the previous tick?
            if (target == null || target.isDead || this.getWorld().getEntityByID(
                    target.getEntityId()) == null || ((EntityLivingBase) target).getHealth() <= 0.0F) {
                target = getTarget();
            }

            // did we even get a target previously?
            if (target == null) {
                return;
            }

            //set where the turret is aiming at.
            this.rotationXZ = TurretHeadUtil.getAimYaw(target, this.pos) + 3.2F;
            this.rotationXY = TurretHeadUtil.getAimPitch(target, this.pos);

            // has cooldown passed?
            if (ticks < (this.getTurretFireRate() * (1 - TurretHeadUtil.getFireRateUpgrades(base)))) {
                return;
            }

            //Player checks: is target in creative mode?
            if (target != null && target instanceof EntityPlayerMP) {
                EntityPlayerMP entity = (EntityPlayerMP) target;

                if (TurretHeadUtil.isTrustedPlayer(entity.getUniqueID(),
                                                   base) || entity.capabilities.isCreativeMode || !base.isAttacksPlayers()) {
                    target = null;
                    return;
                }
            }

            // Can the turret still see the target? (It's moving)
            if (target != null) {
                if (!TurretHeadUtil.canTurretSeeTarget(this, (EntityLivingBase) target)) {
                    target = null;
                    return;
                }
            }

            //Is the target out of range now?
            if (target != null) {
                if (chebyshevDistance(target, base)) {
                    target = null;
                    return;
                }
            }

            //Finally, try to shoot if criteria is met.
            ItemStack ammo = null;
            if (this.requiresAmmo()) {
                if (this.requiresSpecificAmmo()) {
                    for (int i = 0; i <= TurretHeadUtil.getScattershotUpgrades(base); i++) {
                        ammo = TurretHeadUtil.useSpecificItemStackItemFromBase(base, this.getAmmo());
                        if (ammo == null) {
                            ammo = TurretHeadUtil.getSpecificItemFromInvExpanders(worldObj,
                                                                                  new ItemStack(this.getAmmo()), base);
                        }
                    }
                } else {
                    for (int i = 0; i <= TurretHeadUtil.getScattershotUpgrades(base); i++) {
                        ammo = TurretHeadUtil.useAnyItemStackFromBase(base);
                        if (ammo == null) {
                            ammo = TurretHeadUtil.getAnyItemFromInvExpanders(worldObj, base);
                        }
                    }
                }

                // Is there ammo?
                if (ammo == null) {
                    return;
                }
            }

            // Consume energy
            base.setEnergyStored(base.getEnergyStored(EnumFacing.DOWN) - getPowerRequiredForNextShot());

            for (int i = 0; i <= TurretHeadUtil.getScattershotUpgrades(base); i++) {
                TurretProjectile projectile = this.createProjectile(this.getWorld(), target, ammo);

                projectile.setPosition(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);

                Random random = new Random();

//                if ((projectile.amp_level = TurretHeadUtil.getAmpLevel(base)) != 0) {
//                    worldObj.playSoundEffect(this.pos.getX(), this.pos.getY(), this.pos.getZ(), Reference.MOD_ID + ":amped",
//                            ConfigHandler.getTurretSoundVolume(), random.nextFloat() + 0.5F);
//                    projectile.isAmped = true;
//                }

                double d0 = target.posX - projectile.posX;
                double d1 = target.posY + (double) target.getEyeHeight() - projectile.posY;
                double d2 = target.posZ - projectile.posZ;
                double dist = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
                float f1 = (float) dist * (0.2F * (getDistanceToEntity(target) * 0.04F));
                double accuraccy = this.getTurretAccuracy() * (1 - TurretHeadUtil.getAccuraccyUpgrades(
                        base)) * (1 + TurretHeadUtil.getScattershotUpgrades(base));

                double time = dist / (projectile.gravity == 0.00F ? 3.0 : 1.6); // For
                // target
                // leading
                // estimation
                if (projectile.gravity == 0.00F) {
                    projectile.setThrowableHeading(d0 + target.motionX * time, d1 + target.motionY,
                                                   d2 + target.motionZ * time, 3.0F, (float) accuraccy);
                } else {
                    projectile.setThrowableHeading(d0 + target.motionX * time, d1 + (double) f1 + target.motionY,
                                                   d2 + target.motionZ * time, 1.6F, (float) accuraccy);
                }

                this.getWorld().playSoundEffect(this.pos.getX(), this.pos.getY(), this.pos.getZ(),
                                                   Reference.MOD_ID + ":" + this.getLaunchSoundEffect(),
                                                   ConfigHandler.getTurretSoundVolume(), random.nextFloat() + 0.5F);
                this.getWorld().spawnEntityInWorld(projectile);
            }
            ticks = 0;
        }
    }

    void concealmentChecks() {
        if (base != null && base.shouldConcealTurrets) {
            if (!shouldConceal && target == null && ticksWithoutTarget >= 40) {
                ticksWithoutTarget = 0;
                shouldConceal = true;
                playedDeploy = false;
                worldObj.playSoundEffect(this.pos.getX(), this.pos.getY(), this.pos.getZ(), Reference.MOD_ID + ":turretRetract",
                                         ConfigHandler.getTurretSoundVolume(), new Random().nextFloat() + 0.5F);
                worldObj.getBlockState(this.pos).getBlock().setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            } else {
                ticksWithoutTarget++;
            }

            if (base != null && target != null) {
                ticksWithoutTarget = 0;
                shouldConceal = false;

                if (!playedDeploy) {
                    worldObj.playSoundEffect(this.pos.getX(), this.pos.getY(), this.pos.getZ(), Reference.MOD_ID + ":turretDeploy",
                                             ConfigHandler.getTurretSoundVolume(), new Random().nextFloat() + 0.5F);
                    playedDeploy = true;
                    worldObj.getBlockState(this.pos).getBlock().setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 1F, 0.8F);
                }
            }
        } else {
            this.shouldConceal = false;
            worldObj.getBlockState(this.pos).getBlock().setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 1F, 0.8F);
        }
    }
}
