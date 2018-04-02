package de.canitzp.metalworks.machine.biogenerator;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.client.gui.GuiEnergyBar;
import de.canitzp.metalworks.client.gui.GuiFluidBar;
import de.canitzp.metalworks.client.gui.GuiMachine;
import de.canitzp.metalworks.inventory.ContainerMachine;
import de.canitzp.metalworks.machine.IMachineInterface;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class InterfaceBioGenerator implements IMachineInterface<TileBioGenerator> {

    public static final ResourceLocation LOC = new ResourceLocation(Metalworks.MODID, "textures/gui/bio_generator.png");

    @SideOnly(Side.CLIENT)
    @Override
    public void initGui(TileBioGenerator tile, GuiMachine<TileBioGenerator> gui) {
        gui.setSize(176, 74);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(TileBioGenerator tile, GuiMachine<TileBioGenerator> gui, TextureManager texture, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks) {
        texture.bindTexture(LOC);
        gui.drawTexturedModalRect(guiLeft, guiTop, 0, 0, gui.getXSize(), gui.getYSize());
        if(tile.maxBurn > 0){
            int i = (tile.maxBurn - tile.burn) * 29 / tile.maxBurn;
            if(i < 29){
                gui.drawTexturedModalRect(guiLeft + 79, guiTop + 23, 0, 74, 18, i);
            }
        }
    }

    @Nullable
    @Override
    public Pair<Integer, Integer> getInventoryLocation(TileBioGenerator tile, EntityPlayer player) {
        return Pair.of(0, 74);
    }

    @Nullable
    @Override
    public Map<EnumFacing, GuiFluidBar> getFluidBar(TileBioGenerator tile, GuiMachine<TileBioGenerator> gui, EntityPlayer player, int guiLeft, int guiTop) {
        Map<EnumFacing, GuiFluidBar> map = new HashMap<>();
        map.put(EnumFacing.NORTH, new GuiFluidBar(60 + guiLeft, 6 + guiTop));
        return map;
    }

    @Nullable
    @Override
    public GuiEnergyBar getEnergyBar(TileBioGenerator tile, GuiMachine<TileBioGenerator> gui, EntityPlayer player, int guiLeft, int guiTop) {
        return new GuiEnergyBar(guiLeft + 100, guiTop + 6, false);
    }

    @Override
    public void initSlots(TileBioGenerator tile, ContainerMachine<TileBioGenerator> container, EntityPlayer player) {
        container.addSlot(new Slot(tile.inv, 0, 80, 6));
        container.addSlot(new SlotFurnaceOutput(player, tile.inv, 1, 80, 52));
    }
}
