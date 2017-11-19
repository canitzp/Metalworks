package de.canitzp.simplesteel.machine.blastfurnace;

import de.canitzp.simplesteel.SimpleSteel;
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
        if(tile.fuelMax > 0){
            int i = (int) ((tile.fuelLeft / (tile.fuelMax * 1.0F)) * 43);
            this.drawTexturedModalRect(this.guiLeft + 17, this.guiTop + 30 + 43 - i, 176, 43 - i, 4, i);
        }
        if(tile.maxBurn > 0){
            int i = (int) ((tile.burnLeft / (tile.maxBurn  * 1.0F)) * 26);
            this.drawTexturedModalRect(this.guiLeft + 68, this.guiTop + 29, 180, 0, 43, 26 - i);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
        if(mouseX >= this.guiLeft + 17 && mouseX <= this.guiLeft + 17 + 4 && mouseY >= this.guiTop + 30 && mouseY <= this.guiTop + 30 + 43){
            this.drawHoveringText(String.valueOf(tile.fuelLeft), mouseX, mouseY);
        }
    }
}
