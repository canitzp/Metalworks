package de.canitzp.metalworks.item;

import de.canitzp.metalworks.ItemEnergyStorage;
import de.canitzp.metalworks.Util;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author canitzp
 */
public class ItemBlockEnergetic extends ItemBlock{

    private int maxEnergy, maxTransmit, maxReceive;

    public ItemBlockEnergetic(Block block, int maxEnergy, int maxTransmit, int maxReceive) {
        super(block);
        this.setRegistryName(block.getRegistryName());
        this.maxEnergy = maxEnergy;
        this.maxTransmit = maxTransmit;
        this.maxReceive = maxReceive;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(stack.hasCapability(CapabilityEnergy.ENERGY, null)){
            IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
            if(energyStorage != null){
                tooltip.add(Util.formatEnergy(energyStorage.getEnergyStored()) + " / " + Util.formatEnergy(energyStorage.getMaxEnergyStored()));
            }
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        if(!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }
        return new CapProviderItem(Pair.of(CapabilityEnergy.ENERGY, new ItemEnergyStorage(stack, this.maxEnergy, this.maxReceive, this.maxTransmit)));
    }

}
