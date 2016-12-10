package omtteam.openmodularturrets.blocks.turretheads;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import omtteam.openmodularturrets.items.blocks.ItemBlockRocketTurret;
import omtteam.openmodularturrets.reference.OMTNames;
import omtteam.openmodularturrets.reference.Reference;
import omtteam.openmodularturrets.tileentity.turrets.RocketTurretTileEntity;

public class BlockRocketTurret extends BlockAbstractTurretHead {
    public BlockRocketTurret() {
        super();

        this.setUnlocalizedName(OMTNames.Blocks.rocketTurret);
        this.setRegistryName(Reference.MOD_ID, OMTNames.Blocks.rocketTurret);
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockRocketTurret(block);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new RocketTurretTileEntity();
    }
}
