package de.canitzp.metalworks.integration.jei;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.machine.blastfurnace.InterfaceBlastFurnace;
import de.canitzp.metalworks.machine.blastfurnace.TileBlastFurnace;
import de.canitzp.metalworks.machine.duster.InterfaceDuster;
import de.canitzp.metalworks.recipe.OreDictStack;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public abstract class SimpleSteelCategories<T extends IRecipeWrapper> implements IRecipeCategory<T>{

    private final String id;
    private final IDrawable drawable, backupDrawable;

    public SimpleSteelCategories(IRecipeCategoryRegistration reg, String id) {
        this.id = id;
        this.drawable = createDrawable(reg.getJeiHelpers().getGuiHelper());
        this.backupDrawable = reg.getJeiHelpers().getGuiHelper().createBlankDrawable(16, 16);
    }

    protected abstract IDrawable createDrawable(IGuiHelper helper);

    @Nonnull
    @Override
    public String getUid() {
        return this.id;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format("container.jei." + this.id + ".name");
    }

    @Nonnull
    @Override
    public String getModName() {
        return Metalworks.MODNAME;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.drawable != null ? this.drawable : this.backupDrawable;
    }

    public static class BlastFurnace extends SimpleSteelCategories<SimpleSteelJEIWrapper.BlastFurnace>{

        public BlastFurnace(IRecipeCategoryRegistration reg){
            super(reg, SimpleSteelJEIPlugin.BLAST_FURNACE);
        }

        @Override
        protected IDrawable createDrawable(IGuiHelper helper) {
            return helper.createDrawable(InterfaceBlastFurnace.LOC, 59, 9, 60, 66);
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, SimpleSteelJEIWrapper.BlastFurnace recipeWrapper, IIngredients ingredients) {
            IGuiItemStackGroup group = recipeLayout.getItemStacks();
            group.init(TileBlastFurnace.INPUT1, true, 1, 1);
            group.set(TileBlastFurnace.INPUT1, recipeWrapper.recipe.getInputs()[0].getListForJEI());
            if(recipeWrapper.recipe.getInputs()[1] != OreDictStack.EMPTY){
                group.init(TileBlastFurnace.INPUT2, true, 21, 1);
                group.set(TileBlastFurnace.INPUT2, recipeWrapper.recipe.getInputs()[1].getListForJEI());
            }
            if(recipeWrapper.recipe.getInputs()[2] != OreDictStack.EMPTY){
                group.init(TileBlastFurnace.INPUT3, true, 21, 1);
                group.set(TileBlastFurnace.INPUT3, recipeWrapper.recipe.getInputs()[2].getListForJEI());
            }

            group.init(TileBlastFurnace.OUTPUT1, false, 11, 47);
            group.set(TileBlastFurnace.OUTPUT1, recipeWrapper.recipe.getOutputs()[0]);
            if(!recipeWrapper.recipe.getOutputs()[1].isEmpty()){
                group.init(TileBlastFurnace.OUTPUT2, false, 31, 47);
                group.set(TileBlastFurnace.OUTPUT2, recipeWrapper.recipe.getOutputs()[1]);
            }
        }
    }

    public static class Duster extends SimpleSteelCategories<SimpleSteelJEIWrapper.Duster> {

        public Duster(IRecipeCategoryRegistration reg) {
            super(reg, SimpleSteelJEIPlugin.DUSTER);
        }

        @Override
        protected IDrawable createDrawable(IGuiHelper helper) {
            return helper.createDrawable(InterfaceDuster.LOC, 59, 9, 60, 66);
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, SimpleSteelJEIWrapper.Duster recipeWrapper, IIngredients ingredients) {
            IGuiItemStackGroup group = recipeLayout.getItemStacks();
            OreDictStack[] inputs = recipeWrapper.recipe.getInputs();
            if(inputs[0] != OreDictStack.EMPTY){
                group.init(0, true, 11, 1);
                group.set(0, inputs[0].getListForJEI());
            }
            if(inputs[1] != OreDictStack.EMPTY){
                group.init(1, true, 31, 1);
                group.set(1, inputs[1].getListForJEI());
            }
            group.init(2, false, 21, 47);
            group.set(2, recipeWrapper.recipe.getOutput());
        }

    }

    public static class GeoThermalGenerator extends SimpleSteelCategories<SimpleSteelJEIWrapper.GeothermalGenerator> {

        public GeoThermalGenerator(IRecipeCategoryRegistration reg) {
            super(reg, SimpleSteelJEIPlugin.GEOTHERMAL_GENERATOR);
        }

        @Override
        protected IDrawable createDrawable(IGuiHelper helper) {
            return helper.createBlankDrawable(50, 30);
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, SimpleSteelJEIWrapper.GeothermalGenerator recipeWrapper, IIngredients ingredients) {
            recipeLayout.getItemStacks().init(0, true, 0, 0);
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
        }
    }

}
