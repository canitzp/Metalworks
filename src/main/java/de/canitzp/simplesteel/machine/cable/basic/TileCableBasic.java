package de.canitzp.simplesteel.machine.cable.basic;

import de.canitzp.simplesteel.Util;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.EnergyStorage;
import org.apache.commons.lang3.ArrayUtils;

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

    private int receiveEnergy(EnumFacing from, int energy, boolean simulate){
        return 0;
    }

}
