package de.canitzp.simplesteel.machine.blastfurnace;

import com.google.common.collect.Lists;
import de.canitzp.simplesteel.SimpleSteel;
import de.canitzp.simplesteel.Util;
import de.canitzp.simplesteel.client.gui.GuiEnergyBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class GuiBlastFurnace extends GuiContainer {

    public static final ResourceLocation LOC = new ResourceLocation(SimpleSteel.MODID, "textures/gui/blast_furnace.png");

    private TileBlastFurnace tile;
    private GuiEnergyBar energyBar = new GuiEnergyBar();

    public GuiBlastFurnace(EntityPlayer player, TileBlastFurnace tile) {
        super(new ContainerBlastFurnace(player, tile));
        this.tile = tile;
    }

    @Override
    public void initGui() {
        this.xSize = 176;
        this.ySize = 166;
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        if(tile.maxBurn > 0){
            int i = (int) ((tile.burnLeft / (tile.maxBurn  * 1.0F)) * 26);
            this.drawTexturedModalRect(this.guiLeft + 68, this.guiTop + 29, 180, 0, 43, 26 - i);
        }
        if(tile.energy.getMaxEnergyStored() > 0){
            this.energyBar.draw(this.guiLeft + 11, this.guiTop + 11, this.tile.energy.getEnergyStored(), this.tile.energy.getMaxEnergyStored(), -1);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
        this.energyBar.mouseDraw(this, this.guiLeft, this.guiTop, mouseX, mouseY, this.tile.energy.getEnergyStored(), this.tile.energyUsage);
    }
}
