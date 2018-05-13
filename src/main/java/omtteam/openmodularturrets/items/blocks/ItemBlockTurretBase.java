package omtteam.openmodularturrets.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import omtteam.omlib.util.GeneralUtil;
import omtteam.openmodularturrets.handler.OMTConfigHandler;
import omtteam.openmodularturrets.init.ModBlocks;
import omtteam.openmodularturrets.reference.OMTNames;
import omtteam.openmodularturrets.reference.Reference;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static omtteam.omlib.util.GeneralUtil.safeLocalize;

@SuppressWarnings("deprecation")
public class ItemBlockTurretBase extends ItemBlock {
    public ItemBlockTurretBase(Block block) {
        super(block);
        setHasSubtypes(true);
        this.setRegistryName(Reference.MOD_ID, OMTNames.Blocks.turretBase);
    }

    private final static String[] subNames = {
            "_tier_one", "_tier_two", "_tier_three", "_tier_four", "_tier_five"
    };

    @Override
    @ParametersAreNonnullByDefault
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(itemIn)) {
            for (int i = 0; i < 5; i++) {
                subItems.add(new ItemStack(ModBlocks.turretBase, 1, i));
            }
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack itemStack) {
        return "tile." + OMTNames.Blocks.turretBase + subNames[itemStack.getItemDamage()];
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    @SuppressWarnings("unchecked")
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        if (!(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))) {
            tooltip.add(GeneralUtil.shiftDetail);
        } else {
            tooltip.add(safeLocalize(OMTNames.Localizations.GUI.TURRET_BASE_DESCRIPTION));

            switch (stack.getMetadata()) {
                case 0:
                    tooltip.add("");
                    tooltip.add(TextFormatting.AQUA + "--" + safeLocalize("tooptip.energy.label") + "--");
                    tooltip.add(safeLocalize("tooltip.rf.max") + ": " + TextFormatting.WHITE +
                            OMTConfigHandler.getBaseTierOneMaxCharge());
                    tooltip.add(safeLocalize("tooltip.rf.io") + ": " + TextFormatting.WHITE +
                            OMTConfigHandler.getBaseTierOneMaxIo());
                    tooltip.add("");
                    tooltip.add(TextFormatting.GREEN + "--" + safeLocalize("tooltip.extras.label") + "--");
                    tooltip.add(safeLocalize("tooltip.extras.addons.0"));
                    tooltip.add("");
                    tooltip.add(TextFormatting.DARK_GRAY + safeLocalize("flavour.base.1"));
                    return;
                case 1:
                    tooltip.add("");
                    tooltip.add(TextFormatting.AQUA + "--" + safeLocalize("tooptip.energy.label") + "--");
                    tooltip.add(safeLocalize("tooltip.rf.max") + ": " + TextFormatting.WHITE +
                            OMTConfigHandler.getBaseTierTwoMaxCharge());
                    tooltip.add(safeLocalize("tooltip.rf.io") + ": " + TextFormatting.WHITE +
                            OMTConfigHandler.getBaseTierTwoMaxIo());
                    tooltip.add("");
                    tooltip.add(TextFormatting.GREEN + "--" + safeLocalize("tooltip.extras.label") + "--");
                    tooltip.add(safeLocalize("tooltip.extras.addons.2"));
                    tooltip.add(safeLocalize("tooltip.extras.upgrade.1"));
                    tooltip.add("");
                    tooltip.add(TextFormatting.DARK_GRAY + safeLocalize("flavour.base.2"));
                    return;
                case 2:
                    tooltip.add("");
                    tooltip.add(TextFormatting.AQUA + "--" + safeLocalize("tooptip.energy.label") + "--");
                    tooltip.add(safeLocalize("tooltip.rf.max") + ": " + TextFormatting.WHITE +
                            OMTConfigHandler.getBaseTierThreeMaxCharge());
                    tooltip.add(safeLocalize("tooltip.rf.io") + ": " + TextFormatting.WHITE +
                            OMTConfigHandler.getBaseTierThreeMaxIo());
                    tooltip.add("");
                    tooltip.add(TextFormatting.GREEN + "--" + safeLocalize("tooltip.extras.label") + "--");
                    tooltip.add(safeLocalize("tooltip.extras.addons.2"));
                    tooltip.add(safeLocalize("tooltip.extras.upgrade.1"));
                    tooltip.add("");
                    tooltip.add(TextFormatting.DARK_GRAY + safeLocalize("flavour.base.3"));
                    return;
                case 3:
                    tooltip.add("");
                    tooltip.add(TextFormatting.AQUA + "--" + safeLocalize("tooptip.energy.label") + "--");
                    tooltip.add(safeLocalize("tooltip.rf.max") + ": " + TextFormatting.WHITE +
                            OMTConfigHandler.getBaseTierFourMaxCharge());
                    tooltip.add(safeLocalize("tooltip.rf.io") + ": " + TextFormatting.WHITE +
                            OMTConfigHandler.getBaseTierFourMaxIo());
                    tooltip.add("");
                    tooltip.add(TextFormatting.GREEN + "--" + safeLocalize("tooltip.extras.label") + "--");
                    tooltip.add(safeLocalize("tooltip.extras.addons.2"));
                    tooltip.add(safeLocalize("tooltip.extras.upgrade.1"));
                    tooltip.add("");
                    tooltip.add(TextFormatting.DARK_GRAY + safeLocalize("flavour.base.4"));
                    return;
                case 4:
                    tooltip.add("");
                    tooltip.add(TextFormatting.AQUA + "--" + safeLocalize("tooptip.energy.label") + "--");
                    tooltip.add(safeLocalize("tooltip.rf.max") + ": " + TextFormatting.WHITE +
                            OMTConfigHandler.getBaseTierFiveMaxCharge());
                    tooltip.add(safeLocalize("tooltip.rf.io") + ": " + TextFormatting.WHITE +
                            OMTConfigHandler.getBaseTierFiveMaxIo());
                    tooltip.add("");
                    tooltip.add(TextFormatting.GREEN + "--" + safeLocalize("tooltip.extras.label") + "--");
                    tooltip.add(safeLocalize("tooltip.extras.addons.2"));
                    tooltip.add(safeLocalize("tooltip.extras.upgrade.2"));
                    tooltip.add("");
                    tooltip.add(TextFormatting.DARK_GRAY + safeLocalize("flavour.base.5a"));
                    tooltip.add(TextFormatting.DARK_GRAY + safeLocalize("flavour.base.5b"));
            }
        }
    }
}
