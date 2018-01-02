package de.canitzp.metalworks.client.renderer;

import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.machine.supercharger.TileSuperCharger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * @author canitzp
 */
public class RenderSuperCharger extends TileEntitySpecialRenderer<TileSuperCharger> {

    @Override
    public void render(TileSuperCharger te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ItemStack stack = te.getInventory(EnumFacing.NORTH).getStackInSlot(0);
        if(!stack.isEmpty() && !te.getWorld().getBlockState(te.getPos().up()).isFullBlock()){
            this.setLightmapDisabled(true);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 1.0, z + 0.5);
            GlStateManager.rotate(90, 1 , 0, 0);
            GlStateManager.rotate(90, 0 , 0, 1);
            float scale = stack.getItem() instanceof ItemBlock ? 0.85F : 0.65F;
            GlStateManager.scale(scale, scale, scale);
            if(stack.getItem() instanceof ItemBlock){
                GlStateManager.rotate(-90, 1, 0, 0);
                GlStateManager.translate(0, 0.25, 0);
            }
            Util.renderItemInWorld(stack);
            GlStateManager.popMatrix();
            this.setLightmapDisabled(false);
        }
    }

}
