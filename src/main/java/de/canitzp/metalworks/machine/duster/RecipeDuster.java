package de.canitzp.metalworks.machine.duster;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.machine.MachineRecipe;
import de.canitzp.metalworks.recipe.OreDictStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class RecipeDuster extends MachineRecipe{

    public static final int DEFAULT_ENERGY_USAGE = 75;

    private OreDictStack[] inputs;
    private ItemStack output;
    private int time, energy;

    public RecipeDuster(String name, OreDictStack input1, OreDictStack input2, ItemStack output, int time, int energy){
        this.setRegistryName(new ResourceLocation(Metalworks.MODID, "duster." + name));
        this.inputs = new OreDictStack[]{input1, input2};
        this.output = output;
        this.time = time;
        this.energy = energy > 0 ? energy : DEFAULT_ENERGY_USAGE;
    }

    public OreDictStack[] getInputs() {
        return inputs;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getTime() {
        return time;
    }

    public int getEnergy() {
        return energy;
    }

    private boolean canMergeWithAnyInput(ItemStack stack, List<Integer> exceptions){
        if(exceptions.stream().anyMatch(integer -> integer < 0)){
            return false;
        }
        OreDictStack[] inputs1 = this.inputs;
        for (int i = 0; i < inputs1.length; i++) {
            if(!exceptions.contains(i)){
                OreDictStack ore = inputs1[i];
                if (ore.isSubstractable(stack)) {
                    exceptions.add(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkInput(ItemStack[] inputs) {
        if(inputs.length == 2){
            ItemStack input1 = inputs[0];
            ItemStack input2 = inputs[1];
            List<Integer> exceptions = new ArrayList<>();
            if(canMergeWithAnyInput(input1, exceptions) && canMergeWithAnyInput(input2, exceptions)){
                return true;
            }
        }
        return false;
    }
}
