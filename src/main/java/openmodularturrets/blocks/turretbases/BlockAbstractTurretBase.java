package openmodularturrets.blocks.turretbases;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import openmodularturrets.ModularTurrets;
import openmodularturrets.network.SetTurretOwnerMessage;
import openmodularturrets.tileentity.turretBase.TurretBase;

import java.util.Random;

public abstract class BlockAbstractTurretBase extends BlockContainer {
    public BlockAbstractTurretBase() {
        super(Material.rock);

        this.setCreativeTab(ModularTurrets.modularTurretsTab);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
        this.setStepSound(Block.soundTypeStone);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float what, float these, float are) {
        if (!world.isRemote) {
            TurretBase base = (TurretBase) world.getTileEntity(x, y, z);

            if (player.getDisplayName().equals(base.getOwner())) {
                player.openGui(ModularTurrets.instance, base.getBaseTier(), world, x, y, z);
            } else {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("status.ownership")));
            }
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        if (par1World.isRemote) {
            SetTurretOwnerMessage message = new SetTurretOwnerMessage(par2, par3, par4, Minecraft.getMinecraft().getSession()
                    .getUsername());

            ModularTurrets.networking.sendToServer(message);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        if (!world.isRemote) {
            dropItems(world, x, y, z);
            super.breakBlock(world, x, y, z, par5, par6);
        }
    }

    private void dropItems(World world, int x, int y, int z) {
        if (world.getTileEntity(x, y, z) instanceof TurretBase) {
            TurretBase base = (TurretBase) world.getTileEntity(x, y, z);
            Random rand = new Random();
            for (int i = 0; i < base.getSizeInventory(); i++) {
                ItemStack item = base.getStackInSlot(i);

                if (item != null && item.stackSize > 0) {
                    float rx = rand.nextFloat() * 0.8F + 0.1F;
                    float ry = rand.nextFloat() * 0.8F + 0.1F;
                    float rz = rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityItem = new EntityItem(world, x + rx, y
                            + ry, z + rz, new ItemStack(item.getItem(),
                            item.stackSize, item.getItemDamage()));

                    if (item.hasTagCompound()) {
                        entityItem.getEntityItem().setTagCompound(
                                (NBTTagCompound) item.getTagCompound().copy());
                    }

                    float factor = 0.05F;
                    entityItem.motionX = rand.nextGaussian() * factor;
                    entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                    entityItem.motionZ = rand.nextGaussian() * factor;
                    world.spawnEntityInWorld(entityItem);
                    item.stackSize = 0;
                }
            }
        }
    }
}
