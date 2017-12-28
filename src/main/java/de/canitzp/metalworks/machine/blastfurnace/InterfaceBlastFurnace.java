package de.canitzp.metalworks.machine.blastfurnace;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.client.gui.GuiEnergyBar;
import de.canitzp.metalworks.client.gui.GuiMachine;
import de.canitzp.metalworks.inventory.ContainerMachine;
import de.canitzp.metalworks.machine.IMachineInterface;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author canitzp
 */
public class InterfaceBlastFurnace implements IMachineInterface<TileBlastFurnace> {

    public static final ResourceLocation LOC = new ResourceLocation(Metalworks.MODID, "textures/gui/blast_furnace.png");

    @SideOnly(Side.CLIENT)
    @Override
    public void initGui(TileBlastFurnace tile, GuiMachine<TileBlastFurnace> gui) {
        gui.setSize(176, 166);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(TileBlastFurnace tile, GuiMachine<TileBlastFurnace> gui, TextureManager texture, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        texture.bindTexture(LOC);
        gui.drawTexturedModalRect(guiLeft, guiTop, 0, 0, gui.getXSize(), gui.getYSize());
        if(tile.maxBurn > 0){
            int i = (int) ((tile.burnLeft / (tile.maxBurn  * 1.0F)) * 26);
            gui.drawTexturedModalRect(guiLeft + 68, guiTop + 29, 180, 0, 43, 26 - i);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiEnergyBar getEnergyBar(TileBlastFurnace tile, GuiMachine<TileBlastFurnace> gui, EntityPlayer player, int guiLeft, int guiTop) {
        return new GuiEnergyBar(guiLeft + 11, guiTop + 11, false);
    }

    @Override
    public Pair<Integer, Integer> getInventoryLocation(TileBlastFurnace tile, EntityPlayer player) {
        return Pair.of(0, 81);
    }

    @Override
    public void initSlots(TileBlastFurnace tile, ContainerMachine<TileBlastFurnace> container, EntityPlayer player) {
        container.addSlot(new Slot(tile.inventory, TileBlastFurnace.INPUT1, 61, 11));
        container.addSlot(new Slot(tile.inventory, TileBlastFurnace.INPUT2, 81, 11));
        container.addSlot(new Slot(tile.inventory, TileBlastFurnace.INPUT3, 101, 11));
        container.addSlot(new SlotFurnaceOutput(player, tile.inventory, TileBlastFurnace.OUTPUT1, 71, 57));
        container.addSlot(new SlotFurnaceOutput(player, tile.inventory, TileBlastFurnace.OUTPUT2, 91, 57));
    }

}
