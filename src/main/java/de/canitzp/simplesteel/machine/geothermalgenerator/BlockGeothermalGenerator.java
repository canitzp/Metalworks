package de.canitzp.simplesteel.machine.geothermalgenerator;

import de.canitzp.simplesteel.block.BlockContainerBase;
import de.canitzp.simplesteel.machine.duster.TileDuster;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class BlockGeothermalGenerator extends BlockContainerBase<BlockGeothermalGenerator> {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST);

    public BlockGeothermalGenerator() {
        super(Material.IRON, MapColor.BLACK_STAINED_HARDENED_CLAY, "geothermal_generator");
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(4.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(3 & meta)).withProperty(ACTIVE, (meta & 4) == 4);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(ACTIVE) ? 4 : 0);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE, FACING);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileGeothermalGenerator){
            state = state.withProperty(ACTIVE, ((TileGeothermalGenerator) tile).burn > 0 && ((TileGeothermalGenerator) tile).energy.getEnergyStored() < ((TileGeothermalGenerator) tile).energy.getMaxEnergyStored());
        }
        return state;
    }

    @Override
    protected Class<? extends TileEntity> getTileEntityClass() {
        return TileGeothermalGenerator.class;
    }
}
