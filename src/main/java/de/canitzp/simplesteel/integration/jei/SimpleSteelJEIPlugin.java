package de.canitzp.simplesteel.integration.jei;

import com.google.common.collect.Lists;
import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import de.canitzp.simplesteel.machine.MachineRecipe;
import de.canitzp.simplesteel.machine.blastfurnace.GuiBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.RecipeBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.TileBlastFurnace;
import de.canitzp.simplesteel.machine.duster.GuiDuster;
import de.canitzp.simplesteel.machine.duster.RecipeDuster;
import de.canitzp.simplesteel.machine.geothermalgenerator.geoburnable.IGeoburnable;
import de.canitzp.simplesteel.recipe.OreDictStack;
import de.canitzp.simplesteel.recipe.SimpleSteelRecipeHandler;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
@JEIPlugin
public class SimpleSteelJEIPlugin implements IModPlugin{

    public static final String BLAST_FURNACE = SimpleSteel.MODID + ".blast_furnace";
    public static final String DUSTER = SimpleSteel.MODID + ".duster";
    public static final String GEOTHERMAL_GENERATOR = SimpleSteel.MODID + ".geothermal_generator";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new SimpleSteelCategories.BlastFurnace(registry));
        registry.addRecipeCategories(new SimpleSteelCategories.Duster(registry));
        registry.addRecipeCategories(new SimpleSteelCategories.GeoThermalGenerator(registry));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(RecipeBlastFurnace.class, SimpleSteelJEIWrapper.BlastFurnace::new, BLAST_FURNACE);
        registry.handleRecipes(RecipeDuster.class, SimpleSteelJEIWrapper.Duster::new, DUSTER);
        registry.handleRecipes(IGeoburnable.class, SimpleSteelJEIWrapper.GeothermalGenerator::new, GEOTHERMAL_GENERATOR);

        registry.addRecipes(SimpleSteelRecipeHandler.BLAST_FURNACE_RECIPES.values(), BLAST_FURNACE);
        registry.addRecipes(MachineRecipe.getRegisteredForType(RecipeDuster.class), DUSTER);
        registry.addRecipes(SimpleSteel.GEOBURNABLE_REGISTRY.getValues(), GEOTHERMAL_GENERATOR);

        registry.addRecipeCatalyst(new ItemStack(Registry.blastFurnace), BLAST_FURNACE);
        registry.addRecipeCatalyst(new ItemStack(Registry.duster), DUSTER);
        registry.addRecipeCatalyst(new ItemStack(Registry.geothermalGenerator), GEOTHERMAL_GENERATOR);

        registry.addRecipeClickArea(GuiBlastFurnace.class, 68, 29, 42, 26, BLAST_FURNACE);
        registry.addRecipeClickArea(GuiDuster.class, 73, 29, 30, 25, DUSTER);
    }

}
