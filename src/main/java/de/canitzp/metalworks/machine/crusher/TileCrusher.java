package de.canitzp.metalworks.machine.crusher;

import de.canitzp.metalworks.CustomEnergyStorage;
import de.canitzp.metalworks.inventory.SidedBasicInv;
import de.canitzp.metalworks.machine.MachineRecipe;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TileCrusher extends TileBase implements ITickable{

    public CustomEnergyStorage energy = new CustomEnergyStorage(10000, 1500);
    public SidedBasicInv inv = new SidedBasicInv("crusher", 3) {
        @Override
        public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
            return direction != EnumFacing.DOWN;
        }

        @Override
        public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
            return direction != EnumFacing.UP;
        }
    }.setTile(this);
    private ResourceLocation recipeId;

    @Nullable
    @Override
    protected IItemHandler getInventory(@Nullable EnumFacing side) {
        return this.inv.getSidedWrapper(side);
    }

    @Nullable
    @Override
    protected IEnergyStorage getEnergy(@Nullable EnumFacing side) {
        return this.energy;
    }

    @Override
    public void update() {
        if(!this.world.isRemote){
            this.updateForSyncing();
            if(this.recipeId != null){

            } else {
                ItemStack input = this.inv.getStackInSlot(0);
                if(!input.isEmpty()){
                    RecipeCrusher recipe = MachineRecipe.getRecipe(RecipeCrusher.class, input);
                    if(recipe != null && this.inv.canMergeStacks(1, recipe.getOutputs()[0]) && this.inv.canMergeStacks(2, recipe.getOutputs()[1])){
                        input.shrink(recipe.getInput().getStacksize());
                    }
                }
            }
        }
    }

}
