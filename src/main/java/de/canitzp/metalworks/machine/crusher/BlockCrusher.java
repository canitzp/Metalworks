package de.canitzp.metalworks.machine.crusher;

import de.canitzp.metalworks.Registry;
import de.canitzp.metalworks.block.BlockContainerBase;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static de.canitzp.metalworks.Props.ACTIVE;
import static de.canitzp.metalworks.Props.FACING;

/**
 * @author canitzp
 */
@SuppressWarnings("deprecation")
public class BlockCrusher extends BlockContainerBase<BlockCrusher> {

    public BlockCrusher() {
        super(Material.IRON, Registry.DEFAULT_COLOR, "crusher");
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(4.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
        this.addInterface(InterfaceCrusher.class);
        this.setMachineItemBlock(true);
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

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    protected Class<? extends TileBase> getTileEntityClass() {
        return TileCrusher.class;
    }
}
