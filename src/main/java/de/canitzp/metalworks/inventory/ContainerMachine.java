package de.canitzp.metalworks.inventory;

import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class ContainerMachine<T extends TileBase> extends Container {

    private IMachineInterface<T> machineInterface;
    private T tile;

    public ContainerMachine(T tile, IMachineInterface<T> machineInterface){
        this.machineInterface = machineInterface;
        this.tile = tile;
        machineInterface.initSlots(tile, this);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true; // TODO
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return this.machineInterface.shiftStack(this.tile, player.world, player, index);
    }
}
