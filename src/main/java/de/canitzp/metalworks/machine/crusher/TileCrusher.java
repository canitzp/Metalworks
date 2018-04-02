package de.canitzp.metalworks.machine.crusher;

import de.canitzp.metalworks.CustomEnergyStorage;
import de.canitzp.metalworks.inventory.SidedBasicInv;
import de.canitzp.metalworks.machine.MachineRecipe;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author canitzp
 */
public class TileCrusher extends TileBase implements ITickable{

    public static final int ENERGY_CAPACITY = 10000;
    public static final int ENERGY_RECEIVE = 1500;
    public static final int ENERGY_EXTRACT = ENERGY_RECEIVE;

    public final CustomEnergyStorage energy = new CustomEnergyStorage(ENERGY_CAPACITY, ENERGY_RECEIVE, ENERGY_EXTRACT).setTile(this);
    public final SidedBasicInv inv = new SidedBasicInv("crusher", 3) {
        @Override
        public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
            return direction != EnumFacing.DOWN && index == 0;
        }

        @Override
        public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
            return direction != EnumFacing.UP && index != 0;
        }
    }.setTile(this);
    private ResourceLocation recipeId;
    public int burn, maxBurn, energyUsage;

    @Override
    protected Triple<Boolean, Boolean, Boolean> hasEnergyFluidInv() {
        return Triple.of(true, false, true);
    }

    @Nullable
    @Override
    public IItemHandler getInventory(@Nullable EnumFacing side) {
        return this.inv.getSidedWrapper(side);
    }

    @Nullable
    @Override
    public IEnergyStorage getEnergy(@Nullable EnumFacing side) {
        return this.energy;
    }

    @SuppressWarnings("Duplicates")
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

    @SuppressWarnings("Duplicates")
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

    @SuppressWarnings("Duplicates")
    @Override
    public void update() {
        this.updateBase();
        if(!this.world.isRemote){
            if(this.recipeId != null){
                if(this.burn <= 0){
                    this.burn = this.maxBurn = this.energyUsage = 0;
                    RecipeCrusher recipe = MachineRecipe.getRecipe(this.recipeId);
                    if(recipe != null){
                        ItemStack out1 = this.inv.getStackInSlot(1);
                        ItemStack out2 = this.inv.getStackInSlot(2);
                        if(recipe.isOutputMergeable(out1, out2)){
                            if(!recipe.getOutputs()[0].isEmpty()){
                                if(out1.isEmpty()){
                                    this.inv.setInventorySlotContents(1, recipe.getOutputs()[0].copy());
                                } else {
                                    out1.grow(recipe.getOutputs()[0].getCount());
                                }
                            }
                            if(recipe.getSecondChance() > 0 && new Random().nextInt(100/recipe.getSecondChance()) - 1 == 0 && !recipe.getOutputs()[1].isEmpty()){
                                if(out2.isEmpty()){
                                    this.inv.setInventorySlotContents(2, recipe.getOutputs()[1].copy());
                                } else {
                                    out2.grow(recipe.getOutputs()[1].getCount());
                                }
                            }
                        }
                    } else {
                        System.out.println("A 'null' recipe was processed! This is maybe caused by a removed mod, while the burn progress.");
                    }
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
                ItemStack input = this.inv.getStackInSlot(0);
                if(!input.isEmpty()){
                    RecipeCrusher recipe = MachineRecipe.getRecipe(RecipeCrusher.class, input);
                    if(recipe != null && this.inv.canMergeStacks(1, recipe.getOutputs()[0]) && this.inv.canMergeStacks(2, recipe.getOutputs()[1])){
                        input.shrink(recipe.getInput().getStacksize());
                        this.energyUsage = recipe.getEnergy();
                        this.recipeId = recipe.getRegistryName();
                        this.burn = this.maxBurn = recipe.getTime();
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
        return this.burn > 0 && this.energy.getEnergyStored() >= this.energyUsage;
    }

    @Override
    public int getCurrentEnergyUsage() {
        return this.energyUsage;
    }

}
