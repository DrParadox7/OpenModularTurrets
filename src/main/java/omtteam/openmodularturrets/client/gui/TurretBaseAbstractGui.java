package omtteam.openmodularturrets.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import omtteam.omlib.client.gui.BlockingAbstractGuiContainer;
import omtteam.omlib.client.gui.IHasTooltips;
import omtteam.omlib.reference.OMLibNames;
import omtteam.omlib.util.DebugHandler;
import omtteam.omlib.util.PlayerUtil;
import omtteam.omlib.util.TrustedPlayer;
import omtteam.omlib.util.WorldUtil;
import omtteam.openmodularturrets.OpenModularTurrets;
import omtteam.openmodularturrets.handler.OMTNetworkingHandler;
import omtteam.openmodularturrets.network.messages.*;
import omtteam.openmodularturrets.reference.OMTNames;
import omtteam.openmodularturrets.tileentity.TurretBase;
import omtteam.openmodularturrets.tileentity.turrets.TurretHead;
import omtteam.openmodularturrets.util.TurretHeadUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static omtteam.omlib.util.GeneralUtil.*;

/**
 * Created by nico on 6/4/15.
 * Abstract class for all turret base GUIs.
 */

class TurretBaseAbstractGui extends BlockingAbstractGuiContainer implements IHasTooltips {
    private int mouseX;
    private int mouseY;
    private final EntityPlayer player;
    final TurretBase base;

    TurretBaseAbstractGui(InventoryPlayer inventoryPlayer, TurretBase tileEntity, Container container) {
        super(container);
        this.base = tileEntity;
        player = inventoryPlayer.player;
        DebugHandler.getInstance().setPlayer(player);
    }

    protected void drawEnergyBar() {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        int expression = (base.getEnergyLevel(EnumFacing.DOWN) * 51) / (base.getMaxEnergyLevel(
                EnumFacing.DOWN) == 0 ? 1 : base.getMaxEnergyLevel(EnumFacing.DOWN));

        drawTexturedModalRect(x + 153, y + 17, 178, 17, 14, 51);

        int next = new Random().nextInt(3);

        if (next == 0) {
            drawTexturedModalRect(x + 153, y + 17 + 51 - expression, 196, 68 - expression, 14, expression);
        }

        if (next == 1) {
            drawTexturedModalRect(x + 153, y + 17 + 51 - expression, 215, 68 - expression, 14, expression);
        }

        if (next == 2) {
            drawTexturedModalRect(x + 153, y + 17 + 51 - expression, 234, 68 - expression, 14, expression);
        }
    }

    protected void buttonInit() {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        if (PlayerUtil.isPlayerAdmin(player, base)) {
            this.buttonList.add(new GuiButton(3, x + 180, y + 100, 80, 20, safeLocalize(OMTNames.Localizations.GUI.DROP_TURRETS)));
            this.buttonList.add(new GuiButton(4, x + 180, y + 75, 80, 20, safeLocalize(OMTNames.Localizations.GUI.DROP_BASE)));
            this.buttonList.add(new GuiButton(5, x + 180, y + 25, 80, 20, safeLocalize(OMTNames.Localizations.GUI.CONFIGURE)));
            this.buttonList.add(new GuiButton(6, x + 180, y + 50, 80, 20,
                    base.isMultiTargeting() ? safeLocalize(OMTNames.Localizations.GUI.TARGET) + ": "
                            + safeLocalize(OMTNames.Localizations.GUI.MULTI) : safeLocalize(OMTNames.Localizations.GUI.TARGET)
                            + ": " + safeLocalize(OMTNames.Localizations.GUI.SINGLE)));
            this.buttonList.add(new GuiButton(7, x + 180, y, 80, 20, safeLocalize(OMLibNames.Localizations.GUI.MODE)));
            this.buttonList.add(new GuiButton(1, x + 120, y + 15, 20, 20, "+"));
            this.buttonList.add(new GuiButton(2, x + 120, y + 50, 20, 20, "-"));
        } else if (PlayerUtil.canPlayerChangeSetting(player, base)) {
            this.buttonList.add(new GuiButton(5, x + 180, y + 50, 80, 20, safeLocalize(OMTNames.Localizations.GUI.CONFIGURE)));
            this.buttonList.add(new GuiButton(6, x + 180, y + 75, 80, 20,
                    base.isMultiTargeting() ? safeLocalize(OMTNames.Localizations.GUI.TARGET) + ": "
                            + safeLocalize(OMTNames.Localizations.GUI.MULTI) : safeLocalize(OMTNames.Localizations.GUI.TARGET)
                            + ": " + safeLocalize(OMTNames.Localizations.GUI.SINGLE)));
            this.buttonList.add(new GuiButton(1, x + 120, y + 15, 20, 20, "+"));
            this.buttonList.add(new GuiButton(2, x + 120, y + 50, 20, 20, "-"));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        super.initGui();
        buttonInit();
    }


    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.mouseX = par1;
        this.mouseY = par2;
        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == 1) {
            this.base.setCurrentMaxRange((this.base.getCurrentMaxRange() + 1));
            sendChangeToServer();
        }

        if (guibutton.id == 2) {
            this.base.setCurrentMaxRange((this.base.getCurrentMaxRange() - 1));
            sendChangeToServer();
        }

        if (guibutton.id == 3) {
            sendDropTurretsToServer();
        }

        if (guibutton.id == 4) {
            sendDropBaseToServer();
        }

        if (guibutton.id == 5) {
            player.openGui(OpenModularTurrets.instance, 6, player.getEntityWorld(), base.getPos().getX(), base.getPos().getY(), base.getPos().getZ());
        }

        if (guibutton.id == 6) {
            sendSetBaseTargetingToServer();
            for (Object button : buttonList) {
                if (((GuiButton) button).id == 6) {
                    this.base.setMultiTargeting(!this.base.isMultiTargeting());
                    ((GuiButton) button).displayString = base.isMultiTargeting() ? safeLocalize(OMTNames.Localizations.GUI.TARGET) + ": " + safeLocalize(OMTNames.Localizations.GUI.MULTI) : safeLocalize(OMTNames.Localizations.GUI.TARGET) + ": " + safeLocalize(OMTNames.Localizations.GUI.SINGLE);
                }
            }
        }
        if (guibutton.id == 7) {
            sendToggleModeToServer();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        fontRenderer.drawString(base.getTier() > 1 ? safeLocalize(OMTNames.Localizations.GUI.ADDONS) : "", 71, 6, 0);
        fontRenderer.drawString(base.getTier() > 1 ? safeLocalize(OMTNames.Localizations.GUI.UPGRADES) : "", 71, 39, 0);
        fontRenderer.drawString(safeLocalize(OMTNames.Localizations.GUI.AMMO), 8, 6, 0);
        fontRenderer.drawString(safeLocalize(OMTNames.Localizations.GUI.INVENTORY), 8, ySize - 97 + 4, 0);

        fontRenderer.drawStringWithShadow("" + base.getCurrentMaxRange(), String.valueOf(base.getCurrentMaxRange()).length() == 1 ?
                127 : 124, 39, base.getCurrentMaxRange() == getBaseUpperBoundRange() ? 16724530 : 40000);
        fontRenderer.drawString(safeLocalize(OMTNames.Localizations.GUI.RANGE), 116, 6, 0);

        ArrayList targetInfo = new ArrayList();

        targetInfo.add("\u00A76" + safeLocalize(OMLibNames.Localizations.GUI.OWNER) + ": \u00A7f" + base.getOwnerName());
        targetInfo.add("\u00A76" + safeLocalize(OMLibNames.Localizations.GUI.MODE) + ": \u00A7f" + getMachineModeLocalization(base.getMode()));
        boolean isCurrentlyOn = base.isActive();
        targetInfo.add("\u00A76" + safeLocalize(OMLibNames.Localizations.GUI.ACTIVE) + ": " + getColoredBooleanLocalizationYesNo(isCurrentlyOn));
        targetInfo.add("");
        targetInfo.add("\u00A76" + safeLocalize(OMTNames.Localizations.GUI.KILLS) + ": " + base.getKills());
        targetInfo.add("\u00A76" + safeLocalize(OMTNames.Localizations.GUI.PLAYER_KILLS) + ": " + base.getPlayerKills());
        targetInfo.add("");
        if (base.getTrustedPlayers().size() != 0) {
            targetInfo.add("\u00A75" + safeLocalize(OMTNames.Localizations.GUI.TRUSTED_PLAYERS) + ":");
            for (TrustedPlayer trusted_player : base.getTrustedPlayers()) {
                targetInfo.add("\u00A7b" + trusted_player.getName());
            }
        } else {
            targetInfo.add("\u00A75" + safeLocalize(OMTNames.Localizations.GUI.TRUSTED_PLAYERS) + ": " + getColoredBooleanLocalizationYesNo(false));
        }

        targetInfo.add("");
        targetInfo.add("\u00A77" + safeLocalize(OMTNames.Localizations.GUI.ATTACK_MOBS) + ": " + getColoredBooleanLocalizationYesNo(base.isAttacksMobs()));
        targetInfo.add("\u00A77" + safeLocalize(OMTNames.Localizations.GUI.ATTACK_NEUTRALS) + ": " + getColoredBooleanLocalizationYesNo(base.isAttacksNeutrals()));
        targetInfo.add("\u00A77" + safeLocalize(OMTNames.Localizations.GUI.ATTACK_PLAYERS) + ": " + getColoredBooleanLocalizationYesNo(base.isAttacksPlayers()));

        this.drawHoveringText(targetInfo, -128, 17, fontRenderer);

        drawTooltips();
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    public void drawTooltips() {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        int tooltipToDraw = buttonList.stream().filter(GuiButton::isMouseOver).mapToInt(s -> s.id).sum();
        ArrayList<String> tooltip = new ArrayList<>();
        switch (tooltipToDraw) {
            case 1:
                tooltip.add(safeLocalize(OMTNames.Localizations.Tooltip.PLUS_RANGE));
                break;
            case 2:
                tooltip.add(safeLocalize(OMTNames.Localizations.Tooltip.MINUS_RANGE));
                break;
            case 5:
                tooltip.add(safeLocalize(OMTNames.Localizations.Tooltip.CONFIGURE_BASE));
                break;
            case 6:
                tooltip.add(safeLocalize(OMTNames.Localizations.Tooltip.MULTI_TARGETING));
                break;
            case 7:
                tooltip.add(safeLocalize(OMTNames.Localizations.Tooltip.TOGGLE_MODE));
                break;
        }

        if (mouseX > k + 153 && mouseX < k + 153 + 14 && mouseY > l + 17 && mouseY < l + 17 + 51) {
            tooltip.add(base.getEnergyLevel(EnumFacing.DOWN) + "/" + base.getMaxEnergyLevel(EnumFacing.DOWN) + " RF");
            tooltip.add("EU Buffer: " + Math.round(base.getStorageEU()) + "/" + Math.round(base.getMaxStorageEU()));
        }
        if (base.getTier() > 1 && mouseX > k + 71 && mouseX < k + 71 + 40 && mouseY > l + 6 && mouseY < l + 6 + 14) {
            tooltip.add(safeLocalize(OMTNames.Localizations.Tooltip.ADDON_SLOT));
        }
        if (base.getTier() > 1 && mouseX > k + 71 && mouseX < k + 71 + 40 && mouseY > l + 39 && mouseY < l + 39 + 14) {
            tooltip.add(safeLocalize(OMTNames.Localizations.Tooltip.UPGRADE_SLOT));
        }
        if (mouseX > k + 10 && mouseX < k + 8 + 40 && mouseY > l + 6 && mouseY < l + 6 + 14) {
            tooltip.add(safeLocalize(OMTNames.Localizations.Tooltip.AMMO_SLOT));
        }
        if (mouseX > k + 123 && mouseX < k + 134 && mouseY > l + 35 && mouseY < l + 48) {
            tooltip.add(safeLocalize(OMTNames.Localizations.Tooltip.BASE_MAX_RANGE));
        }
        Slot slot = getSlotUnderMouse();
        if (slot != null && !slot.getStack().isEmpty()) {
            ItemStack stack = slot.getStack();
            this.renderToolTip(stack, mouseX - guiLeft, mouseY - guiTop);
        }
        if (!tooltip.isEmpty())
            this.drawHoveringText(tooltip, mouseX - k, mouseY - l, Minecraft.getMinecraft().fontRenderer);
    }


    private void sendChangeToServer() {
        MessageAdjustMaxRange message = new MessageAdjustMaxRange(base.getPos().getX(), base.getPos().getY(), base.getPos().getZ(),
                base.getCurrentMaxRange());

        OMTNetworkingHandler.INSTANCE.sendToServer(message);
    }

    private void sendDropTurretsToServer() {
        MessageDropTurrets message = new MessageDropTurrets(base.getPos().getX(), base.getPos().getY(), base.getPos().getZ());
        OMTNetworkingHandler.INSTANCE.sendToServer(message);
    }

    private void sendDropBaseToServer() {
        MessageDropBase message = new MessageDropBase(base.getPos().getX(), base.getPos().getY(), base.getPos().getZ());
        OMTNetworkingHandler.INSTANCE.sendToServer(message);
    }

    private void sendSetBaseTargetingToServer() {
        MessageSetBaseTargetingType message = new MessageSetBaseTargetingType(base.getPos().getX(), base.getPos().getY(), base.getPos().getZ());
        OMTNetworkingHandler.INSTANCE.sendToServer(message);
    }

    private void sendToggleModeToServer() {
        MessageToggleMode message = new MessageToggleMode(base.getPos().getX(), base.getPos().getY(), base.getPos().getZ());
        OMTNetworkingHandler.INSTANCE.sendToServer(message);
    }

    @Override
    public ArrayList<Rectangle> getBlockingAreas() {
        ArrayList<Rectangle> list = new ArrayList<>();
        Rectangle rectangleGUI = new Rectangle(0, 0, 0, 0);
        if (PlayerUtil.isPlayerAdmin(player, base)) {
            rectangleGUI = new Rectangle((width - xSize) / 2 + 180, (height - ySize) / 2, 80, 120);
        } else if (PlayerUtil.canPlayerChangeSetting(player, base)) {
            rectangleGUI = new Rectangle((width - xSize) / 2 + 180, (height - ySize) / 2 + 50, 80, 20);
        }
        list.add(rectangleGUI);
        return list;
    }

    private int getBaseUpperBoundRange() {
        int maxRange = 0;
        List<TileEntity> tileEntities = WorldUtil.getTouchingTileEntities(base.getWorld(), base.getPos());
        for (TileEntity te : tileEntities) {
            if (te instanceof TurretHead) {
                maxRange = Math.max(((TurretHead) te).getTurretRange() + TurretHeadUtil.getRangeUpgrades(base), maxRange);
            }
        }
        return maxRange;
    }
}
