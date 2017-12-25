package de.canitzp.metalworks;

import de.canitzp.metalworks.machine.TileBase;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class CustomEnergyStorage extends EnergyStorage {

    @Nullable
    private TileBase tileToUpdate = null;

    public CustomEnergyStorage(int capacity) {
        super(capacity);
    }

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public int forceReceive(int maxReceive, boolean simulate){
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        this.notifyTile();
        return energyReceived;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = super.receiveEnergy(maxReceive, simulate);
        this.notifyTile();
        return energy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = super.extractEnergy(maxExtract, simulate);
        this.notifyTile();
        return energy;
    }

    public CustomEnergyStorage setTile(@Nullable TileBase tile){
        this.tileToUpdate = tile;
        return this;
    }

    private void notifyTile(){
        if(this.tileToUpdate != null){
            this.tileToUpdate.syncToClients();
        }
    }

}
