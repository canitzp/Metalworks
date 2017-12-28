package de.canitzp.metalworks.machine.crusher;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.client.gui.GuiEnergyBar;
import de.canitzp.metalworks.client.gui.GuiMachine;
import de.canitzp.metalworks.integration.jei.SimpleSteelJEIPlugin;
import de.canitzp.metalworks.inventory.ContainerMachine;
import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.duster.TileDuster;
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
public class InterfaceCrusher implements IMachineInterface<TileCrusher> {

    public static final ResourceLocation LOC = new ResourceLocation(Metalworks.MODID, "textures/gui/crusher.png");

    @SideOnly(Side.CLIENT)
    @Override
    public void initGui(TileCrusher tile, GuiMachine<TileCrusher> gui) {
        gui.setSize(176, 74);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(TileCrusher tile, GuiMachine<TileCrusher> gui, TextureManager texture, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        texture.bindTexture(LOC);
        gui.drawTexturedModalRect(guiLeft, guiTop, 0, 0, gui.getXSize(), gui.getYSize());
        if(tile.maxBurn > 0){
            int i = (int) ((tile.burn / (tile.maxBurn  * 1.0F)) * 25.0F);
            gui.drawTexturedModalRect(guiLeft + 83, guiTop + 25, 0, 74, 30, 25 - i);
        }
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public JEIData getJEIClickArea(TileCrusher tile, GuiMachine<TileCrusher> gui, EntityPlayer player, int guiLeft, int guiTop) {
        return new JEIData(83, 25, 30, 25, SimpleSteelJEIPlugin.CRUSHER);
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public GuiEnergyBar getEnergyBar(TileCrusher tile, GuiMachine<TileCrusher> gui, EntityPlayer player, int guiLeft, int guiTop) {
        return new GuiEnergyBar(guiLeft + 60, guiTop + 6, false);
    }

    @Nullable
    @Override
    public Pair<Integer, Integer> getInventoryLocation(TileCrusher tile, EntityPlayer player) {
        return Pair.of(0, 74);
    }

    @Override
    public void initSlots(TileCrusher tile, ContainerMachine<TileCrusher> container, EntityPlayer player) {
        container.addSlot(new Slot(tile.inv, 0, 90, 6));
        container.addSlot(new SlotFurnaceOutput(player, tile.inv, 1, 80, 52));
        container.addSlot(new SlotFurnaceOutput(player, tile.inv, 2, 100, 52));
    }
}
