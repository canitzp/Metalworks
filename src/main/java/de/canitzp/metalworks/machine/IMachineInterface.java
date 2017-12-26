package de.canitzp.metalworks.machine;

import de.canitzp.metalworks.gui.GuiMachine;
import de.canitzp.metalworks.inventory.ContainerMachine;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

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

    default void initSlots(T tile, ContainerMachine<T> container){}

    @Nonnull
    default ItemStack shiftStack(T tile, World world, EntityPlayer player, int index){
        return ItemStack.EMPTY;
    }

}
