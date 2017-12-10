package de.canitzp.simplesteel.machine.duster;

import de.canitzp.simplesteel.CustomEnergyStorage;
import de.canitzp.simplesteel.Util;
import de.canitzp.simplesteel.inventory.SidedBasicInv;
import de.canitzp.simplesteel.machine.MachineRecipe;
import de.canitzp.simplesteel.machine.TileBase;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TileDuster extends TileBase implements ITickable{

    public static final int INPUT1 = 0;
    public static final int INPUT2 = 1;
    public static final int OUTPUT = 2;

    public CustomEnergyStorage energy = new CustomEnergyStorage(10000, 1500).setTile(this);
    public SidedBasicInv inventory = new SidedBasicInv("duster", 3) {
        @Override
        public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
            return index != OUTPUT && direction != EnumFacing.DOWN;
        }

        @Override
        public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
            return index == OUTPUT && direction != EnumFacing.UP;
        }
    }.setTile(this);
    public int energyUsage, maxBurn, burn;
    private ResourceLocation recipeId;

    @Nullable
    @Override
    protected IItemHandler getInventory(@Nullable EnumFacing side) {
        return this.inventory.getSidedWrapper(side);
    }

    @Nullable
    @Override
    protected IEnergyStorage getEnergy(@Nullable EnumFacing side) {
        return this.energy;
    }

    @Override
    public void readNBT(NBTTagCompound nbt, NBTType type) {
        super.readNBT(nbt, type);
        this.maxBurn = nbt.getInteger("MaxBurn");
        this.burn = nbt.getInteger("BurnLeft");
        this.energyUsage = nbt.getInteger("EnergyUsagePerTick");
        if(type != NBTType.SYNC && nbt.hasKey("Recipe", Constants.NBT.TAG_STRING)){
            this.recipeId = new ResourceLocation(nbt.getString("Recipe"));
        }
    }

    @Override
    public void writeNBT(NBTTagCompound nbt, NBTType type) {
        super.writeNBT(nbt, type);
        nbt.setInteger("MaxBurn", this.maxBurn);
        nbt.setInteger("BurnLeft", this.burn);
        nbt.setInteger("EnergyUsagePerTick", this.energyUsage);
        if(type != NBTType.SYNC){
            if(this.recipeId != null) {
                nbt.setString("Recipe", this.recipeId.toString());
            }
        }
    }

    @Override
    public void update() {
        if(!world.isRemote){
            this.updateForSyncing();
            if(this.recipeId != null){
                if(burn <= 0){
                    RecipeDuster recipe = MachineRecipe.getRecipe(this.recipeId);
                    if(recipe != null){
                        ItemStack out = this.inventory.getStackInSlot(OUTPUT);
                        if(out.isEmpty()){
                            this.inventory.setInventorySlotContents(OUTPUT, recipe.getOutput().copy());
                        } else if (ItemHandlerHelper.canItemStacksStack(out, recipe.getOutput()) && out.getCount() + recipe.getOutput().getCount() <= out.getMaxStackSize()){
                            out.grow(recipe.getOutput().getCount());
                        }
                    }  else {
                        System.out.println("A 'null' recipe was processed! This is maybe caused by a removed mod, while the burn progress.");
                    }
                    this.burn = this.maxBurn = this.energyUsage = 0;
                    this.recipeId = null;
                    this.syncToClients();
                } else {
                    if(this.energy.extractEnergy(this.energyUsage, true) == this.energyUsage){
                        this.energy.extractEnergy(this.energyUsage, false);
                        this.burn--;
                    } else if(this.burn < this.maxBurn) {
                        this.burn++;
                    }
                    this.syncToClients();
                }
            } else {
                ItemStack input1 = this.inventory.getStackInSlot(INPUT1);
                ItemStack input2 = this.inventory.getStackInSlot(INPUT2);
                if(!input1.isEmpty() || !input2.isEmpty()){
                    RecipeDuster recipe = MachineRecipe.getRecipe(RecipeDuster.class, input1, input2);
                    if(recipe != null && (this.inventory.getStackInSlot(OUTPUT).isEmpty() || Util.canItemStacksStack(this.inventory.getStackInSlot(OUTPUT), recipe.getOutput()))){
                        input1.shrink(recipe.getInputs()[0].getStacksize());
                        input2.shrink(recipe.getInputs()[1].getStacksize());
                        this.recipeId = recipe.getRegistryName();
                        this.burn = this.maxBurn = recipe.getTime();
                        this.energyUsage = recipe.getEnergy();
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
}
