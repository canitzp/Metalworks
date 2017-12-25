package de.canitzp.metalworks.machine.photovoltaicpanel;

import de.canitzp.metalworks.block.BlockContainerBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

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
