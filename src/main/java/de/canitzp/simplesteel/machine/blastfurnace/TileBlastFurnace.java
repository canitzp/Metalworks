package de.canitzp.simplesteel.machine.blastfurnace;

import de.canitzp.simplesteel.inventory.SidedBasicInv;
import de.canitzp.simplesteel.inventory.SidedWrapper;
import de.canitzp.simplesteel.SimpleSteel;
import de.canitzp.simplesteel.recipe.SimpleSteelRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

/**
 * @author canitzp
 */
public class TileBlastFurnace extends TileEntity implements ITickable{

    public static final int INPUT1 = 0;
    public static final int INPUT2 = 1;
    public static final int INPUT3 = 2;
    public static final int OUTPUT1 = 3;
    public static final int OUTPUT2 = 4;
    public static final int FUEL = 5;

    public EnergyStorage energy = new EnergyStorage(10000, 1500){
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int i = super.receiveEnergy(maxReceive, simulate);
            TileBlastFurnace.this.sync(true);
            return i;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int i = super.extractEnergy(maxExtract, simulate);
            TileBlastFurnace.this.sync(true);
            return i;
        }
    };
    public SidedBasicInv inventory = new SidedBasicInv("blast_furnace", 6) {
        @Override
        public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
            if(side != EnumFacing.DOWN) {
                ItemStack fuel = this.getStackInSlot(FUEL);
                return fuel.isEmpty() || !ItemHandlerHelper.canItemStacksStack(fuel, stack) || index == FUEL || fuel.getCount() + stack.getCount() > fuel.getMaxStackSize();
            }
            return false;
        }
        @Override
        public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing side) {
            return (index == 3 || index == 4) && side != EnumFacing.UP;
        }
        @Override
        public void markDirty() {
            super.markDirty();
            TileBlastFurnace.this.markDirty();
            TileBlastFurnace.this.sync(true);
        }

        @Override
        public boolean isItemValidForSlot(int index, ItemStack stack) {
            return index != FUEL || TileEntityFurnace.isItemFuel(stack);
        }

    };
    private SidedWrapper wrapper = new SidedWrapper(this.inventory, EnumFacing.NORTH);
    private String recipeID = null;
    public int maxBurn, burnLeft, energyUsage;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.recipeID = compound.hasKey("RecipeUUID", Constants.NBT.TAG_STRING) ? compound.getString("RecipeUUID") : null;
        this.maxBurn = compound.getInteger("MaxBurn");
        this.burnLeft = compound.getInteger("BurnLeft");
        this.energyUsage = compound.getInteger("EnergyUsagePerTick");
        CapabilityEnergy.ENERGY.readNBT(this.energy, null, compound.hasKey("Energy") ? compound.getTag("Energy") : new NBTTagInt(0));
        this.inventory.readTag(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if(this.recipeID != null) {
            compound.setString("RecipeUUID", this.recipeID);
        }
        compound.setInteger("MaxBurn", this.maxBurn);
        compound.setInteger("BurnLeft", this.burnLeft);
        compound.setInteger("EnergyUsagePerTick", this.energyUsage);
        compound.setTag("Energy", Validate.notNull(CapabilityEnergy.ENERGY.writeNBT(this.energy, null)));
        this.inventory.writeTag(compound);
        return super.writeToNBT(compound);
    }

    @Override
    public void update() {
        if(!world.isRemote){
            if(this.recipeID != null){
                if(this.burnLeft <= 0){
                    this.burnLeft = this.maxBurn = this.energyUsage = 0;
                    RecipeBlastFurnace recipe = SimpleSteelRecipeHandler.getBlastFurnaceRecipeForId(this.recipeID);
                    if(recipe != null){
                        ItemStack out1 = this.inventory.getStackInSlot(OUTPUT1);
                        ItemStack out2 = this.inventory.getStackInSlot(OUTPUT2);
                        if(recipe.isOutputMergeable(out1, out2)){
                            if(!recipe.getOutputs()[0].isEmpty()){
                                if(out1.isEmpty()){
                                    this.inventory.setInventorySlotContents(OUTPUT1, recipe.getOutputs()[0].copy());
                                } else {
                                    out1.grow(recipe.getOutputs()[0].getCount());
                                }
                            }
                            if(recipe.getSecondOutputChance() > 0 && new Random().nextInt(100/recipe.getSecondOutputChance()) - 1 == 0 && !recipe.getOutputs()[1].isEmpty()){
                                if(out2.isEmpty()){
                                    this.inventory.setInventorySlotContents(OUTPUT2, recipe.getOutputs()[1].copy());
                                } else {
                                    out2.grow(recipe.getOutputs()[1].getCount());
                                }
                            }
                        }
                    } else {
                        System.out.println("A 'null' recipe was processed! This is maybe caused by a removed mod, while the burn progress.");
                    }
                    this.recipeID = null;
                    this.sync(true);
                } else {
                    if(this.energy.extractEnergy(this.energyUsage, true) == this.energyUsage){
                        this.energy.extractEnergy(this.energyUsage, false);
                        this.burnLeft--;
                    } else if(this.burnLeft < this.maxBurn) {
                        this.burnLeft++;
                    }
                    this.sync(false);
                }
            } else {
                ItemStack input1 = this.inventory.getStackInSlot(INPUT1);
                ItemStack input2 = this.inventory.getStackInSlot(INPUT2);
                ItemStack input3 = this.inventory.getStackInSlot(INPUT3);
                if((!input1.isEmpty() || !input2.isEmpty() || !input3.isEmpty())){
                    RecipeBlastFurnace recipe = SimpleSteelRecipeHandler.getBlastFurnaceRecipe(input1, input2, input3);
                    if(recipe != null && recipe.isOutputMergeable(this.inventory.getStackInSlot(OUTPUT1), this.inventory.getStackInSlot(OUTPUT2)) && this.energy.extractEnergy(recipe.getEnergyUsage(), true) == recipe.getEnergyUsage()){
                        this.recipeID = SimpleSteelRecipeHandler.getIdForBlastFurnaceRecipe(recipe);
                        this.burnLeft = this.maxBurn = recipe.getBurnTime();
                        this.energyUsage = recipe.getEnergyUsage();
                        if(!input1.isEmpty()){
                            recipe.shrink(input1);
                        }
                        if(!input2.isEmpty()){
                            recipe.shrink(input2);
                        }
                        if(!input3.isEmpty()){
                            recipe.shrink(input3);
                        }
                        this.sync(false);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.wrapper);
        }
        if(capability == CapabilityEnergy.ENERGY){
            return CapabilityEnergy.ENERGY.cast(this.energy);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        if(this.world != null && this.world.isRemote){
            this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
        }
        this.handleUpdateTag(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), 0, this.writeToNBT(new NBTTagCompound()));
    }

    public void sync(boolean force){
        if(this.world != null && !this.world.isRemote && (force || world.getTotalWorldTime() % 10 == 0)){
            for(EntityPlayer player : this.getWorld().playerEntities){
                if(player instanceof EntityPlayerMP){
                    BlockPos pos = this.getPos();
                    SPacketUpdateTileEntity packet = this.getUpdatePacket();
                    if(player.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 64 && packet != null){
                        ((EntityPlayerMP) player).connection.sendPacket(packet);
                    }
                }
            }
        }
    }

}
