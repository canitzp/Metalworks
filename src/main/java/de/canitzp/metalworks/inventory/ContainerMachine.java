package de.canitzp.metalworks.inventory;

import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

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
        Pair<Integer, Integer> invCoords = machineInterface.getInventoryLocation(tile, player);
        if(invCoords != null){
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlot(new Slot(player.inventory, j + i * 9 + 9, (invCoords.getLeft() + 8) + j * 18, (invCoords.getRight() + 3) + i * 18));
                }
            }
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(player.inventory, k, (invCoords.getLeft() + 8) + k * 18, (invCoords.getRight() + 61)));
            }
        }
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
