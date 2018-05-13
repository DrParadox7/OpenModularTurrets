package omtteam.openmodularturrets.tileentity;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import omtteam.omlib.tileentity.TileEntityContainer;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.omlib.util.ItemStackList;
import omtteam.openmodularturrets.api.ITurretBaseAddonTileEntity;
import omtteam.openmodularturrets.util.OMTUtil;
import omtteam.openmodularturrets.util.TurretHeadUtil;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static omtteam.omlib.util.MathUtil.truncateDoubleToInt;
import static omtteam.openmodularturrets.util.TurretHeadUtil.getTurretBaseFacing;

public class Expander extends TileEntityContainer implements ITickable, ITurretBaseAddonTileEntity {
    private boolean powerExpander;
    private EnumFacing orientation;
    private int tier;

    protected IItemHandlerModifiable inventory;

    protected void setupInventory() {
        inventory = new ItemStackHandler(9) {

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            public boolean isItemValidForSlot(int index, ItemStack stack) {
                return !isPowerExpander() && OMTUtil.isItemStackValidAmmo(stack);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!isItemValidForSlot(slot, stack))
                    return stack;
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public int getSlotLimit(int slot) {
                return truncateDoubleToInt(Math.pow(2, tier + 1));
            }
        };
    }

    public Expander() {
        super();
        this.orientation = EnumFacing.NORTH;
        setupInventory();
    }

    public Expander(int tier, boolean powerExpander) {
        super();
        setupInventory();
        this.tier = tier;
        this.powerExpander = powerExpander;
        this.orientation = EnumFacing.NORTH;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean("powerExpander", powerExpander);
        nbtTagCompound.setByte("direction", (byte) orientation.ordinal());
        nbtTagCompound.setInteger("tier", tier);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.powerExpander = nbtTagCompound.getBoolean("powerExpander");
        this.tier = nbtTagCompound.getInteger("tier");
        if (nbtTagCompound.hasKey("direction")) {
            this.setOrientation(EnumFacing.getFront(nbtTagCompound.getByte("direction")));
        }
    }

    public void setSide() {
        this.setOrientation(getTurretBaseFacing(this.getWorld(), this.pos));
    }

    @Override
    public void update() {
        if (this.getWorld().getWorldTime() % 15 == 0 && getBase() == null || dropBlock) {
            this.getWorld().destroyBlock(this.pos, true);
        }
    }

    public int getTier() {
        return tier;
    }

    public TurretBase getBase() {
        return TurretHeadUtil.getTurretBase(this.getWorld(), this.pos);
    }

    public boolean isPowerExpander() {
        return powerExpander;
    }

    public EnumFacing getOrientation() {
        return orientation;
    }

    private void setOrientation(EnumFacing orientation) {
        this.orientation = orientation;
    }

    @Nonnull
    @Override
    public TileEntityOwnedBlock getLinkedBlock() {
        return getBase();
    }

    @Override
    public IItemHandlerModifiable getInventory() {
        return inventory;
    }
}
