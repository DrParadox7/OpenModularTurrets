package omtteam.openmodularturrets.api;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import omtteam.omlib.api.network.IController;
import omtteam.omlib.tileentity.EnumMachineMode;
import omtteam.omlib.util.TrustedPlayer;
import omtteam.openmodularturrets.util.TargetingSettings;

import java.util.List;

/**
 * Created by Keridos on 08/08/17.
 * This interface should be implemented by tile entities that want to control turret bases.
 * Register them with base.registerController(IBaseController instance) for them to be used.
 * Only one instance of this interface can be registered per base.
 * Turret Bases get additional API functions like getAllTargetsInRange.
 */
@SuppressWarnings("ALL")
public interface IBaseController extends IController {
    /**
     * Return true if the Entity given per parameter is a valid target.
     *
     * @param target the target
     * @param yaw    the yaw of the vector between turret and target
     * @param pitch  the pitch of the vector between turret and target
     * @return boolean
     */
    boolean isEntityValidTarget(Entity target, float yaw, float pitch);

    /**
     * Return true if the controller overrides base modes.
     *
     * @return boolean
     */
    boolean overridesMode();

    /**
     * Return the overriden mode if the controller overrides base modes.
     *
     * @return overriding mode
     */
    EnumMachineMode getOverriddenMode();

    /**
     * Return the BlockPos of the tile entity implementing this.
     *
     * @return BlockPos
     */
    BlockPos getPositionOfBlock();

    /**
     * Return the overridden targeting settings for the base.
     *
     * @return TargetingSettings
     */
    TargetingSettings getTargetingSettings();


    /**
     * Return the overridden trusted player list for the base.
     *
     * @return List of TrustedPlayer
     */
    List<TrustedPlayer> getTrustedPlayerList();
}
