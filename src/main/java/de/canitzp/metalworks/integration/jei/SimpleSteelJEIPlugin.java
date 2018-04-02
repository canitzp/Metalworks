package de.canitzp.metalworks.integration.jei;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Registry;
import de.canitzp.metalworks.client.gui.GuiMachine;
import de.canitzp.metalworks.machine.ItemBioGeneratorFuel;
import de.canitzp.metalworks.machine.MachineRecipe;
import de.canitzp.metalworks.machine.blastfurnace.RecipeBlastFurnace;
import de.canitzp.metalworks.machine.crusher.RecipeCrusher;
import de.canitzp.metalworks.machine.duster.RecipeDuster;
import de.canitzp.metalworks.machine.geothermalgenerator.geoburnable.IGeoburnable;
import de.canitzp.metalworks.recipe.SimpleSteelRecipeHandler;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author canitzp
 */
@JEIPlugin
public class SimpleSteelJEIPlugin implements IModPlugin{

    public static final String BLAST_FURNACE = Metalworks.MODID + ".blast_furnace";
    public static final String DUSTER = Metalworks.MODID + ".duster";
    public static final String GEOTHERMAL_GENERATOR = Metalworks.MODID + ".geothermal_generator";
    public static final String CRUSHER = Metalworks.MODID + ".crusher";
    public static final String BIO_GENERATOR = Metalworks.MODID + ".bio_generator";

    private static IRecipesGui recipesGui;

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        GuiMachine.isJeiLoaded = true;
        recipesGui = jeiRuntime.getRecipesGui();
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new SimpleSteelCategories.BlastFurnace(registry));
        registry.addRecipeCategories(new SimpleSteelCategories.Duster(registry));
        registry.addRecipeCategories(new SimpleSteelCategories.GeoThermalGenerator(registry));
        registry.addRecipeCategories(new SimpleSteelCategories.Crusher(registry));
        registry.addRecipeCategories(new SimpleSteelCategories.BioGenerator(registry));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(RecipeBlastFurnace.class, SimpleSteelJEIWrapper.BlastFurnace::new, BLAST_FURNACE);
        registry.handleRecipes(RecipeDuster.class, SimpleSteelJEIWrapper.Duster::new, DUSTER);
        registry.handleRecipes(IGeoburnable.class, SimpleSteelJEIWrapper.GeothermalGenerator::new, GEOTHERMAL_GENERATOR);
        registry.handleRecipes(RecipeCrusher.class, SimpleSteelJEIWrapper.Crusher::new, CRUSHER);
        registry.handleRecipes(ItemBioGeneratorFuel.class, SimpleSteelJEIWrapper.BioGenerator::new, BIO_GENERATOR);

        registry.addRecipes(SimpleSteelRecipeHandler.BLAST_FURNACE_RECIPES.values(), BLAST_FURNACE);
        registry.addRecipes(MachineRecipe.getRegisteredForType(RecipeDuster.class), DUSTER);
        registry.addRecipes(Metalworks.GEOBURNABLE_REGISTRY.getValuesCollection(), GEOTHERMAL_GENERATOR);
        registry.addRecipes(MachineRecipe.getRegisteredForType(RecipeCrusher.class), CRUSHER);
        registry.addRecipes(Metalworks.GENERATOR_FUEL_REGISTRY.getValuesCollection().stream().filter(iGeneratorFuel -> iGeneratorFuel instanceof ItemBioGeneratorFuel).collect(Collectors.toList()), BIO_GENERATOR);

        registry.addRecipeCatalyst(new ItemStack(Registry.blastFurnace), BLAST_FURNACE);
        registry.addRecipeCatalyst(new ItemStack(Registry.duster), DUSTER);
        registry.addRecipeCatalyst(new ItemStack(Registry.geothermalGenerator), GEOTHERMAL_GENERATOR);
        registry.addRecipeCatalyst(new ItemStack(Registry.crusher), CRUSHER);
        registry.addRecipeCatalyst(new ItemStack(Registry.bioGenerator), BIO_GENERATOR);
    }

    public static void openRecipes(List<String> cats){
        if(recipesGui != null){
            recipesGui.showCategories(cats);
        }
    }

}
