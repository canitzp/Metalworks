package de.canitzp.metalworks.client.renderer;

import de.canitzp.metalworks.CustomFluidTank;
import de.canitzp.metalworks.client.gui.GuiFluidBar;
import de.canitzp.metalworks.machine.biogenerator.TileBioGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author canitzp
 */
public class RenderBioGenerator extends TileEntitySpecialRenderer<TileBioGenerator> {

    private int heightStep = 0;
    private long lastWorldTime;
    private boolean invert = false;

    @Override
    public void render(TileBioGenerator tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        CustomFluidTank tank = tile.tank;
        if(tank.getFluid() != null){
            FluidStack fluidStack = tank.getFluid();
            int texHeight = fluidStack.getFluid() == FluidRegistry.LAVA ? 320 : 512;
            if(tile.getWorld().getTotalWorldTime() % 2 == 0){
                if(lastWorldTime != tile.getWorld().getTotalWorldTime()){
                    if(invert){
                        heightStep-=16;
                    } else {
                        heightStep+=16;
                    }
                    if(heightStep >= texHeight){
                        if(fluidStack.getFluid() == FluidRegistry.LAVA){
                            invert = true;
                            heightStep = texHeight - 16;
                        } else {
                            heightStep = 0;
                        }
                    }
                    if(heightStep <= 0 && invert){
                        invert = false;
                    }
                    lastWorldTime = tile.getWorld().getTotalWorldTime();
                }
            }
            int i = (tank.getCapacity() - fluidStack.amount) * 14 / tank.getCapacity();
            ResourceLocation loc = GuiFluidBar.getFluidTexture(fluidStack.getFluid());
            GlStateManager.pushMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
            GlStateManager.translate(x, y + 0.0625, z);
            float scale = 0.0625F;
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.pushMatrix();
                GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.translate(-15, 0, 1);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, heightStep, 14, 14 - i, 16, texHeight);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
                //GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.translate(1, 0, 1);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, heightStep, 14, 14 - i, 16, texHeight);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
                GlStateManager.rotate(-90, 0, 1, 0);
                GlStateManager.translate(1, 0, -15);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, heightStep, 14, 14 - i, 16, texHeight);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.translate(-15, 0, -15);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, heightStep, 14, 14 - i, 16, texHeight);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(1, 14 - i, 1);
                GlStateManager.rotate(90, 1, 0, 0);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, heightStep, 14, 14, 16, texHeight);
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }
        super.render(tile, x, y, z, partialTicks, destroyStage, alpha);
    }
}
