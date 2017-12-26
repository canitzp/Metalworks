package de.canitzp.metalworks.machine.photovoltaicpanel;

import de.canitzp.metalworks.block.BlockContainerBase;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

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
    protected Class<? extends TileBase> getTileEntityClass() {
        return TilePhotovoltaicPanel.class;
    }

}
