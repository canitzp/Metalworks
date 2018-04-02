package de.canitzp.metalworks.machine.tank;

import de.canitzp.metalworks.CustomFluidTank;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author canitzp
 */
public class TileTank extends TileBase {

    public final CustomFluidTank tank = new CustomFluidTank(20000).setTile(this);

    @Override
    protected Triple<Boolean, Boolean, Boolean> hasEnergyFluidInv() {
        return Triple.of(false, true, false);
    }

    @Nullable
    @Override
    public IFluidHandler getTank(@Nullable EnumFacing side) {
        return this.tank;
    }

}
