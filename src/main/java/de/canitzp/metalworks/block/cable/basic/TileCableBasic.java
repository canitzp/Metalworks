package de.canitzp.metalworks.block.cable.basic;

import de.canitzp.metalworks.block.cable.Network;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TileCableBasic extends TileBase{

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

    @Nullable
    @Override
    public IEnergyStorage getEnergy(@Nullable EnumFacing side) {
        return this.energy;
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
