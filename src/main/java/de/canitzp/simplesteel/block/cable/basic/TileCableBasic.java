package de.canitzp.simplesteel.block.cable.basic;

import de.canitzp.simplesteel.block.cable.Network;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TileCableBasic extends TileEntity{

    public Network network;

    public EnergyStorage energy = new EnergyStorage(1000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return network != null ?  network.transmitEnergy(pos, maxReceive, simulate) : 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public boolean canExtract() {
            return false;
        }
    };

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(this.energy) : null;
    }

    @Override
    public void onLoad() {
        if(!world.isRemote){
            Network cached = null;
            for(EnumFacing side : EnumFacing.values()){
                TileEntity tile = this.world.getTileEntity(this.pos.offset(side));
                if(tile instanceof TileCableBasic){
                    if(((TileCableBasic) tile).network != null && cached != null && ((TileCableBasic) tile).network != cached){
                        cached.integrate(((TileCableBasic) tile).network);
                    } else {
                        cached = ((TileCableBasic) tile).network;
                    }
                }
            }
            this.network = cached != null ? cached : new Network(this.world);
            this.network.searchFor(this.pos);
        }
    }

}
