package de.canitzp.simplesteel;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * @author canitzp
 */
public class Util {

    public static final String ENERY_UNIT = "CF";

    public static boolean isDayTime(World world){
        return world.isDaytime();
    }

    public static boolean canBlockSeeSky(World world, BlockPos pos){
        for(int i = pos.getY() + 1; i <= world.getHeight(); i++){
            pos = pos.offset(EnumFacing.UP);
            if(!isBlockTransparent(world, pos, world.getBlockState(pos))){
                return false;
            }
        }
        return true;
    }

    public static boolean isBlockTransparent(World world, BlockPos pos, IBlockState state){
        return state.getBlock() instanceof BlockAir || !state.getMaterial().isOpaque() || !state.isBlockNormalCube() || !state.isFullBlock() || !state.isOpaqueCube();
    }

    public static String formatEnergy(IEnergyStorage energy){
        return String.format("%s / %s", formatEnergy(energy.getEnergyStored()), formatEnergy(energy.getMaxEnergyStored()));
    }

    public static String formatEnergy(int energy) {
        if (energy < 1000) {
            return energy + " " + ENERY_UNIT;
        }
        final int exp = (int) (Math.log(energy) / Math.log(1000));
        final char unitType = "kmg".charAt(exp - 1);
        return String.format("%.1f %s%s", energy / Math.pow(1000, exp), unitType, ENERY_UNIT);
    }

    public static int pushEnergy(World world, BlockPos pos, IEnergyStorage energy, EnumFacing... ignore){
        if(!world.isRemote && energy.canExtract()){
            for(EnumFacing side : EnumFacing.values()){
                if(ignore.length > 0 && ArrayUtils.contains(ignore, side)){
                   continue;
                }
                TileEntity tile = world.getTileEntity(pos.offset(side));
                if(tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())){
                    IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
                    if(storage != null && storage.canReceive()){
                        return energy.extractEnergy(storage.receiveEnergy(energy.extractEnergy(Integer.MAX_VALUE, true), false), false);
                    }
                }
            }
        }
        return 0;
    }

}
