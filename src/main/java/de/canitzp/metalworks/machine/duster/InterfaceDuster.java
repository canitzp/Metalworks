package de.canitzp.metalworks.machine.duster;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.client.gui.GuiEnergyBar;
import de.canitzp.metalworks.client.gui.GuiMachine;
import de.canitzp.metalworks.integration.jei.SimpleSteelJEIPlugin;
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

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class InterfaceDuster implements IMachineInterface<TileDuster> {

    public static final ResourceLocation LOC = new ResourceLocation(Metalworks.MODID, "textures/gui/duster.png");

    @SideOnly(Side.CLIENT)
    @Override
    public void initGui(TileDuster tile, GuiMachine<TileDuster> gui) {
        gui.setSize(176, 166);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(TileDuster tile, GuiMachine<TileDuster> gui, TextureManager texture, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        texture.bindTexture(LOC);
        gui.drawTexturedModalRect(guiLeft, guiTop, 0, 0, gui.getXSize(), gui.getYSize());
        if(tile.maxBurn > 0){
            int i = (int) ((tile.burn / (tile.maxBurn  * 1.0F)) * 25.0F);
            gui.drawTexturedModalRect(guiLeft + 73, guiTop + 29, 176, 0, 30, 25 - i);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawScreen(TileDuster tile, GuiMachine<TileDuster> gui, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        if(mouseX >= guiLeft + 73 && mouseX <= guiLeft + 73 + 30 && mouseY >= guiTop + 29 && mouseY <= guiTop + 29 + 25){
            Util.drawProgressText(gui, mouseX, mouseY, tile.burn, tile.maxBurn, true);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiEnergyBar getEnergyBar(TileDuster tile, GuiMachine<TileDuster> gui, EntityPlayer player, int guiLeft, int guiTop) {
        return new GuiEnergyBar(guiLeft + 11, guiTop + 11);
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public JEIData getJEIClickArea(TileDuster tile, GuiMachine<TileDuster> gui, EntityPlayer player, int guiLeft, int guiTop) {
        return new JEIData(73, 29, 30, 25, SimpleSteelJEIPlugin.DUSTER);
    }

    @Nullable
    @Override
    public Pair<Integer, Integer> getInventoryLocation(TileDuster tile, EntityPlayer player) {
        return Pair.of(0, 81);
    }

    @Override
    public void initSlots(TileDuster tile, ContainerMachine<TileDuster> container, EntityPlayer player) {
        container.addSlot(new Slot(tile.inventory, TileDuster.INPUT1, 71, 11));
        container.addSlot(new Slot(tile.inventory, TileDuster.INPUT2, 91, 11));
        container.addSlot(new SlotFurnaceOutput(player, tile.inventory, TileDuster.OUTPUT, 81, 57));
    }
}
