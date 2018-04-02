package de.canitzp.metalworks.machine.blastfurnace;

import de.canitzp.metalworks.config.ConfMachines;
import de.canitzp.metalworks.recipe.OreDictStack;
import net.minecraft.item.ItemStack;

/**
 * @author canitzp
 */
public class RecipeBlastFurnace {

    public static final int DEFAULT_ENERGY_USAGE = ConfMachines.BF_DEFAULT_ENERGY;
    public static final int DEFAULT_BURN_TIME = ConfMachines.BF_DEFAULT_BURN_TIME;

    private final String id;

    private final OreDictStack[] inputs;
    private final ItemStack[] outputs;
    private final int secondOutputChance;
    private final int burnTime;
    private final int energyUsage;

    public RecipeBlastFurnace(OreDictStack input1, OreDictStack input2, OreDictStack input3, ItemStack output1, ItemStack output2, int out2Chance, int burnTime, int energyUsagePerTick){
        this.inputs = new OreDictStack[]{input1, input2, input3};
        this.outputs = new ItemStack[]{output1, output2};
        this.secondOutputChance = out2Chance;
        this.burnTime = burnTime > 0 ? burnTime : DEFAULT_BURN_TIME;
        this.energyUsage = energyUsagePerTick > 0 ? energyUsagePerTick : DEFAULT_ENERGY_USAGE;
        this.id = String.format("%s#%s#%s#%s#%s#%d#%d", input1.getName(), input2.getName(), input3.getName(), output1.getUnlocalizedName(), output2.getUnlocalizedName(), out2Chance, burnTime);
    }

    public OreDictStack[] getInputs() {
        return inputs.clone();
    }

    public ItemStack[] getOutputs() {
        return outputs.clone();
    }

    public int getSecondOutputChance() {
        return secondOutputChance;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getEnergyUsage() {
        return energyUsage;
    }

    public boolean isRecipe(ItemStack input1, ItemStack input2, ItemStack input3){
        if(this.inputs[0].isSubstractable(input1)){
            if(this.inputs[1].isSubstractable(input2)){
                return this.inputs[2].isSubstractable(input3);
            } else if(this.inputs[1].isSubstractable(input3)){
                return this.inputs[2].isSubstractable(input2);
            }
        } else if(this.inputs[0].isSubstractable(input2)){
            if(this.inputs[1].isSubstractable(input1)){
                return this.inputs[2].isSubstractable(input3);
            } else if(this.inputs[1].isSubstractable(input3)){
                return this.inputs[2].isSubstractable(input1);
            }
        } else if(this.inputs[0].isSubstractable(input3)){
            if(this.inputs[1].isSubstractable(input1)){
                return this.inputs[2].isSubstractable(input2);
            } else if(this.inputs[1].isSubstractable(input2)){
                return this.inputs[2].isSubstractable(input1);
            }
        }
        return false;
    }

    public boolean isOutputMergeable(ItemStack output1, ItemStack output2){
        return (output1.isEmpty() || (output1.isItemEqual(this.outputs[0].copy()) && output1.copy().getCount() + this.outputs[0].copy().getCount() <= output1.getMaxStackSize())) &&
                output2.isEmpty() || (output2.isItemEqual(this.outputs[1].copy()) && output2.copy().getCount() + this.outputs[1].copy().getCount() <= output2.getMaxStackSize());
    }

    public String getID(){
        return this.id;
    }

    public void shrink(ItemStack stack){
        for(OreDictStack oreDictStack : this.inputs){
            if(oreDictStack.isSubstractable(stack)){
                stack.shrink(oreDictStack.getStacksize());
                return;
            }
        }
    }
}
