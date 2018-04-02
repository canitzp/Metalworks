package de.canitzp.metalworks;

import de.canitzp.metalworks.machine.TileBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class CustomFluidTank extends FluidTank {

    private TileBase notifyTile;

    public CustomFluidTank(int capacity) {
        super(capacity);
    }

    public CustomFluidTank(@Nullable FluidStack fluidStack, int capacity) {
        super(fluidStack, capacity);
    }

    public CustomFluidTank(Fluid fluid, int amount, int capacity) {
        super(fluid, amount, capacity);
    }

    public CustomFluidTank setTile(TileBase tile){
        this.notifyTile = tile;
        this.setTileEntity(tile); // We wanna fire the FluidFillingEvent
        return this;
    }

    @Override
    protected void onContentsChanged() {
        if(this.notifyTile != null){
            this.notifyTile.syncToClients();
            this.notifyTile.markDirty();
        }
    }
}
