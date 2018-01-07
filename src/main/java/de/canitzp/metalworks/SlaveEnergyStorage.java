package de.canitzp.metalworks;

/**
 * @author canitzp
 */
public class SlaveEnergyStorage extends CustomEnergyStorage {

    private CustomEnergyStorage master;
    private boolean canExtract = true, canReceive = true;

    public SlaveEnergyStorage(CustomEnergyStorage energyStorage) {
        super(energyStorage.getMaxEnergyStored(), energyStorage.getReceiveTransfer(), energyStorage.getExtractTransfer());
        this.master = energyStorage;
    }

    public SlaveEnergyStorage set(boolean canReceive, boolean canExtract){
        this.canReceive = canReceive;
        this.canExtract = canExtract;
        return this;
    }

    @Override
    public void setEnergy(int energy) {
        this.master.setEnergy(energy);
    }

    @Override
    public int getEnergyStored() {
        return this.master.getEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return this.canExtract;
    }

    @Override
    public boolean canReceive() {
        return this.canReceive;
    }

    @Override
    protected void notifyTile() {
        this.master.notifyTile();
    }

    @Override
    public int forceReceive(int maxReceive, boolean simulate) {
        return this.master.forceReceive(maxReceive, simulate);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return this.master.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return this.master.extractEnergy(maxExtract, simulate);
    }
}
