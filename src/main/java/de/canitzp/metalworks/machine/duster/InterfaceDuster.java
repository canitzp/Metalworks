package de.canitzp.metalworks.machine.duster;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.client.gui.GuiEnergyBar;
import de.canitzp.metalworks.gui.GuiMachine;
import de.canitzp.metalworks.machine.IMachineInterface;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class InterfaceDuster implements IMachineInterface<TileDuster> {

    public static final ResourceLocation LOC = new ResourceLocation(Metalworks.MODID, "textures/gui/duster.png");
    private GuiEnergyBar energyBar = new GuiEnergyBar();

    @Override
    public void initGui(TileDuster tile, GuiMachine<TileDuster> gui) {
        gui.setSize(176, 166);
    }

    @Override
    public void drawBackground(TileDuster tile, GuiMachine<TileDuster> gui, TextureManager texture, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        texture.bindTexture(LOC);
        gui.drawTexturedModalRect(guiLeft, guiTop, 0, 0, gui.getXSize(), gui.getYSize());
        if(tile.maxBurn > 0){
            int i = (int) ((tile.burn / (tile.maxBurn  * 1.0F)) * 25.0F);
            gui.drawTexturedModalRect(guiLeft + 73, guiTop + 29, 176, 0, 30, 25 - i);
        }
        if(tile.energy.getMaxEnergyStored() > 0){
            this.energyBar.draw(guiLeft + 11, guiTop + 11, tile.energy.getEnergyStored(), tile.energy.getMaxEnergyStored(), -1);
        }
    }

    @Override
    public void drawScreen(TileDuster tile, GuiMachine<TileDuster> gui, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        this.energyBar.mouseDraw(gui, guiLeft, guiTop, mouseX, mouseY, tile.energy.getEnergyStored(), tile.energyUsage);
        if(mouseX >= guiLeft + 73 && mouseX <= guiLeft + 73 + 30 && mouseY >= guiTop + 29 && mouseY <= guiTop + 29 + 25){
            Util.drawProgressText(gui, mouseX, mouseY, tile.burn, tile.maxBurn, true);
        }
    }
}
