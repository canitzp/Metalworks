package de.canitzp.metalworks.machine;

import de.canitzp.metalworks.client.gui.GuiEnergyBar;
import de.canitzp.metalworks.client.gui.GuiFluidBar;
import de.canitzp.metalworks.client.gui.GuiMachine;
import de.canitzp.metalworks.inventory.ContainerMachine;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public interface IMachineInterface<T extends TileBase> {

    @SideOnly(Side.CLIENT)
    default void initGui(T tile, GuiMachine<T> gui){}

    @SideOnly(Side.CLIENT)
    default void drawBackground(T tile, GuiMachine<T> gui, TextureManager texture, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){}

    @SideOnly(Side.CLIENT)
    default void drawScreen(T tile, GuiMachine<T> gui, int guiLeft, int guiTop, int mouseX, int mouseY, float partialTicks){}

    @SideOnly(Side.CLIENT)
    @Nullable
    default GuiEnergyBar getEnergyBar(T tile, GuiMachine<T> gui, EntityPlayer player, int guiLeft, int guiTop){
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    default Map<EnumFacing, GuiFluidBar> getFluidBar(T tile, GuiMachine<T> gui, EntityPlayer player, int guiLeft, int guiTop){
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    default JEIData getJEIClickArea(T tile, GuiMachine<T> gui, EntityPlayer player, int guiLeft, int guiTop){
        return null;
    }

    @Nullable
    default Pair<Integer, Integer> getInventoryLocation(T tile, EntityPlayer player){
        return null;
    }

    default void initSlots(T tile, ContainerMachine<T> container, EntityPlayer player){}

    @SuppressWarnings("SameReturnValue")
    @Nonnull
    default ItemStack shiftStack(T tile, World world, EntityPlayer player, int index){
        return ItemStack.EMPTY;
    }

    @SideOnly(Side.CLIENT)
    class JEIData{
        public final int x;
        public final int y;
        public final int width;
        public final int height;
        public final List<String> cats;
        public JEIData(int x, int y, int width, int height, String... cats){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.cats = Arrays.asList(cats);
        }

    }

}
