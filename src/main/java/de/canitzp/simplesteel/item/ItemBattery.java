package de.canitzp.simplesteel.item;

import de.canitzp.simplesteel.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author canitzp
 */
public class ItemBattery extends ItemBase {

    private int maxEnergy;

    public ItemBattery(String name, int maxEnergy) {
        super(name);
        this.maxEnergy = maxEnergy;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if(energy != null){
            tooltip.add(Util.formatEnergy(energy));
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ICapabilityProvider() {
            @Override
            public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                return capability == CapabilityEnergy.ENERGY;
            }

            @Nullable
            @Override
            public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                int maxTransfer = Math.round(maxEnergy / 50.0F);

                return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(new EnergyStorage(maxEnergy, maxTransfer, maxTransfer, stack.getTagCompound() != null ? stack.getTagCompound().getInteger("Energy") : 0){
                    @Override
                    public int receiveEnergy(int maxReceive, boolean simulate) {
                        int i = super.receiveEnergy(maxReceive, simulate);
                        setEnergy(energy);
                        return i;
                    }

                    @Override
                    public int extractEnergy(int maxExtract, boolean simulate) {
                        int i = super.extractEnergy(maxExtract, simulate);
                        setEnergy(energy);
                        return i;
                    }

                    private void setEnergy(int energy){
                        NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
                        nbt.setInteger("Energy", energy);
                        stack.setTagCompound(nbt);
                    }
                }) : null;
            }
        };
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, facing)){
            if(!world.isRemote){
                IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, facing);
                if(energy != null && energy.canExtract()){
                    IEnergyStorage storage = player.getHeldItem(hand).getCapability(CapabilityEnergy.ENERGY, null);
                    if(storage != null && storage.canReceive()){
                        storage.receiveEnergy(energy.extractEnergy(storage.receiveEnergy(Integer.MAX_VALUE, true), false), false);
                    }
                    System.out.println(storage.getEnergyStored());
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

}
