package openmodularturrets.tileentity.turretbase;


import net.minecraftforge.fml.common.Optional;
import openmodularturrets.handler.ConfigHandler;

public class TurretBaseTierOneTileEntity extends TurretBase {
    public TurretBaseTierOneTileEntity() {
        this(ConfigHandler.getBaseTierOneMaxCharge(), ConfigHandler.getBaseTierOneMaxIo());
    }

    public TurretBaseTierOneTileEntity(int MaxEnergyStorage, int MaxIO) {
        super(MaxEnergyStorage, MaxIO);
    }

    @Override
    public int getBaseTier() {
        return 1;
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Optional.Method(modid = "OpenComputers")
    @Override
    public String getComponentName() {
        return "tierOneTurretBase";
    }
}
