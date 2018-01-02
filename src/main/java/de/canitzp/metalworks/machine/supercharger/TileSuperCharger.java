package de.canitzp.metalworks.machine.supercharger;

import de.canitzp.metalworks.CustomEnergyStorage;
import de.canitzp.metalworks.inventory.SidedBasicInv;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TileSuperCharger extends TileBase implements ITickable{

    public static int ENERGY_CAPACITY = 100000;
    public static int ENERGY_RECEIVE = 15000;
    public static int ENERGY_EXTRACT = ENERGY_RECEIVE;

    private CustomEnergyStorage energy = new CustomEnergyStorage(ENERGY_CAPACITY, ENERGY_RECEIVE, ENERGY_EXTRACT).setTile(this);
    private SidedBasicInv inv = new SidedBasicInv("super_charger", 1) {
        @Override
        public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
            return direction != EnumFacing.DOWN;
        }

        @Override
        public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
            return direction != EnumFacing.UP && getChargingState() == 10;
        }

        @Override
        public int getInventoryStackLimit() {
            return 1;
        }
    }.setTile(this);
    private boolean isWorking;

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
    public void writeNBT(NBTTagCompound nbt, NBTType type) {
        super.writeNBT(nbt, type);
        nbt.setBoolean("Working", this.isWorking);
    }

    @Override
    public void readNBT(NBTTagCompound nbt, NBTType type) {
        super.readNBT(nbt, type);
        this.isWorking = nbt.getBoolean("Working");
    }

    @Override
    public void update() {
        if(!world.isRemote){
            super.updateForSyncing();
            if(this.energy.getEnergyStored() > 0){
                IEnergyStorage itemEnergy = this.getItemEnergy();
                if(itemEnergy != null && itemEnergy.getEnergyStored() < itemEnergy.getMaxEnergyStored()){
                    this.isWorking = true;
                    this.energy.extractEnergy(itemEnergy.receiveEnergy(this.energy.extractEnergy(this.energy.getExtractTransfer(), true), false), false);
                } else {
                    this.isWorking = false;
                    this.syncToClients();
                }
            } else {
                this.isWorking = false;
                this.syncToClients();
            }
        }
    }

    @Override
    public void onSyncPacket() {
        this.markForRenderUpdate();
    }

    private IEnergyStorage getItemEnergy(){
        ItemStack stack = this.inv.getStackInSlot(0);
        if(!stack.isEmpty() && stack.hasCapability(CapabilityEnergy.ENERGY, null)){
            return stack.getCapability(CapabilityEnergy.ENERGY, null);
        }
        return null;
    }

    public int getChargingState(){
        IEnergyStorage storage = getItemEnergy();
        if(storage != null && storage.getMaxEnergyStored() > 0){
            return (int) (storage.getEnergyStored() / (storage.getMaxEnergyStored() * 1.0F) * 10.0F);
        }
        return 0;
    }

    @Override
    public boolean isWorking() {
        return this.isWorking;
    }
}
