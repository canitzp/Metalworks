package de.canitzp.simplesteel.machine.geothermalgenerator.geoburnable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * @author canitzp
 */
public interface IGeoburnable extends IForgeRegistryEntry<IGeoburnable> {

    public static final int DEFAULT_BURN_TIME = 100;

    int getBurnTime(World world, BlockPos pos, IBlockState state, EnumFacing side);

    default int getBurnEnergy(World world, BlockPos pos, IBlockState state, EnumFacing side){
        return 2;
    }

    default boolean isBurnable(IBlockState state, EnumFacing side){
        return true;
    }
}
