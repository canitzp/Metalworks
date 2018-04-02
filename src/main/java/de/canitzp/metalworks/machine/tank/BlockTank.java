package de.canitzp.metalworks.machine.tank;

import de.canitzp.metalworks.block.BlockContainerBase;
import de.canitzp.metalworks.client.renderer.RenderTank;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author canitzp
 */
public class BlockTank extends BlockContainerBase<BlockTank> {

    public BlockTank() {
        super(Material.GLASS, "tank");
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(2.5F);
        this.setMachineItemBlock(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        super.registerClient();
        ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new RenderTank());
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

    @Override
    protected Class<? extends TileBase> getTileEntityClass() {
        return TileTank.class;
    }
}
