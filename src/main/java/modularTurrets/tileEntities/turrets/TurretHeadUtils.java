package modularTurrets.tileEntities.turrets;

import java.util.ArrayList;
import java.util.List;

import modularTurrets.items.AccuraccyUpgradeItem;
import modularTurrets.items.RedstoneReactorAddonItem;
import modularTurrets.items.DamageAmpAddonItem;
import modularTurrets.items.EfficiencyUpgradeItem;
import modularTurrets.items.FireRateUpgradeItem;
import modularTurrets.items.RangeUpgradeItem;
import modularTurrets.items.SolarPanelAddonItem;
import modularTurrets.misc.ConfigHandler;
import modularTurrets.misc.Constants;
import modularTurrets.tileEntities.turretBase.TurretBase;
import modularTurrets.tileEntities.turretBase.TurretWoodBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TurretHeadUtils {

    private static ArrayList warnList = new ArrayList();

    public static void warnPlayers(TurretBase base, World worldObj,
	    int downLowAmount, int xCoord, int yCoord, int zCoord,
	    int turretRange) {

	if (base.attacksPlayers) {
	    if (worldObj.getTotalWorldTime() % 160 == 0) {
		warnList.clear();
	    }

	    int warnDistance = ConfigHandler.getTurretWarningDistance();
	    AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(xCoord
		    - turretRange - warnDistance, yCoord - downLowAmount
		    - warnDistance, zCoord - turretRange - warnDistance, xCoord
		    + turretRange + warnDistance, yCoord + turretRange
		    + warnDistance, zCoord + turretRange + warnDistance);
	    List<Entity> targets = worldObj.getEntitiesWithinAABB(
		    EntityPlayerMP.class, axis);

	    for (int i = 0; i < targets.size(); i++) {
		EntityPlayerMP entity = (EntityPlayerMP) targets.get(i);
		if (!entity.username.equals(base.getOwner())
			&& !isTrustedPlayer(entity.username, base)) {
		    dispatchWarnMessage(entity, worldObj);
		}
	    }
	}
    }

    public static void dispatchWarnMessage(EntityPlayerMP player, World worldObj) {

	if (!warnList.contains(player)) {
	    warnList.add(player);
	    worldObj.playSoundEffect(player.posX, player.posY, player.posZ,
		    "modularturrets:warning", 1.0F, 1.0F);
	    player.addChatMessage("\u00A74You are entering a turret-protected area. Continue at your own risk.");
	}
    }

    public static Entity getTarget(TurretBase base, World worldObj,
	    int downLowAmount, int xCoord, int yCoord, int zCoord,
	    int turretRange, EntityLivingBase turret) {

	warnPlayers(base, worldObj, downLowAmount, xCoord, yCoord, zCoord,
		turretRange);

	Entity target = null;
	if (!worldObj.isRemote && base != null && base.getOwner() != null) {
	    AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(xCoord
		    - turretRange, yCoord - downLowAmount,
		    zCoord - turretRange, xCoord + turretRange, yCoord
			    + turretRange, zCoord + turretRange);
	    List<Entity> targets = worldObj.getEntitiesWithinAABB(Entity.class,
		    axis);

	    for (int a = 0; a < targets.size(); a++) {

		if (base.isAttacksNeutrals()) {
		    if (targets.get(a) instanceof EntityAnimal
			    && !targets.get(a).isDead) {
			target = targets.get(a);
		    }
		}

		if (base.isAttacksNeutrals()) {
		    if (targets.get(a) instanceof EntityAmbientCreature
			    && !targets.get(a).isDead) {
			target = targets.get(a);
		    }
		}

		if (base.isAttacksMobs()) {
		    if (targets.get(a) instanceof EntityMob
			    && !targets.get(a).isDead) {
			target = targets.get(a);
		    }
		}

		if (base.isAttacksMobs()) {
		    if (targets.get(a) instanceof EntityFlying
			    && !targets.get(a).isDead) {
			target = targets.get(a);
		    }
		}

		if (base.isAttacksMobs()) {
		    if (targets.get(a) instanceof EntitySlime
			    && !targets.get(a).isDead) {
			target = targets.get(a);
		    }
		}

		if (base.isAttacksPlayers()) {
		    if (targets.get(a) instanceof EntityPlayerMP
			    && !targets.get(a).isDead) {
			EntityPlayerMP entity = (EntityPlayerMP) targets.get(a);
			if (!entity.username.equals(base.getOwner())
				&& !isTrustedPlayer(entity.username, base)) {
			    target = targets.get(a);
			}

		    }
		}

		if (target != null && turret != null) {
		    EntityLivingBase targetELB = (EntityLivingBase) target;
		    if (canTurretSeeTarget(turret, targetELB)) {
			return target;
		    }
		}
	    }
	}
	return null;
    }

    public static boolean isTrustedPlayer(String name, TurretBase base) {
	for (int i = 0; i < base.trustedPlayers.size(); i++) {
	    if (base.trustedPlayers.get(i).equals(name)) {
		return true;
	    }
	}
	return false;
    }

    public static TurretBase getTurretBase(World world, int x, int y, int z) {
	if (world != null) {
	    if (world.getBlockTileEntity(x + 1, y, z) instanceof TurretBase) {
		return (TurretBase) world.getBlockTileEntity(x + 1, y, z);
	    }

	    if (world.getBlockTileEntity(x - 1, y, z) instanceof TurretBase) {
		return (TurretBase) world.getBlockTileEntity(x - 1, y, z);
	    }

	    if (world.getBlockTileEntity(x, y, z + 1) instanceof TurretBase) {
		return (TurretBase) world.getBlockTileEntity(x, y, z + 1);
	    }

	    if (world.getBlockTileEntity(x, y, z - 1) instanceof TurretBase) {
		return (TurretBase) world.getBlockTileEntity(x, y, z - 1);
	    }

	    if (world.getBlockTileEntity(x, y + 1, z) instanceof TurretBase) {
		return (TurretBase) world.getBlockTileEntity(x, y + 1, z);
	    }

	    if (world.getBlockTileEntity(x, y - 1, z) instanceof TurretBase) {
		return (TurretBase) world.getBlockTileEntity(x, y - 1, z);
	    }
	}

	return null;
    }

    public static float getAimYaw(Entity target, int xCoord, int yCoord,
	    int zCoord) {
	double dX = (target.posX) - (xCoord);
	double dZ = (target.posZ) - (zCoord);
	float yaw = (float) Math.atan2(dZ, dX);
	yaw = yaw - 1.570796F + 3.2F;
	return yaw;
    }

    public static float getAimPitch(Entity target, int xCoord, int yCoord,
	    int zCoord) {
	double dX = (target.posX - 0.2F) - (xCoord + 0.6F);
	double dY = (target.posY + 0.6F) - (yCoord - 0.6F);
	double dZ = (target.posZ - 0.2F) - (zCoord + 0.6F);
	float pitch = (float) (Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI);
	pitch = pitch + 1.65F;
	return pitch;
    }

    public static ItemStack useAnyItemStackFromBase(TurretBase base) {
	for (int i = 0; i <= 8; i++) {
	    ItemStack ammoCheck = base.inv[i];
	    if (ammoCheck != null && ammoCheck.stackSize > 0
		    && ammoCheck.getItem() != null
		    && ammoCheck.getItem() != null) {
		base.decrStackSize(i, 1);
		return new ItemStack(ammoCheck.getItem());
	    }
	}
	return null;
    }

    public static ItemStack useSpecificItemStackItemFromBase(TurretBase base,
	    Item item) {
	for (int i = 0; i <= 8; i++) {
	    ItemStack ammoCheck = base.inv[i];
	    if (ammoCheck != null && ammoCheck.stackSize > 0
		    && ammoCheck.getItem() != null
		    && ammoCheck.itemID == item.itemID) {
		base.decrStackSize(i, 1);
		return new ItemStack(ammoCheck.getItem());

	    }
	}
	return null;
    }

    public static int getRangeUpgrades(TurretBase base) {

	int value = 0;
	int tier = base.baseTier;
	if (tier == 1) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof RangeUpgradeItem) {
		    value = value
			    + (Constants.rangeUpgradeBoost * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}
	if (tier == 2) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof RangeUpgradeItem) {
		    value = value
			    + (Constants.rangeUpgradeBoost * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}

	if (tier == 3) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof RangeUpgradeItem) {
		    value = value
			    + (Constants.rangeUpgradeBoost * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}

	if (tier == 4) {
	    for (int i = 11; i <= 12; i++) {
		if (base.inv != null && base.getStackInSlot(i) != null) {
		    if (base.getStackInSlot(i).getItem() instanceof RangeUpgradeItem) {
			value = value
				+ (Constants.rangeUpgradeBoost * base
					.getStackInSlot(i).stackSize);
		    }
		}
	    }
	}

	return value;
    }

    public static float getAccuraccyUpgrades(TurretBase base) {

	float value = 0.0F;
	int tier = base.baseTier;

	if (tier == 1) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof AccuraccyUpgradeItem) {
		    value = value
			    + (Constants.accuraccyUpgradeBoost * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}
	if (tier == 2) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof AccuraccyUpgradeItem) {
		    value = value
			    + (Constants.accuraccyUpgradeBoost * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}

	if (tier == 3) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof AccuraccyUpgradeItem) {
		    value = value
			    + (Constants.accuraccyUpgradeBoost * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}

	if (tier == 4) {
	    for (int i = 11; i <= 12; i++) {
		if (base.inv != null && base.getStackInSlot(i) != null) {
		    if (base.getStackInSlot(i).getItem() instanceof AccuraccyUpgradeItem) {
			value = value
				+ (Constants.accuraccyUpgradeBoost * base
					.getStackInSlot(i).stackSize);
		    }
		}
	    }
	}

	return value;
    }

    public static float getEfficiencyUpgrades(TurretBase base) {

	float value = 0.0F;
	int tier = base.baseTier;

	if (tier == 1) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof EfficiencyUpgradeItem) {
		    value = value
			    + (Constants.efficiencyUpgradeBoostPercentage * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}
	if (tier == 2) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof EfficiencyUpgradeItem) {
		    value = value
			    + (Constants.efficiencyUpgradeBoostPercentage * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}

	if (tier == 3) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof EfficiencyUpgradeItem) {
		    value = value
			    + (Constants.efficiencyUpgradeBoostPercentage * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}

	if (tier == 4) {
	    for (int i = 11; i <= 12; i++) {
		if (base.inv != null && base.getStackInSlot(i) != null) {
		    if (base.getStackInSlot(i).getItem() instanceof EfficiencyUpgradeItem) {
			value = value
				+ (Constants.efficiencyUpgradeBoostPercentage * base
					.getStackInSlot(i).stackSize);
		    }
		}
	    }
	}

	return value;
    }

    public static float getFireRateUpgrades(TurretBase base) {

	float value = 0.0F;
	int tier = base.baseTier;
	if (tier == 1) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof FireRateUpgradeItem) {
		    value = value
			    + (Constants.fireRateUpgradeBoostPercentage * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}
	if (tier == 2) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof FireRateUpgradeItem) {
		    value = value
			    + (Constants.fireRateUpgradeBoostPercentage * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}

	if (tier == 3) {
	    if (base.inv != null && base.getStackInSlot(11) != null) {
		if (base.getStackInSlot(11).getItem() instanceof FireRateUpgradeItem) {
		    value = value
			    + (Constants.fireRateUpgradeBoostPercentage * base
				    .getStackInSlot(11).stackSize);
		}
	    }
	}

	if (tier == 4) {
	    for (int i = 11; i <= 12; i++) {
		if (base.inv != null && base.getStackInSlot(i) != null) {
		    if (base.getStackInSlot(i).getItem() instanceof FireRateUpgradeItem) {
			value = value
				+ (Constants.fireRateUpgradeBoostPercentage * base
					.getStackInSlot(i).stackSize);
		    }
		}
	    }
	}

	return value;
    }

    public static boolean hasRedstoneReactor(TurretBase base) {

	boolean value = false;
	if (!(base instanceof TurretWoodBase)) {
	    for (int i = 9; i <= 10; i++) {
		if (base.inv != null && base.getStackInSlot(i) != null) {
		    if (base.getStackInSlot(i).getItem() instanceof RedstoneReactorAddonItem) {
			value = true;
		    }
		}
	    }
	    return value;
	}
	return false;
    }

    public static boolean hasDamageAmpAddon(TurretBase base) {

	boolean value = false;
	if (!(base instanceof TurretWoodBase)) {
	    for (int i = 9; i <= 10; i++) {
		if (base.inv != null && base.getStackInSlot(i) != null) {
		    if (base.getStackInSlot(i).getItem() instanceof DamageAmpAddonItem) {
			value = true;
		    }
		}
	    }
	    return value;
	}
	return false;
    }

    public static boolean hasSolarPanelAddon(TurretBase base) {

	boolean value = false;
	if (!(base instanceof TurretWoodBase)) {
	    for (int i = 9; i <= 10; i++) {
		if (base.inv != null && base.getStackInSlot(i) != null) {
		    if (base.getStackInSlot(i).getItem() instanceof SolarPanelAddonItem) {
			value = true;
		    }
		}
	    }
	    return value;
	}
	return false;
    }

    public static void updateSolarPanelAddon(TurretBase base) {
	if (hasSolarPanelAddon(base)) {
	    if (base.worldObj.isDaytime()
		    && !base.worldObj.isRaining()
		    && base.worldObj.canBlockSeeTheSky(base.xCoord,
			    base.yCoord + 2, base.zCoord)) {
		base.receiveEnergy(ForgeDirection.UNKNOWN, 1, false);
	    }
	}
    }

    public static void updateRedstoneReactor(TurretBase base) {
	if (hasRedstoneReactor(base)
		&& Constants.redstoneReactorAddonGen < (base.storage
			.getMaxEnergyStored() - base.storage.getEnergyStored())) {
	    ItemStack redstone = useSpecificItemStackItemFromBase(base,
		    Item.redstone);
	    if (redstone != null) {
		base.storage.setEnergyStored(base.storage.getEnergyStored()
			+ Constants.redstoneReactorAddonGen);
	    }
	}
    }

    public static boolean canTurretSeeTarget(EntityLivingBase turret,
	    EntityLivingBase target) {
	if (turret.canEntityBeSeen(target)) {
	    return true;
	} else {
	    return false;
	}

    }
}
