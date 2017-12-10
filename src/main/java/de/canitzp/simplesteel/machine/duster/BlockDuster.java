package de.canitzp.simplesteel.machine.duster;

import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import de.canitzp.simplesteel.block.BlockContainerBase;
import de.canitzp.simplesteel.machine.blastfurnace.TileBlastFurnace;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class BlockDuster extends BlockContainerBase<BlockDuster> {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST);

    public BlockDuster() {
        super(Material.IRON, "duster");
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
    protected Class<? extends TileEntity> getTileEntityClass() {
        return TileDuster.class;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote){
            player.openGui(SimpleSteel.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileDuster){
            state = state.withProperty(ACTIVE, ((TileDuster) tile).burn > 0 && ((TileDuster) tile).energy.getEnergyStored() >= ((TileDuster) tile).energyUsage);
        }
        return state;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if(!world.isRemote && world.getTileEntity(pos) instanceof TileDuster){
            InventoryHelper.dropInventoryItems(world, pos, ((TileDuster) world.getTileEntity(pos)).inventory);
        }
        super.breakBlock(world, pos, state);
    }
}
