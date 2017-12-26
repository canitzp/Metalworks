package de.canitzp.metalworks.inventory;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * @author canitzp
 */
public class SidedWrapper extends SidedInvWrapper {

    public SidedWrapper(ISidedInventory inv, EnumFacing side) {
        super(inv, side);
    }

}
