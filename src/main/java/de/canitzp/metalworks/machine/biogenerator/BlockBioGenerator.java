package de.canitzp.metalworks.machine.biogenerator;

import de.canitzp.metalworks.block.BlockContainerBase;
import de.canitzp.metalworks.client.renderer.RenderBioGenerator;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static de.canitzp.metalworks.Props.ACTIVE;
import static de.canitzp.metalworks.Props.FACING;

/**
 * @author canitzp
 */
@SuppressWarnings("deprecation")
public class BlockBioGenerator extends BlockContainerBase<BlockBioGenerator> {

    public BlockBioGenerator() {
        super(Material.IRON, "bio_generator");
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(4.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
        this.addInterface(InterfaceBioGenerator.class);
        this.setMachineItemBlock(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        super.registerClient();
        ClientRegistry.bindTileEntitySpecialRenderer(TileBioGenerator.class, new RenderBioGenerator());
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
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
        return TileBioGenerator.class;
    }
}
