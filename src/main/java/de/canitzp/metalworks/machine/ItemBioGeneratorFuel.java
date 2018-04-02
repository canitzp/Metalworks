package de.canitzp.metalworks.machine;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Registry;
import net.minecraft.block.IGrowable;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author canitzp
 */
public class ItemBioGeneratorFuel extends DefaultGeneratorFuel {

    private final ItemStack stack;
    private final ItemStack waste;
    private final FluidStack fluidStack;
    private final int burnTime;
    private final int energyPerTick;
    private final List<GeneratorTypes> generators;

    public ItemBioGeneratorFuel(ItemStack stack, ItemStack waste, FluidStack fluidStack, int burnTime, int energyPerTick, GeneratorTypes... generators){
        this.stack = stack;
        this.waste = waste;
        this.fluidStack = fluidStack;
        this.burnTime = burnTime;
        this.energyPerTick = energyPerTick;
        this.generators = Arrays.asList(generators);
        String name = Arrays.stream(generators).map(type -> "." + type.name()).collect(Collectors.joining());
        this.setRegistryName(Metalworks.MODID, "generator_fuel." + name + Objects.requireNonNull(stack.getItem().getRegistryName()).getResourcePath());
    }

    public ItemBioGeneratorFuel(ItemStack stack, ItemStack waste, FluidStack fluidStack, int energyPerTick, GeneratorTypes... generators){
        this(stack, waste, fluidStack, DEFAULT_BURN_TIME, energyPerTick, generators);
    }

    public ItemBioGeneratorFuel(ItemStack stack, ItemStack waste, FluidStack fluidStack, GeneratorTypes... generators){
        this(stack, waste, fluidStack, DEFAULT_ENERGY_PRODUCED, generators);
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public ItemStack getWaste() {
        return this.waste;
    }

    @Override
    public FluidStack getFluid() {
        return this.fluidStack;
    }

    @Override
    public int getFuelTime(ItemStack stack) {
        return this.burnTime;
    }

    @Override
    public int getEnergyPerTick(ItemStack stack) {
        return this.energyPerTick;
    }

    @Override
    public List<GeneratorTypes> workingIn(ItemStack stack) {
        return this.generators;
    }

    public static void registerDefaultRecipes(IForgeRegistry<IGeneratorFuel> reg){
        ItemStack waste = new ItemStack(Registry.bioWaste);
        FluidStack water = new FluidStack(FluidRegistry.WATER, 200);

        ForgeRegistries.BLOCKS.getValuesCollection().stream().filter(block -> block instanceof IGrowable).filter(block -> !new ItemStack(block).isEmpty())
                .forEach(block -> reg.register(new ItemBioGeneratorFuel(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), waste, water, 75, 5, GeneratorTypes.BIO)));
        ForgeRegistries.ITEMS.getValuesCollection().stream().filter(item -> item instanceof IPlantable)
                .forEach(item -> reg.register(new ItemBioGeneratorFuel(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), waste, water, 75, 5, GeneratorTypes.BIO)));
        ForgeRegistries.ITEMS.getValuesCollection().stream().filter(item -> item instanceof ItemFood)
                .forEach(item -> reg.register(new ItemBioGeneratorFuel(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), waste, water, 150, 5, GeneratorTypes.BIO)));
    }

}
