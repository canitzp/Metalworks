package de.canitzp.metalworks.machine.biogenerator;

import de.canitzp.metalworks.CustomEnergyStorage;
import de.canitzp.metalworks.CustomFluidTank;
import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.inventory.SidedBasicInv;
import de.canitzp.metalworks.machine.IGeneratorFuel;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TileBioGenerator extends TileBase implements ITickable{

    public static final int ENERGY_CAPACITY = 10000;
    public static final int ENERGY_RECEIVE = 1500;
    public static final int ENERGY_EXTRACT = ENERGY_RECEIVE;
    public static final int FLUID_CAPACITY = 10000;

    public final CustomEnergyStorage energy = new CustomEnergyStorage(ENERGY_CAPACITY, ENERGY_RECEIVE, ENERGY_EXTRACT){
        @Override
        public boolean canReceive() {
            return false;
        }
    }.setTile(this);
    public final CustomFluidTank tank = new CustomFluidTank(FLUID_CAPACITY).setTile(this);
    public final SidedBasicInv inv = new SidedBasicInv("bio_generator", 2) {
        @Override
        public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
            return direction != EnumFacing.DOWN && index == 0;
        }

        @Override
        public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
            return direction != EnumFacing.UP && index != 0;
        }
    }.setTile(this);

    int burn, maxBurn;
    private int energyPerTick;
    private ItemStack waste = ItemStack.EMPTY;

    @Override
    public void update() {
        super.updateBase();
        if(!this.world.isRemote){
            if(this.isWorking() && energy.getEnergyStored() + this.energyPerTick <= energy.getMaxEnergyStored()){
                this.burn--;
                this.energy.forceReceive(this.energyPerTick, false);
                if(this.burn <= 0){
                    this.burn = this.maxBurn = this.energyPerTick = 0;
                    if(waste != null && !waste.isEmpty()){
                        if(this.inv.getStackInSlot(1).isEmpty()){
                            this.inv.setInventorySlotContents(1, this.waste);
                        } else if(Util.canItemStacksStack(this.inv.getStackInSlot(1), this.waste)){
                            this.inv.getStackInSlot(1).grow(this.waste.getCount());
                        } else {
                            this.world.spawnEntity(new EntityItem(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.waste));
                        }
                    }
                }
            } else {
                ItemStack stack = this.inv.getStackInSlot(0);
                FluidStack fluidStack = this.tank.getFluid();
                if(!stack.isEmpty() && fluidStack != null){
                    for(IGeneratorFuel fuel : Metalworks.GENERATOR_FUEL_REGISTRY){
                        if(fuel.workingIn(stack).contains(IGeneratorFuel.GeneratorTypes.BIO)
                                && Util.canItemStacksStackWithoutStacksizeMax(fuel.getStack(), stack)
                                && fluidStack.getFluid() == fuel.getFluid().getFluid()
                                && fluidStack.amount >= fuel.getFluid().amount
                                && energy.getEnergyStored() + (fuel.getEnergyPerTick(stack) * fuel.getFuelTime(stack)) <= energy.getMaxEnergyStored()
                                && (Util.canItemStacksStack(this.inv.getStackInSlot(1), fuel.getWaste().copy()) || this.inv.getStackInSlot(1).isEmpty())){
                            this.burn = this.maxBurn = fuel.getFuelTime(stack);
                            this.energyPerTick = fuel.getEnergyPerTick(stack);
                            stack.shrink(fuel.getStack().getCount());
                            this.tank.drain(fuel.getFluid(), true);
                            this.waste = fuel.getWaste().copy();
                        }
                    }
                }
            }
            if(this.energy.getEnergyStored() > 0){
                Util.pushEnergy(this.world, this.pos, this.energy);
            }
        }
    }

    @Nullable
    @Override
    public IFluidHandler getTank(@Nullable EnumFacing side) {
        return this.tank;
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

    @Override
    public boolean isWorking() {
        return this.burn > 0;
    }

    @Override
    public void writeNBT(NBTTagCompound nbt, NBTType type) {
        super.writeNBT(nbt, type);
        nbt.setInteger("Burn", this.burn);
        nbt.setInteger("MaxBurn", this.maxBurn);
        nbt.setInteger("EnergyUsage", this.energyPerTick);
        NBTTagCompound stackTag = new NBTTagCompound();
        this.waste.writeToNBT(stackTag);
        nbt.setTag("WasteStack", stackTag);
    }

    @Override
    public void readNBT(NBTTagCompound nbt, NBTType type) {
        super.readNBT(nbt, type);
        this.burn = nbt.getInteger("Burn");
        this.maxBurn = nbt.getInteger("MaxBurn");
        this.energyPerTick = nbt.getInteger("EnergyUsage");
        NBTTagCompound stackTag = nbt.getCompoundTag("WasteStack");
        this.waste = new ItemStack(stackTag);
    }

    @Override
    public int getCurrentEnergyUsage() {
        return -this.energyPerTick;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onSyncPacket() {
        //this.markForRenderUpdate();
    }
}
