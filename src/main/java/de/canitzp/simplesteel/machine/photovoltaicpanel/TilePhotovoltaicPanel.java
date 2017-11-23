package de.canitzp.simplesteel.machine.photovoltaicpanel;

import de.canitzp.simplesteel.CustomEnergyStorage;
import de.canitzp.simplesteel.Util;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TilePhotovoltaicPanel extends TileEntity implements ITickable{

    private boolean cachedCanProduce = false;

    // The real produce of a photovoltaic panel with the size of 1m² would be 140kW per year -> 393.2W per Day -> 16.38W per hour -> 273mW per minute -> 4.5mW per second -> 0.227mW per tick
    // but since a day in minecraft is only 12 minutes long, I recalculate this with the minecraft values:
    // 393.2W/day (day=12; in reality a day has 1440 minutes) -> 32.76W/minute -> 546.1mW/second -> 27.3mW/tick
    // I now talked to some people and we decided to ste the conversion rate to 1W equals 300RF(293.04), after my calculations
    private CustomEnergyStorage energy = new CustomEnergyStorage(8){
        @Override
        public boolean canReceive() {
            return false;
        }
    };

    @Override
    public void update() {
        if(!world.isRemote){
            if(world.getTotalWorldTime() % 4 == 0){
                this.cachedCanProduce = Util.isDayTime(this.world) && Util.canBlockSeeSky(this.world, this.pos);
            }
            if(this.cachedCanProduce){
                energy.forceReceive(8, false);
            }
            if(this.energy.getEnergyStored() > 0){
                Util.pushEnergy(this.world, this.pos, this.energy, EnumFacing.UP);
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(this.energy) : null;
    }
}
