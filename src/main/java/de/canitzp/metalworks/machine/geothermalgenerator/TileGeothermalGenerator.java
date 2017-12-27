package de.canitzp.metalworks.machine.geothermalgenerator;

import de.canitzp.metalworks.CustomEnergyStorage;
import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.machine.TileBase;
import de.canitzp.metalworks.machine.geothermalgenerator.geoburnable.IGeoburnable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TileGeothermalGenerator extends TileBase implements ITickable{

    public CustomEnergyStorage energy = new CustomEnergyStorage(5000){
        @Override
        public boolean canReceive() {
            return false;
        }
    }.setTile(this);
    public int burn = 0, energyPerTick = 0, cooldown = 0;

    @Override
    public void update() {
        if(!this.world.isRemote){
            this.updateForSyncing();
            if(this.burn <= 0){
                if(this.cooldown <= 0){
                    this.energyPerTick = 0;
                    for(EnumFacing side : EnumFacing.values()){
                        if(side != EnumFacing.UP){
                            for(IGeoburnable burnable : Metalworks.GEOBURNABLE_REGISTRY){
                                BlockPos pos = this.pos.offset(side);
                                IBlockState state = this.world.getBlockState(pos);
                                if(burnable.isBurnable(state, side)){
                                    this.burn = burnable.getBurnTime(this.world, pos, state, side);
                                    if(this.burn > 0){
                                        this.energyPerTick = burnable.getBurnEnergy(this.world, pos, state, side);
                                        this.cooldown = burnable.getCooldown(this.world, pos, state, side);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    this.cooldown--;
                }
            } else {
                this.burn--;
                this.energy.forceReceive(this.energyPerTick, false);
            }
            if(this.energy.getEnergyStored() > 0){
                Util.pushEnergy(this.world, this.pos, this.energy);
            }
            this.syncToClients();
        }
    }

    @Nullable
    @Override
    public IEnergyStorage getEnergy(@Nullable EnumFacing side) {
        return this.energy;
    }

    @Override
    public void writeNBT(NBTTagCompound nbt, NBTType type) {
        super.writeNBT(nbt, type);
        nbt.setInteger("Burn", this.burn);
        nbt.setInteger("EnergyGen", this.energyPerTick);
        nbt.setInteger("Cooldown", this.cooldown);
    }

    @Override
    public void readNBT(NBTTagCompound nbt, NBTType type) {
        super.readNBT(nbt, type);
        this.burn = nbt.getInteger("Burn");
        this.energyPerTick = nbt.getInteger("EnergyGen");
        this.cooldown = nbt.getInteger("Cooldown");
    }

    @Override
    public void onSyncPacket() {
        this.markForRenderUpdate();
    }

    @Override
    public boolean isWorking() {
        return this.burn > 0 && this.energy.getEnergyStored() < this.energy.getMaxEnergyStored();
    }
}
