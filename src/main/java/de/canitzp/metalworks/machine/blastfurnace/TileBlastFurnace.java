package de.canitzp.metalworks.machine.blastfurnace;

import de.canitzp.metalworks.CustomEnergyStorage;
import de.canitzp.metalworks.inventory.SidedBasicInv;
import de.canitzp.metalworks.machine.TileBase;
import de.canitzp.metalworks.recipe.SimpleSteelRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author canitzp
 */
public class TileBlastFurnace extends TileBase implements ITickable{

    public static final int INPUT1 = 0;
    public static final int INPUT2 = 1;
    public static final int INPUT3 = 2;
    public static final int OUTPUT1 = 3;
    public static final int OUTPUT2 = 4;

    public CustomEnergyStorage energy = new CustomEnergyStorage(10000, 1500).setTile(this);
    public SidedBasicInv inventory = new SidedBasicInv("blast_furnace", 6) {
        @Override
        public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
            return side != EnumFacing.DOWN && index != OUTPUT1 && index != OUTPUT2;
        }
        @Override
        public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
            return (index == OUTPUT1 || index == OUTPUT2) && side != EnumFacing.UP;
        }
    }.setTile(this);
    private String recipeID = null;
    public int maxBurn, burnLeft, energyUsage;

    @Nullable
    @Override
    public IItemHandler getInventory(@Nullable EnumFacing side) {
        return this.inventory.getSidedWrapper(side);
    }

    @Nullable
    @Override
    public IEnergyStorage getEnergy(@Nullable EnumFacing side) {
        return this.energy;
    }

    @Override
    public void readNBT(NBTTagCompound compound, NBTType type) {
        super.readNBT(compound, type);
        this.maxBurn = compound.getInteger("MaxBurn");
        this.burnLeft = compound.getInteger("BurnLeft");
        this.energyUsage = compound.getInteger("EnergyUsagePerTick");
        if(type != NBTType.SYNC && compound.hasKey("RecipeUUID", Constants.NBT.TAG_STRING)){
            this.recipeID = compound.getString("RecipeUUID");
        }
    }

    @Override
    public void writeNBT(NBTTagCompound compound, NBTType type) {
        super.writeNBT(compound, type);
        compound.setInteger("MaxBurn", this.maxBurn);
        compound.setInteger("BurnLeft", this.burnLeft);
        compound.setInteger("EnergyUsagePerTick", this.energyUsage);
        if(type != NBTType.SYNC){
            if(this.recipeID != null) {
                compound.setString("RecipeUUID", this.recipeID);
            }
        }
    }

    @Override
    public void update() {
        if(!world.isRemote){
            this.updateForSyncing();
            if(this.recipeID != null){
                if(this.burnLeft <= 0){
                    this.burnLeft = this.maxBurn = this.energyUsage = 0;
                    RecipeBlastFurnace recipe = SimpleSteelRecipeHandler.getBlastFurnaceRecipeForId(this.recipeID);
                    if(recipe != null){
                        ItemStack out1 = this.inventory.getStackInSlot(OUTPUT1);
                        ItemStack out2 = this.inventory.getStackInSlot(OUTPUT2);
                        if(recipe.isOutputMergeable(out1, out2)){
                            if(!recipe.getOutputs()[0].isEmpty()){
                                if(out1.isEmpty()){
                                    this.inventory.setInventorySlotContents(OUTPUT1, recipe.getOutputs()[0].copy());
                                } else {
                                    out1.grow(recipe.getOutputs()[0].getCount());
                                }
                            }
                            if(recipe.getSecondOutputChance() > 0 && new Random().nextInt(100/recipe.getSecondOutputChance()) - 1 == 0 && !recipe.getOutputs()[1].isEmpty()){
                                if(out2.isEmpty()){
                                    this.inventory.setInventorySlotContents(OUTPUT2, recipe.getOutputs()[1].copy());
                                } else {
                                    out2.grow(recipe.getOutputs()[1].getCount());
                                }
                            }
                        }
                    } else {
                        System.out.println("A 'null' recipe was processed! This is maybe caused by a removed mod, while the burn progress.");
                    }
                    this.recipeID = null;
                    this.syncToClients();
                } else {
                    if(this.energy.extractEnergy(this.energyUsage, true) == this.energyUsage){
                        this.energy.extractEnergy(this.energyUsage, false);
                        this.burnLeft--;
                    } else if(this.burnLeft < this.maxBurn) {
                        this.burnLeft++;
                    }
                    this.syncToClients();
                }
            } else {
                ItemStack input1 = this.inventory.getStackInSlot(INPUT1);
                ItemStack input2 = this.inventory.getStackInSlot(INPUT2);
                ItemStack input3 = this.inventory.getStackInSlot(INPUT3);
                if((!input1.isEmpty() || !input2.isEmpty() || !input3.isEmpty())){
                    RecipeBlastFurnace recipe = SimpleSteelRecipeHandler.getBlastFurnaceRecipe(input1, input2, input3);
                    if(recipe != null && recipe.isOutputMergeable(this.inventory.getStackInSlot(OUTPUT1), this.inventory.getStackInSlot(OUTPUT2)) && this.energy.extractEnergy(recipe.getEnergyUsage(), true) == recipe.getEnergyUsage()){
                        this.recipeID = SimpleSteelRecipeHandler.getIdForBlastFurnaceRecipe(recipe);
                        this.burnLeft = this.maxBurn = recipe.getBurnTime();
                        this.energyUsage = recipe.getEnergyUsage();
                        if(!input1.isEmpty()){
                            recipe.shrink(input1);
                        }
                        if(!input2.isEmpty()){
                            recipe.shrink(input2);
                        }
                        if(!input3.isEmpty()){
                            recipe.shrink(input3);
                        }
                        this.syncToClients();
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onSyncPacket() {
        this.markForRenderUpdate();
    }

    @Override
    public boolean isWorking() {
        return this.burnLeft > 0 && this.energy.getEnergyStored() >= this.energyUsage;
    }

    @Override
    public int getCurrentEnergyUsage() {
        return this.energyUsage;
    }
}
