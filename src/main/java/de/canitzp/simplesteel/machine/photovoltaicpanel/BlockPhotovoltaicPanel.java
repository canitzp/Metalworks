package de.canitzp.simplesteel.machine.photovoltaicpanel;

import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import de.canitzp.simplesteel.block.BlockContainerBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class BlockPhotovoltaicPanel extends BlockContainerBase<BlockPhotovoltaicPanel> {

    public BlockPhotovoltaicPanel() {
        super(Material.IRON, MapColor.PURPLE, "photovoltaic_panel");
        this.setHardness(4.5F);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    protected Class<? extends TileEntity> getTileEntityClass() {
        return TilePhotovoltaicPanel.class;
    }

}
