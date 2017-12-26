package de.canitzp.metalworks.inventory;

import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class ContainerMachine<T extends TileBase> extends Container {

    private IMachineInterface<T> machineInterface;
    private T tile;
    private EntityPlayer player;

    public ContainerMachine(EntityPlayer player, T tile, IMachineInterface<T> machineInterface){
        this.machineInterface = machineInterface;
        this.tile = tile;
        this.player = player;
        machineInterface.initSlots(tile, this, player);
        if(!player.world.isRemote){
            tile.syncToClients();
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.canBeUsedBy(player);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        return this.machineInterface.shiftStack(this.tile, player.world, player, index);
    }

    public Slot addSlot(Slot slot){
        return this.addSlotToContainer(slot);
    }
}
