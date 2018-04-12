package de.canitzp.metalworks.block.plant.papyrus;

import de.canitzp.metalworks.Metalworks;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author canitzp
 */
public class BlockPapyrus extends BlockBush implements IGrowable {

    public static final PropertyInteger STATE = PropertyInteger.create("grow_state", 0, 15);

    public BlockPapyrus(){
        this.setRegistryName(Metalworks.MODID, "plant_papyrus");
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return false;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

    }
}
