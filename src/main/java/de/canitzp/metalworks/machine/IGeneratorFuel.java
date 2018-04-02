package de.canitzp.metalworks.machine;

import de.canitzp.metalworks.config.ConfMachines;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

/**
 * @author canitzp
 */
public interface IGeneratorFuel extends IForgeRegistryEntry<IGeneratorFuel> {

    int DEFAULT_BURN_TIME = ConfMachines.BG_DEFAULT_BURN_TIME;
    int DEFAULT_ENERGY_PRODUCED = ConfMachines.BG_DEFAULT_ENERGY;

    ItemStack getStack();

    ItemStack getWaste();

    FluidStack getFluid();

    int getFuelTime(ItemStack stack);

    int getEnergyPerTick(ItemStack stack);

    List<GeneratorTypes> workingIn(ItemStack stack);

    enum GeneratorTypes{
        BIO
    }

}
