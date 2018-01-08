package de.canitzp.metalworks.block.cable;

import com.google.common.collect.Lists;
import de.canitzp.metalworks.block.cable.basic.TileCableBasic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author canitzp
 */
public class Network {

    private World world;
    private List<BlockPos> cables = new ArrayList<>();
    private Map<BlockPos, EnumFacing> receiver = new ConcurrentHashMap<>();

    public Network(World world){
        this.world = world;
    }

    public void integrate(Network other){
        for(BlockPos cable : other.cables){
            TileEntity tile = this.world.getTileEntity(cable);
            if(tile instanceof TileCableBasic){
                ((TileCableBasic) tile).network = this;
            }
        }
        cables.addAll(other.cables);
        receiver.putAll(other.receiver);
        other.cables.clear();
        other.receiver.clear();
    }

    public void searchFor(BlockPos from){
        this.cables.clear();
        this.receiver.clear();
        this.cables.add(from);
        this.recursive(from, Lists.newArrayList(from), this.cables, this.receiver);
    }

    private void recursive(BlockPos from, List<BlockPos> searched, List<BlockPos> found, Map<BlockPos, EnumFacing> receivers){
        for(EnumFacing side : EnumFacing.values()){
            BlockPos newPos = from.offset(side);
            if(!searched.contains(newPos)){
                searched.add(newPos);
                TileEntity tile = this.world.getTileEntity(newPos);
                if(tile != null){
                    if(tile instanceof TileCableBasic){
                        found.add(newPos);
                        ((TileCableBasic) tile).network = this;
                        recursive(newPos, searched, found, receivers);
                    } else if(tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())){
                        IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
                        if(storage != null && storage.canReceive()){
                            receivers.put(newPos, side.getOpposite());
                        }
                    }
                }
            }
        }
    }

    public int transmitEnergy(BlockPos from, int energy, boolean simulate){
        int oldEnergy = energy;
        for(Map.Entry<BlockPos, EnumFacing> entry : this.receiver.entrySet()){
            TileEntity tile = this.world.getTileEntity(entry.getKey());
            if(tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, entry.getValue())){
                IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, entry.getValue());
                if(storage != null && storage.canReceive()){
                    energy -= storage.receiveEnergy(energy, simulate);
                }
            }
            if(energy <= 0){
                return oldEnergy;
            }
        }
        return oldEnergy - energy;
    }
}
