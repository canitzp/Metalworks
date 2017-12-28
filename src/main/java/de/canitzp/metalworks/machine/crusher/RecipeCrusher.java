package de.canitzp.metalworks.machine.crusher;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.config.ConfMachines;
import de.canitzp.metalworks.machine.MachineRecipe;
import de.canitzp.metalworks.recipe.OreDictStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class RecipeCrusher extends MachineRecipe {

    public static final int DEFAULT_ENERGY = ConfMachines.CR_DEFAULT_ENERGY;
    public static final int DEFAULT_TIME = ConfMachines.CR_DEFAULT_BURN_TIME;

    private OreDictStack input = OreDictStack.EMPTY;
    private ItemStack[] outputs;
    private int sencondChance, time, energy;

    public RecipeCrusher(String name, OreDictStack input, ItemStack output, ItemStack secondOutput, int secondChance, int time, int energy){
        this.setRegistryName(new ResourceLocation(Metalworks.MODID, "crusher." + name));
        this.input = input;
        this.outputs = new ItemStack[]{output, secondOutput};
        this.sencondChance = secondChance;
        this.energy = energy > 0 ? energy : DEFAULT_ENERGY;
        this.time = time > 0 ? time : DEFAULT_TIME;
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

    public boolean isOutputMergeable(ItemStack output1, ItemStack output2){
        return (output1.isEmpty() || (output1.isItemEqual(this.outputs[0].copy()) && output1.copy().getCount() + this.outputs[0].copy().getCount() <= output1.getMaxStackSize())) &&
                output2.isEmpty() || (output2.isItemEqual(this.outputs[1].copy()) && output2.copy().getCount() + this.outputs[1].copy().getCount() <= output2.getMaxStackSize());
    }
}
