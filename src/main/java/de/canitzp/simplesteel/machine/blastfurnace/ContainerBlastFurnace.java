package de.canitzp.simplesteel.machine.blastfurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class ContainerBlastFurnace extends Container {

    public ContainerBlastFurnace(EntityPlayer player, TileBlastFurnace tile) {
        this.addSlotToContainer(new Slot(tile.inventory, TileBlastFurnace.INPUT1, 61, 11));
        this.addSlotToContainer(new Slot(tile.inventory, TileBlastFurnace.INPUT2, 81, 11));
        this.addSlotToContainer(new Slot(tile.inventory, TileBlastFurnace.INPUT3, 101, 11));
        this.addSlotToContainer(new SlotFurnaceOutput(player, tile.inventory, TileBlastFurnace.OUTPUT1, 71, 57));
        this.addSlotToContainer(new SlotFurnaceOutput(player, tile.inventory, TileBlastFurnace.OUTPUT2, 91, 57));
        this.addSlotToContainer(new Slot(tile.inventory, TileBlastFurnace.FUEL, 11, 11){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return TileEntityFurnace.isItemFuel(stack);
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 142));
        }
        if(!player.world.isRemote){
            tile.sync(true);
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return true;
    }

}
