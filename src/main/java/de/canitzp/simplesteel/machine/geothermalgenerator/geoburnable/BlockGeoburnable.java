package de.canitzp.simplesteel.machine.geothermalgenerator.geoburnable;

import de.canitzp.simplesteel.SimpleSteel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class BlockGeoburnable extends DefaultGeoBurnable {

    public IBlockState state;
    public int energy = 0, burn = DEFAULT_BURN_TIME;

    public BlockGeoburnable(IBlockState state, int burnTime, int energy){
        this.state = state;
        this.energy = energy;
        this.burn = burnTime;
        this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, "geoburnable:" + state.toString()));
    }

    public BlockGeoburnable(Block block, int burnTime, int energy){
        this(block.getDefaultState(), burnTime, energy);
    }

    public BlockGeoburnable(IBlockState state, int burnTime){
        this(state, burnTime, 0);
    }

    public BlockGeoburnable(Block block, int burnTime){
        this(block, burnTime, 0);
    }

    public BlockGeoburnable(IBlockState state){
        this(state, DEFAULT_BURN_TIME, 0);
    }

    public BlockGeoburnable(Block block){
        this(block, DEFAULT_BURN_TIME, 0);
    }

    @Override
    public int getBurnTime(World world, BlockPos pos, IBlockState state, EnumFacing side) {
        return this.burn;
    }

    @Override
    public int getBurnEnergy(World world, BlockPos pos, IBlockState state, EnumFacing side) {
        return this.energy > 0 ? this.energy : super.getBurnEnergy(world, pos, state, side);
    }

    @Override
    public boolean isBurnable(IBlockState state, EnumFacing side) {
        return state.equals(this.state);
    }
}
