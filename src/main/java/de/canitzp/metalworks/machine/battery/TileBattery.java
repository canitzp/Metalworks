package de.canitzp.metalworks.machine.battery;

import de.canitzp.metalworks.CustomEnergyStorage;
import de.canitzp.metalworks.SlaveEnergyStorage;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TileBattery extends TileBase implements ITickable{

    private CustomEnergyStorage normal, input, output;

    @Nullable
    @Override
    public IEnergyStorage getEnergy(@Nullable EnumFacing side) {
        if(side == null){
            return this.normal;
        } else if(side == this.getInputSide()){
            return this.input;
        } else if(side == this.getOutputSide()){
            return this.output;
        }
        return null;
    }

    @Override
    public void onLoad() {
        Block block = world.getBlockState(this.getPos()).getBlock();
        if(block instanceof BlockBattery){
            BlockBattery.Type batType = ((BlockBattery) block).getBatteryType();
            this.normal = new CustomEnergyStorage(batType.maxEnergy, batType.maxTransfer).setTile(this);
            this.input = new SlaveEnergyStorage(this.normal).set(true, false);
            this.output = new SlaveEnergyStorage(this.normal).set(false, true);
        }
    }

    @Override
    public void update() {
        this.updateBase();
        if(!world.isRemote){
            if(this.output != null)
                Util.pushEnergy(this.world, this.pos, this.output, this.getOutputSide());
            if(this.world.getTotalWorldTime() % 100 == 0){
                this.syncToClients();
            }
        }
    }

    @Override
    public void onSyncPacket() {
        this.markForRenderUpdate();
    }

    public int getChargingState(){
        IEnergyStorage storage = this.normal;
        if(storage != null && storage.getMaxEnergyStored() > 0){
            return (int) (storage.getEnergyStored() / (storage.getMaxEnergyStored() * 1.0F) * 10.0F);
        }
        return 0;
    }

    public EnumFacing getInputSide(){
        return this.world != null ? this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING) : EnumFacing.NORTH;
    }

    public EnumFacing getOutputSide(){
        return this.getInputSide().getOpposite();
    }
}
