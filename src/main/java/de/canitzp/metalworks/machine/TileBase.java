package de.canitzp.metalworks.machine;

import de.canitzp.metalworks.CustomEnergyStorage;
import de.canitzp.metalworks.network.NetworkHandler;
import de.canitzp.metalworks.network.packet.PacketSyncTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author canitzp
 */
public class TileBase extends TileEntity {

    @Override
    public final void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.readNBT(compound, NBTType.SAVE);
    }

    @Nonnull
    @Override
    public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.writeNBT(compound, NBTType.SAVE);
        return compound;
    }

    @Override
    public final boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return this.getCapability(capability, facing) != null;
    }

    @Nullable
    @Override
    public final <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            IItemHandler inventory = getInventory(facing);
            if(inventory != null){
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
            }
        }
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            IFluidHandler tank = getTank(facing);
            if(tank != null){
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
            }
        }
        if(capability == CapabilityEnergy.ENERGY){
            IEnergyStorage energy = getEnergy(facing);
            if(energy != null){
                return CapabilityEnergy.ENERGY.cast(energy);
            }
        }
        return null;
    }

    //only affects saving
    protected Triple<Boolean, Boolean, Boolean> hasEnergyFluidInv(){
        return Triple.of(true, true, true);
    }

    protected Collection<EnumFacing> getSidesToSave(){
        return Collections.emptyList();
    }

    @Nullable
    public IItemHandler getInventory(@Nullable EnumFacing side){
        return null;
    }

    @Nullable
    public IEnergyStorage getEnergy(@Nullable EnumFacing side){
        return null;
    }

    @Nullable
    public IFluidHandler getTank(@Nullable EnumFacing side){
        return null;
    }

    public void writeNBT(NBTTagCompound nbt, NBTType type) {
        NBTTagCompound caps = new NBTTagCompound();
        Collection<EnumFacing> sidesToSave = this.getSidesToSave();
        if (sidesToSave != null && !sidesToSave.isEmpty()) {
            for (EnumFacing side : sidesToSave) {
                NBTTagCompound capsSided = new NBTTagCompound();
                this.writeCapabilities(capsSided, side);
                if (!capsSided.hasNoTags()) {
                    caps.setTag(side.toString().toLowerCase(Locale.ROOT), capsSided);
                }
            }
        } else {
            NBTTagCompound capsSided = new NBTTagCompound();
            this.writeCapabilities(capsSided, null);
            if(!capsSided.hasNoTags()){
                caps.setTag("default", capsSided);
            }
        }
        if(!caps.hasNoTags()){
            nbt.setTag("TileBaseCapabilities", caps);
        }
    }

    public void readNBT(NBTTagCompound nbt, NBTType type) {
        NBTTagCompound caps = nbt.getCompoundTag("TileBaseCapabilities");
        Collection<EnumFacing> sidesToSave = this.getSidesToSave();
        if(sidesToSave != null && !sidesToSave.isEmpty()){
            for (EnumFacing side : sidesToSave) {
                String name = side.toString().toLowerCase(Locale.ROOT);
                if (caps.hasKey(name, Constants.NBT.TAG_COMPOUND)) {
                    this.readCapabilities(caps.getCompoundTag(name), side);
                }
            }
        } else if (caps.hasKey("default", Constants.NBT.TAG_COMPOUND)) {
            this.readCapabilities(caps.getCompoundTag("default"), null);
        }
    }

    private void readCapabilities(NBTTagCompound nbt, @Nullable EnumFacing side){
        if(hasEnergyFluidInv().getRight()){
            IItemHandler inventory = this.getInventory(side);
            if(inventory instanceof IItemHandlerModifiable && nbt.hasKey("Inventory")){
                for(int i = 0; i < inventory.getSlots(); i++){ // clear the inventory, otherwise empty stacks doesn't get overridden while syncing. Forge Bug?
                    ((IItemHandlerModifiable) inventory).setStackInSlot(i, ItemStack.EMPTY);
                }
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, side, nbt.getTag("Inventory"));
            }
        }
        if(hasEnergyFluidInv().getMiddle()){
            IFluidHandler tank = getTank(side);
            if(tank instanceof IFluidTank && nbt.hasKey("FluidTank")){
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tank, side, nbt.getCompoundTag("FluidTank"));
            }
        }
        if(hasEnergyFluidInv().getLeft()){
            IEnergyStorage energy = getEnergy(side);
            if(energy instanceof CustomEnergyStorage && nbt.hasKey("Energy", Constants.NBT.TAG_COMPOUND)){
                NBTTagCompound energyTag = nbt.getCompoundTag("Energy");
                ((CustomEnergyStorage) energy).setValues(energyTag.getInteger("Stored"), energyTag.getInteger("Capacity"),
                        energyTag.getInteger("MaxReceive"), energyTag.getInteger("MaxExtract"));
            }
        }
    }

    private void writeCapabilities(NBTTagCompound nbt, @Nullable EnumFacing side){
        if(hasEnergyFluidInv().getRight()){
            IItemHandler inventory = this.getInventory(side);
            if(inventory instanceof IItemHandlerModifiable){
                nbt.setTag("Inventory", Objects.requireNonNull(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, side)));
            }
        }
        if(hasEnergyFluidInv().getMiddle()){
            IFluidHandler tank = getTank(side);
            if(tank instanceof IFluidTank){
                nbt.setTag("FluidTank", Objects.requireNonNull(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tank, side)));
            }
        }
        if(hasEnergyFluidInv().getLeft()){
            IEnergyStorage energy = getEnergy(side);
            if(energy instanceof CustomEnergyStorage){
                NBTTagCompound energyTag = new NBTTagCompound();
                energyTag.setInteger("Stored", energy.getEnergyStored());
                energyTag.setInteger("Capacity", energy.getMaxEnergyStored());
                energyTag.setInteger("MaxReceive", ((CustomEnergyStorage) energy).getReceiveTransfer());
                energyTag.setInteger("MaxExtract", ((CustomEnergyStorage) energy).getExtractTransfer());
                nbt.setTag("Energy", energyTag);
            }
        }
    }

    @Override
    public void onLoad() {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                syncToClients();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private boolean isSyncDirty = true;
    public void syncToClients(){
        if(this.world != null && !this.world.isRemote){
            if(world.getTotalWorldTime() % 10 == 0 || !(this instanceof ITickable)){
                NBTTagCompound syncTag = new NBTTagCompound();
                this.writeNBT(syncTag, NBTType.SYNC);
                for(EntityPlayer player : this.world.playerEntities){
                    if(player instanceof EntityPlayerMP && player.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 64){
                        NetworkHandler.NET.sendTo(new PacketSyncTile(syncTag, this.pos), (EntityPlayerMP) player);
                    }
                }
                this.isSyncDirty = false;
            } else {
                this.isSyncDirty = true;
            }
        }
    }

    private static final Deque<Chunk> renderUpdateChunks = new ArrayDeque<>();
    @SideOnly(Side.CLIENT)
    public void markForRenderUpdate(){
        if(this.world != null && this.world.isRemote){
            if(!renderUpdateChunks.isEmpty() && this.world.getTotalWorldTime() % 20 == 0){
                Chunk chunk = renderUpdateChunks.getFirst();
                renderUpdateChunks.removeFirst();
                if(chunk.isLoaded()){
                    this.world.markBlockRangeForRenderUpdate(chunk.x << 4, 1, chunk.z << 4, chunk.x << 4, 255, chunk.z << 4);
                }
            } else {
                Chunk chunk = this.world.getChunkFromBlockCoords(this.getPos());
                if(!renderUpdateChunks.contains(chunk)){
                    renderUpdateChunks.addLast(chunk);
                }
            }
        }
    }

    protected void updateBase(){
        if(!this.world.isRemote && this.isSyncDirty){
            this.syncToClients();
        }
        if(this.world.isRemote && !renderUpdateChunks.isEmpty()){
            this.markForRenderUpdate();
        }
    }

    @SideOnly(Side.CLIENT)
    public void onSyncPacket(){}

    public void breakBlock(){
        if(!this.world.isRemote){
            List<IItemHandler> cached = new ArrayList<>();
            for(EnumFacing side : EnumFacing.values()){
                IItemHandler inv = this.getInventory(side);
                if(inv != null && !cached.contains(inv)){
                    for(int i = 0; i < inv.getSlots(); i++){
                        InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), inv.getStackInSlot(i));
                    }
                    cached.add(inv);
                }
            }
            cached.clear();
        }
    }

    public boolean isWorking(){
        return false;
    }

    public boolean canBeUsedBy(EntityPlayer player) {
        return player.getDistanceSq(this.getPos().getX()+0.5D, this.pos.getY()+0.5D, this.pos.getZ()+0.5D) <= 64 && !this.isInvalid() && this.world.getTileEntity(this.pos) == this;
    }

    public int getCurrentEnergyUsage(){
        return -1;
    }

    public enum NBTType{
        SAVE,
        DROP,
        SYNC
    }
}
