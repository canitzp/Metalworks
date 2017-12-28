package de.canitzp.metalworks.machine.photovoltaicpanel;

import de.canitzp.metalworks.CustomEnergyStorage;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.config.ConfMachines;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TilePhotovoltaicPanel extends TileBase implements ITickable{

    public static final int ENERGY_PRODUCTION = ConfMachines.PP_ENERGY_PRODUCTION;

    private boolean cachedCanProduce = false;

    // The real produce of a photovoltaic panel with the size of 1m² would be 140kW per year -> 393.2W per Day -> 16.38W per hour -> 273mW per minute -> 4.5mW per second -> 0.227mW per tick
    // but since a day in minecraft is only 12 minutes long, I recalculate this with the minecraft values:
    // 393.2W/day (day=12; in reality a day has 1440 minutes) -> 32.76W/minute -> 546.1mW/second -> 27.3mW/tick
    // I now talked to some people and we decided to ste the conversion rate to 1W equals 300RF(293.04), after my calculations
    private CustomEnergyStorage energy = new CustomEnergyStorage(ENERGY_PRODUCTION){
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
                energy.forceReceive(ENERGY_PRODUCTION, false);
            }
            if(this.energy.getEnergyStored() > 0){
                Util.pushEnergy(this.world, this.pos, this.energy, EnumFacing.UP);
            }
        }
    }

    @Nullable
    @Override
    public IEnergyStorage getEnergy(@Nullable EnumFacing side) {
        return this.energy;
    }
}
