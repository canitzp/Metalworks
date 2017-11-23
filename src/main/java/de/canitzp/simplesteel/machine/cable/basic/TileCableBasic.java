package de.canitzp.simplesteel.machine.cable.basic;

import de.canitzp.simplesteel.Util;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author canitzp
 */
public class TileCableBasic extends TileEntity {

    public EnergyStorage[] sidedEnergy = new EnergyStorage[EnumFacing.values().length];

    public TileCableBasic(){
        for(EnumFacing side : EnumFacing.values()){
            sidedEnergy[side.ordinal()] = new EnergyStorage(5000){
                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    return TileCableBasic.this.receiveEnergy(side, maxReceive, simulate);
                }

                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {
                    return 0;
                }

                @Override
                public boolean canExtract() {
                    return false;
                }

                @Override
                public int getEnergyStored() {
                    return 0;
                }
            };
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(this.sidedEnergy[facing != null ? facing.ordinal() : 0]) : null;
    }

    private int receiveEnergy(EnumFacing from, int energy, boolean simulate){
        if(!world.isRemote){
            int originEnergy = energy;
            for(EnumFacing side : EnumFacing.values()){
                if(side != from){
                    TileEntity tile = this.world.getTileEntity(this.pos.offset(side));
                    if(tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())){
                        IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
                        if(storage != null && storage.canReceive()){
                            energy -= storage.receiveEnergy(energy, simulate);
                            if(energy <= 0){
                                break;
                            }
                        }
                    }
                }
            }
            return originEnergy - energy;
        }
        return 0;
    }

}
