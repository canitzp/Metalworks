package de.canitzp.metalworks.machine.battery;

import de.canitzp.metalworks.Props;
import de.canitzp.metalworks.block.BlockContainerBase;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author canitzp
 */
public class BlockBattery extends BlockContainerBase<BlockBattery> {

    public static PropertyInteger STATE = PropertyInteger.create("state", 0, 10);

    private Type batteryType;

    public BlockBattery(Type batteryType) {
        super(Material.IRON, "battery_" + batteryType.name);
        this.batteryType = batteryType;
        this.setEnergeticItem(batteryType.maxEnergy, batteryType.maxTransfer, batteryType.maxTransfer);
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(4.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.NORTH).withProperty(STATE, 0));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    protected Class<? extends TileBase> getTileEntityClass() {
        return TileBattery.class;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockDirectional.FACING).getIndex();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockDirectional.FACING, STATE);
    }

    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileBattery){
            state = state.withProperty(STATE, ((TileBattery) tile).getChargingState());
        }
        return super.getActualState(state, world, pos);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(BlockDirectional.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing side = state.getValue(BlockDirectional.FACING);
        switch (side){
            case DOWN:
            case UP: {
                return new AxisAlignedBB(2/16F, 0, 2/16F, 14/16F, 1, 14/16F);
            }
            case EAST:
            case WEST: {
                return new AxisAlignedBB(0, 2/16F, 2/16F, 1, 14/16F, 14/16F);
            }
            case NORTH:
            case SOUTH: {
                return new AxisAlignedBB(2/16F, 2/16F, 0, 14/16F, 14/16F, 1);
            }
        }
        return FULL_BLOCK_AABB;
    }

    public Type getBatteryType() {
        return batteryType;
    }

    public static class Type{
        public String name;
        public int maxEnergy, maxTransfer;

        public Type(String name, int maxEnergy, int maxTransfer) {
            this.name = name;
            this.maxEnergy = maxEnergy;
            this.maxTransfer = maxTransfer;
        }
    }
}
