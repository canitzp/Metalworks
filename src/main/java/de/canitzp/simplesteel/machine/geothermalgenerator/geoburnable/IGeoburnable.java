package de.canitzp.simplesteel.machine.geothermalgenerator.geoburnable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * @author canitzp
 */
public interface IGeoburnable extends IForgeRegistryEntry<IGeoburnable> {

    int DEFAULT_BURN_TIME = 100;
    int DEFAULT_ENERGY = 1;
    int DEFAULT_COOLDOWN = 20;

    int getBurnTime(World world, BlockPos pos, IBlockState state, EnumFacing side);

    default int getBurnEnergy(World world, BlockPos pos, IBlockState state, EnumFacing side){
        return DEFAULT_ENERGY;
    }

    default boolean isBurnable(IBlockState state, EnumFacing side){
        return true;
    }

    default int getCooldown(World world, BlockPos pos, IBlockState state, EnumFacing side){
        return DEFAULT_COOLDOWN;
    }

    default ItemStack getJEIIcon(){
        return ItemStack.EMPTY;
    }

    default void drawJEIText(FontRenderer font){

    }
}
