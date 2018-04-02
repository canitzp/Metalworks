package de.canitzp.metalworks.item;

import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.block.BlockContainerBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author canitzp
 */
public class ItemMachineBlock<T extends BlockContainerBase<?>> extends ItemBlock {

    private static long lastSideSwitch = 0;

    private EnumFacing activeSide;
    private final List<EnumFacing> sidesToShow;

    public ItemMachineBlock(T block) {
        super(block);
        this.sidesToShow = block.getSidesToShowInTooltip();
        if(this.sidesToShow != null && !this.sidesToShow.isEmpty()){
            this.activeSide = this.sidesToShow.get(0);
        }
        this.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
        if (this.activeSide != null && this.sidesToShow.size() > 1 && world != null && world.getTotalWorldTime() - lastSideSwitch >= 40) {
            lastSideSwitch = world.getTotalWorldTime();
            if (this.sidesToShow.indexOf(this.activeSide) < this.sidesToShow.size() - 1) {
                this.activeSide = this.sidesToShow.get(this.sidesToShow.indexOf(this.activeSide) + 1);
            } else {
                this.activeSide = this.sidesToShow.get(0);
            }
        }
        super.addInformation(stack, world, tooltip, flagIn);
        if (this.sidesToShow != null && this.activeSide != null && this.sidesToShow.size() > 1) {
            tooltip.add(String.format("Current side: %s", StringUtils.capitalize(activeSide.getName())));
        }
        String sideString = this.activeSide != null ? this.activeSide.toString() : "default";
        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("TileData", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound tileData = stack.getTagCompound().getCompoundTag("TileData");
            if (tileData.hasKey("TileBaseCapabilities", Constants.NBT.TAG_COMPOUND)) {
                NBTTagCompound tileCapsData = tileData.getCompoundTag("TileBaseCapabilities");
                if (tileCapsData.hasKey(sideString, Constants.NBT.TAG_COMPOUND)) {
                    NBTTagCompound sidedTag = tileCapsData.getCompoundTag(sideString);
                    if (sidedTag.hasKey("Energy", Constants.NBT.TAG_COMPOUND)) {
                        NBTTagCompound energyTag = sidedTag.getCompoundTag("Energy");
                        tooltip.add(String.format("%s / %s", Util.formatEnergy(energyTag.getInteger("Stored")), Util.formatEnergy(energyTag.getInteger("Capacity"))));
                    }
                    if (sidedTag.hasKey("FluidTank", Constants.NBT.TAG_COMPOUND)) {
                        NBTTagCompound fluidTag = sidedTag.getCompoundTag("FluidTank");
                        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(fluidTag);
                        if (fluidStack != null && fluidStack.getFluid() != null) {
                            tooltip.add(String.format("%s %smB / %smB", fluidStack.getLocalizedName(), fluidStack.amount, fluidTag.getInteger("Capacity")));
                        }
                    }
                }
            }
        }
    }
}
