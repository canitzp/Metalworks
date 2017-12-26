package de.canitzp.metalworks.machine.crusher;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.machine.MachineRecipe;
import de.canitzp.metalworks.recipe.OreDictStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class RecipeCrusher extends MachineRecipe {

    public static final int DEFAULT_ENERGY = 65;

    private OreDictStack input = OreDictStack.EMPTY;
    private ItemStack[] outputs;
    private int sencondChance, exp, time, energy;

    public RecipeCrusher(String name, OreDictStack input, ItemStack output, ItemStack secondOutput, int secondChance, int exp, int time, int energy){
        this.setRegistryName(new ResourceLocation(Metalworks.MODID, "crusher." + name));
        this.input = input;
        this.outputs = new ItemStack[]{output, secondOutput};
        this.sencondChance = secondChance;
        this.energy = energy > 0 ? energy : DEFAULT_ENERGY;
        this.exp = exp;
        this.time = time;
    }

    public OreDictStack getInput() {
        return input;
    }

    public ItemStack[] getOutputs() {
        return outputs;
    }

    public int getSencondChance() {
        return sencondChance;
    }

    public int getExp() {
        return exp;
    }

    public int getEnergy() {
        return energy;
    }

    public int getTime() {
        return time;
    }

    @Override
    public boolean checkInput(ItemStack[] inputs) {
        return inputs.length == 1 && this.input.isSubstractable(inputs[0]);
    }
}
