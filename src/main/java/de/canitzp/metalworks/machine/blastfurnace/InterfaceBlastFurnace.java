package de.canitzp.metalworks.machine.blastfurnace;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.client.gui.GuiEnergyBar;
import de.canitzp.metalworks.gui.GuiMachine;
import de.canitzp.metalworks.inventory.ContainerMachine;
import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.duster.TileDuster;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class InterfaceBlastFurnace implements IMachineInterface<TileBlastFurnace> {

    public static final ResourceLocation LOC = new ResourceLocation(Metalworks.MODID, "textures/gui/blast_furnace.png");
    private GuiEnergyBar energyBar = new GuiEnergyBar();

    @Override
    public void initGui(TileBlastFurnace tile, GuiMachine<TileBlastFurnace> gui) {
        gui.setSize(176, 166);
    }

    @Override
    public void drawBackground(TileBlastFurnace tile, GuiMachine<TileBlastFurnace> gui, TextureManager texture, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        texture.bindTexture(LOC);
        gui.drawTexturedModalRect(guiLeft, guiTop, 0, 0, gui.getXSize(), gui.getYSize());
        if(tile.maxBurn > 0){
            int i = (int) ((tile.burnLeft / (tile.maxBurn  * 1.0F)) * 26);
            gui.drawTexturedModalRect(guiLeft + 68, guiTop + 29, 180, 0, 43, 26 - i);
        }
        if(tile.energy.getMaxEnergyStored() > 0){
            this.energyBar.draw(guiLeft + 11, guiTop + 11, tile.energy.getEnergyStored(), tile.energy.getMaxEnergyStored(), -1);
        }
    }

    @Override
    public void drawScreen(TileBlastFurnace tile, GuiMachine<TileBlastFurnace> gui, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        this.energyBar.mouseDraw(gui, guiLeft, guiTop, mouseX, mouseY, tile.energy.getEnergyStored(), tile.energyUsage);
    }

    @Override
    public void initSlots(TileBlastFurnace tile, ContainerMachine<TileBlastFurnace> container, EntityPlayer player) {
        container.addSlot(new Slot(tile.inventory, TileBlastFurnace.INPUT1, 61, 11));
        container.addSlot(new Slot(tile.inventory, TileBlastFurnace.INPUT2, 81, 11));
        container.addSlot(new Slot(tile.inventory, TileBlastFurnace.INPUT3, 101, 11));
        container.addSlot(new SlotFurnaceOutput(player, tile.inventory, TileBlastFurnace.OUTPUT1, 71, 57));
        container.addSlot(new SlotFurnaceOutput(player, tile.inventory, TileBlastFurnace.OUTPUT2, 91, 57));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                container.addSlot(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            container.addSlot(new Slot(player.inventory, k, 8 + k * 18, 142));
        }
    }

}
