package openmodularturrets.items.upgrades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.reference.Names;

import java.util.List;

public class EfficiencyUpgradeItem extends UpgradeItem {
    public EfficiencyUpgradeItem() {
        super();

        this.setUnlocalizedName(Names.Items.efficiencyUpgrade);
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        p_77624_3_.add("");
        p_77624_3_.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("turret.upgrade.label"));
        p_77624_3_.add("");
        p_77624_3_.add(
                "- " + ConfigHandler.getEfficiencyUpgradeBoostPercentage() * 100 + "% " + StatCollector.translateToLocal(
                        "turret.upgrade.eff"));
        p_77624_3_.add(StatCollector.translateToLocal("turret.upgrade.stacks"));
        p_77624_3_.add("");
        p_77624_3_.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("turret.upgrade.eff.flavour"));
    }
}