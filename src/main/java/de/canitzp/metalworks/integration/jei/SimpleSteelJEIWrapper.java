package de.canitzp.metalworks.integration.jei;

import com.google.common.collect.Lists;
import de.canitzp.metalworks.CustomFluidTank;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.client.gui.GuiEnergyBar;
import de.canitzp.metalworks.client.gui.GuiFluidBar;
import de.canitzp.metalworks.machine.IGeneratorFuel;
import de.canitzp.metalworks.machine.biogenerator.TileBioGenerator;
import de.canitzp.metalworks.machine.blastfurnace.RecipeBlastFurnace;
import de.canitzp.metalworks.machine.crusher.RecipeCrusher;
import de.canitzp.metalworks.machine.crusher.TileCrusher;
import de.canitzp.metalworks.machine.duster.RecipeDuster;
import de.canitzp.metalworks.machine.geothermalgenerator.geoburnable.IGeoburnable;
import de.canitzp.metalworks.recipe.OreDictStack;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        public void getIngredients(@Nonnull IIngredients ingredients) {
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
        public void getIngredients(@Nonnull IIngredients ingredients) {
            ingredients.setInput(ItemStack.class, this.recipe.getJEIIcon());
        }

        @Override
        public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            this.recipe.drawJEIText(mc.fontRenderer);
        }

    }

    public static class Crusher extends SimpleSteelJEIWrapper<RecipeCrusher>{

        private static final GuiEnergyBar energyBar = new GuiEnergyBar(1, 1, false);

        public Crusher(RecipeCrusher recipe) {
            super(recipe);
        }

        @Override
        public void getIngredients(@Nonnull IIngredients ingredients) {
            ingredients.setInputs(ItemStack.class, this.recipe.getInput().getListForJEI());
            ingredients.setOutputs(ItemStack.class, Arrays.asList(this.recipe.getOutputs()));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            energyBar.draw(this.recipe.getEnergy(), TileCrusher.ENERGY_CAPACITY, -1);
        }

        @Nonnull
        @Override
        public List<String> getTooltipStrings(int mouseX, int mouseY) {
            if(mouseX >= 0 && mouseX <= 20 && mouseY >= 0 && mouseY <= 64){
                return Lists.newArrayList(Util.formatEnergy(this.recipe.getEnergy() * this.recipe.getTime()), Util.formatEnergy(this.recipe.getEnergy()) + "/tick");
            }
            if(mouseX >= 24 && mouseX <= 24 + 30 && mouseY >= 20 && mouseY <= 20 + 25){
                List<String> list = new ArrayList<>();
                list.add("Time:     " + this.recipe.getTime());
                if(this.recipe.getSecondChance() > 0){
                    list.add("Chance: " + this.recipe.getSecondChance() + "%");
                }
                return list;
            }
            return Collections.emptyList();
        }
    }

    public static class BioGenerator extends SimpleSteelJEIWrapper<IGeneratorFuel>{

        private static final GuiEnergyBar energyBar = new GuiEnergyBar(1, 1, false);
        private static final GuiFluidBar fluidBar = new GuiFluidBar(41, 1);
        private final int energyPerTime;

        public BioGenerator(IGeneratorFuel recipe) {
            super(recipe);
            this.energyPerTime = this.recipe.getEnergyPerTick(this.recipe.getStack()) * this.recipe.getFuelTime(this.recipe.getStack());
        }

        @Override
        public void getIngredients(@Nonnull IIngredients ingredients) {
            ingredients.setInput(ItemStack.class, this.recipe.getStack().copy());
            ingredients.setOutput(ItemStack.class, this.recipe.getWaste().copy());
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            energyBar.draw(energyPerTime ,TileBioGenerator.ENERGY_CAPACITY, -1);
            fluidBar.draw(new CustomFluidTank(this.recipe.getFluid(), TileBioGenerator.FLUID_CAPACITY));
        }

        @Nonnull
        @Override
        public List<String> getTooltipStrings(int mouseX, int mouseY) {
            if(mouseX >= 0 && mouseX <= 16 && mouseY >= 0 && mouseY <= 64){
                return Lists.newArrayList(Util.formatEnergy(energyPerTime), Util.formatEnergy(this.recipe.getEnergyPerTick(this.recipe.getStack())) + "/tick");
            }
            if(mouseX >= 40 && mouseX <= 56 && mouseY >= 0 && mouseY <= 64){
                return Lists.newArrayList(this.recipe.getFluid().getLocalizedName(), String.format("%smB", this.recipe.getFluid().amount));
            }
            return Collections.emptyList();
        }

    }
}
