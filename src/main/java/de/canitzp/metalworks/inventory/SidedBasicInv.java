package de.canitzp.metalworks.inventory;

import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public abstract class SidedBasicInv extends InventoryBasic implements ISidedInventory {

    private Map<EnumFacing, SidedWrapper> cachedFaces = new HashMap<>();
    private TileBase tile = null;

    public SidedBasicInv(String title, int slotCount) {
        super(title, false, slotCount);
    }

    public SidedBasicInv setTile(TileBase tile){
        this.tile = tile;
        return this;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if(this.tile != null){
            this.tile.markDirty();
            this.tile.syncToClients();
        }
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        int[] ints = new int[this.getSizeInventory()];
        for(int i = 0; i < this.getSizeInventory(); i++){
            ints[i] = i;
        }
        return ints;
    }

    public SidedWrapper getSidedWrapper(EnumFacing side){
        if(side != null){
            if(cachedFaces.containsKey(side)){
                return cachedFaces.get(side);
            } else {
                cachedFaces.put(side, new SidedWrapper(this, side));
                return getSidedWrapper(side);
            }
        }
        return null;
    }

}
