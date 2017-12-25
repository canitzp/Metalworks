package de.canitzp.metalworks.machine.duster;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.client.gui.GuiEnergyBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class GuiDuster extends GuiContainer {

    public static final ResourceLocation LOC = new ResourceLocation(Metalworks.MODID, "textures/gui/duster.png");

    private TileDuster tile;
    private GuiEnergyBar energyBar = new GuiEnergyBar();

    public GuiDuster(EntityPlayer player, TileDuster tile) {
        super(new ContainerDuster(player, tile));
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
            int i = (int) ((tile.burn / (tile.maxBurn  * 1.0F)) * 25.0F);
            this.drawTexturedModalRect(this.guiLeft + 73, this.guiTop + 29, 176, 0, 30, 25 - i);
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
        if(mouseX >= this.guiLeft + 73 && mouseX <= this.guiLeft + 73 + 30 && mouseY >= this.guiTop + 29 && mouseY <= this.guiTop + 29 + 25){
            Util.drawProgressText(this, mouseX, mouseY, this.tile.burn, this.tile.maxBurn, true);
        }
    }
}
