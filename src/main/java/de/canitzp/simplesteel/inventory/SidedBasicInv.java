package de.canitzp.simplesteel.inventory;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public abstract class SidedBasicInv extends InventoryBasic implements ISidedInventory {

    public SidedBasicInv(String title, int slotCount) {
        super(title, false, slotCount);
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        int[] ints = new int[this.getSizeInventory()];
        for(int i = 0; i < this.getSizeInventory(); i++){
            ints[i] = i;
        }
        return ints;
    }

    public NBTTagCompound writeTag(NBTTagCompound nbt){
        if(this.getSizeInventory() > 0){
            NBTTagList tagList = new NBTTagList();
            for(int i = 0; i < this.getSizeInventory(); i++){
                ItemStack slot = this.getStackInSlot(i);
                NBTTagCompound tagCompound = new NBTTagCompound();
                if(!slot.isEmpty()){
                    slot.writeToNBT(tagCompound);
                }
                tagList.appendTag(tagCompound);
            }
            nbt.setTag("Inventory", tagList);
        }
        return nbt;
    }

    public void readTag(NBTTagCompound nbt){
        if(nbt.hasKey("Inventory", Constants.NBT.TAG_LIST)){
            if(this.getSizeInventory() > 0){
                NBTTagList tagList = nbt.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
                for(int i = 0; i < this.getSizeInventory(); i++){
                    NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                    this.setInventorySlotContents(i, tagCompound.hasKey("id") ? new ItemStack(tagCompound) : ItemStack.EMPTY);
                }
            }
        }
    }

}
