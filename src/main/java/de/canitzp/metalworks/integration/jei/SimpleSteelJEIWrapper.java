package de.canitzp.metalworks.integration.jei;

import com.google.common.collect.Lists;
import de.canitzp.metalworks.machine.blastfurnace.RecipeBlastFurnace;
import de.canitzp.metalworks.machine.duster.RecipeDuster;
import de.canitzp.metalworks.machine.geothermalgenerator.geoburnable.IGeoburnable;
import de.canitzp.metalworks.recipe.OreDictStack;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public abstract class SimpleSteelJEIWrapper<T> implements IRecipeWrapper{

    public final T recipe;

    public SimpleSteelJEIWrapper(T recipe) {
        this.recipe = recipe;
    }

    public static class BlastFurnace implements IRecipeWrapper{
        public final RecipeBlastFurnace recipe;

        public BlastFurnace(RecipeBlastFurnace recipe) {
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
            mc.fontRenderer.drawString("Burn time: " + recipe.getBurnTime(), 50, 25, 0xFFFFFF, false);
            mc.fontRenderer.drawString("Energy   : " + recipe.getEnergyUsage(), 50, 35, 0xFFFFFF, false);
            if(!recipe.getOutputs()[1].isEmpty()){
                mc.fontRenderer.drawString(recipe.getSecondOutputChance() + "%", 52, 52, 0xFFFFFF, false);
            }
        }
    }

    public static class Duster extends SimpleSteelJEIWrapper<RecipeDuster>{

        public Duster(RecipeDuster recipe) {
            super(recipe);
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            List<List<ItemStack>> inputList = new ArrayList<>();
            for(OreDictStack stack : this.recipe.getInputs()){
                inputList.add(stack.getListForJEI());
            }
            ingredients.setInputLists(ItemStack.class, inputList);
            ingredients.setOutput(ItemStack.class, this.recipe.getOutput());
        }

        @Override
        public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            mc.fontRenderer.drawString("Burn time: " + recipe.getTime(), 45, 25, 0xFFFFFF, false);
            mc.fontRenderer.drawString("Energy   : " + recipe.getEnergy(), 45, 35, 0xFFFFFF, false);
        }
    }

    public static class GeothermalGenerator extends SimpleSteelJEIWrapper<IGeoburnable>{

        public GeothermalGenerator(IGeoburnable recipe) {
            super(recipe);
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInput(ItemStack.class, this.recipe.getJEIIcon());
        }

        @Override
        public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            this.recipe.drawJEIText(mc.fontRenderer);
        }

    }

}
