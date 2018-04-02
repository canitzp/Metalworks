package de.canitzp.metalworks;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;

/**
 * @author canitzp
 */
public class ItemEnergyStorage extends CustomEnergyStorage {

    private final ItemStack stack;

    public ItemEnergyStorage(ItemStack stack, int capacity) {
        super(capacity);
        this.stack = stack;
        this.fixItemStack();
    }

    public ItemEnergyStorage(ItemStack stack, int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
        this.stack = stack;
        this.fixItemStack();
    }

    public ItemEnergyStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
        this.stack = stack;
        this.fixItemStack();
    }

    public ItemEnergyStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        this.stack = stack;
        this.fixItemStack();
    }

    @Override
    public int getEnergyStored() {
        return Objects.requireNonNull(this.stack.getTagCompound()).getCompoundTag("TileData").getInteger("Energy");
    }

    @Override
    public void setEnergy(int energy){
        Objects.requireNonNull(this.stack.getTagCompound()).getCompoundTag("TileData").setInteger("Energy", energy);
    }

    private void fixItemStack(){
        if(!this.stack.hasTagCompound()){
            this.stack.setTagCompound(new NBTTagCompound());
        }
        if(!Objects.requireNonNull(this.stack.getTagCompound()).hasKey("TileData",Constants.NBT.TAG_COMPOUND)){
            this.stack.getTagCompound().setTag("TileData", new NBTTagCompound());
        }
    }

}
