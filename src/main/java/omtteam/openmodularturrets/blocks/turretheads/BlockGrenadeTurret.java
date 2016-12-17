package omtteam.openmodularturrets.blocks.turretheads;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import omtteam.openmodularturrets.items.blocks.ItemBlockGrenadeLauncherTurret;
import omtteam.openmodularturrets.reference.OMTNames;
import omtteam.openmodularturrets.reference.Reference;
import omtteam.openmodularturrets.tileentity.turrets.GrenadeLauncherTurretTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BlockGrenadeTurret extends BlockAbstractTurretHead {
    public BlockGrenadeTurret() {
        super();

        this.setUnlocalizedName(OMTNames.Blocks.grenadeTurret);
        this.setRegistryName(Reference.MOD_ID, OMTNames.Blocks.grenadeTurret);
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockGrenadeLauncherTurret(block);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new GrenadeLauncherTurretTileEntity();
    }
}
