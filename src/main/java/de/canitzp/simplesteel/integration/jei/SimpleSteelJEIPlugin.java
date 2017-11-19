package de.canitzp.simplesteel.integration.jei;

import com.google.common.collect.Lists;
import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import de.canitzp.simplesteel.machine.blastfurnace.GuiBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.RecipeBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.TileBlastFurnace;
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

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new IRecipeCategory<BlastFurnaceRecipeWrapper>() {
            final IDrawable drawable = registry.getJeiHelpers().getGuiHelper().createDrawable(GuiBlastFurnace.LOC, 59, 9, 60, 66);
            @Nonnull
            @Override
            public String getUid() {
                return BLAST_FURNACE;
            }

            @Nonnull
            @Override
            public String getTitle() {
                return I18n.format("container.jei." + BLAST_FURNACE + ".name");
            }

            @Nonnull
            @Override
            public String getModName() {
                return SimpleSteel.MODNAME;
            }

            @Nonnull
            @Override
            public IDrawable getBackground() {
                return this.drawable;
            }

            @Override
            public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull BlastFurnaceRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
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
        });
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(RecipeBlastFurnace.class, BlastFurnaceRecipeWrapper::new, BLAST_FURNACE);

        registry.addRecipes(SimpleSteelRecipeHandler.BLAST_FURNACE_RECIPES.values(), BLAST_FURNACE);

        registry.addRecipeCatalyst(new ItemStack(Registry.blastFurnace), BLAST_FURNACE);

        registry.addRecipeClickArea(GuiBlastFurnace.class, 68, 29, 42, 26, BLAST_FURNACE);
    }

    private static class BlastFurnaceRecipeWrapper implements IRecipeWrapper{
        private final RecipeBlastFurnace recipe;

        private BlastFurnaceRecipeWrapper(RecipeBlastFurnace recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(@Nonnull IIngredients ingredients) {
            List<List<ItemStack>> inputList = new ArrayList<>();
            for(OreDictStack stack : recipe.getInputs()){
                inputList.add(stack.getListForJEI());
            }
            ingredients.setInputLists(ItemStack.class, inputList);
            ingredients.setOutputs(ItemStack.class, Lists.newArrayList(recipe.getOutputs()));
        }

        @Override
        public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            mc.fontRenderer.drawString("Burn time: " + recipe.getBurnTime(), 45, 30, 0xFFFFFF, false);
            if(!recipe.getOutputs()[1].isEmpty()){
                mc.fontRenderer.drawString(recipe.getSecondOutputChance() + "%", 52, 52, 0xFFFFFF, false);
            }
        }
    }

}
