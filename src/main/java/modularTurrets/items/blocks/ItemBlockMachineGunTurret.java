package modularTurrets.items.blocks;

import modularTurrets.misc.Constants;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.text.DecimalFormat;
import java.util.List;

public class ItemBlockMachineGunTurret extends ItemBlock {
    public static final DecimalFormat df = new DecimalFormat("0.0");

    public ItemBlockMachineGunTurret(Block p_i45328_1_) {
        super(p_i45328_1_);
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        p_77624_3_.add("");
        p_77624_3_.add(EnumChatFormatting.GOLD + "--" + StatCollector.translateToLocal("tooltip.info") + "--");
        p_77624_3_.add(StatCollector.translateToLocal("tooltip.tier") + ": " + EnumChatFormatting.WHITE + "1");
        p_77624_3_.add(StatCollector.translateToLocal("tooltip.range") + ": " + EnumChatFormatting.WHITE + Constants.machineGunTurretRange);
        p_77624_3_.add(StatCollector.translateToLocal("tooltip.accuracy") + ": " + EnumChatFormatting.WHITE + StatCollector.translateToLocal("turret.accuracy.medium"));
        p_77624_3_.add(StatCollector.translateToLocal("tooltip.ammo") + ": " + EnumChatFormatting.WHITE + StatCollector.translateToLocal("turret.ammo.1"));
        p_77624_3_.add(StatCollector.translateToLocal("tooltip.tierRequired") + ": " + EnumChatFormatting.WHITE + StatCollector.translateToLocal("base.tier.1"));
        p_77624_3_.add("");
        p_77624_3_.add(EnumChatFormatting.DARK_PURPLE + "--" + StatCollector.translateToLocal("tooltip.damage.label") + "--");
        p_77624_3_.add(StatCollector.translateToLocal("tooltip.damage.stat") + ": " + EnumChatFormatting.WHITE + (Constants.machineGunTurretDamage / 2F) + " " + StatCollector.translateToLocal("tooltip.health"));
        p_77624_3_.add(StatCollector.translateToLocal("tooltip.aoe") + ": " + EnumChatFormatting.WHITE + "0");
        p_77624_3_.add(StatCollector.translateToLocal("tooltip.firerate") + ": " + EnumChatFormatting.WHITE + df.format(20.0F / Constants.machineGunTurretFireRate));
        p_77624_3_.add(StatCollector.translateToLocal("tooltip.energy.stat") + ": " + EnumChatFormatting.WHITE + Constants.machineGunTurretPowerUse+" RF");
        p_77624_3_.add("");
        p_77624_3_.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("flavour.turret.1"));
    }
}
