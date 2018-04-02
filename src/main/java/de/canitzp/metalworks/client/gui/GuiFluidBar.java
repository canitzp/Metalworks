package de.canitzp.metalworks.client.gui;

import de.canitzp.metalworks.CustomFluidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class GuiFluidBar extends Gui {

    public static final Map<Fluid, ResourceLocation> resources = new HashMap<>();
    private FluidStack cachedStack;
    private final int x;
    private final int y;
    private int fluidChange;
    private long lastChangeTime = System.currentTimeMillis();

    public GuiFluidBar(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static ResourceLocation getFluidTexture(Fluid fluid){
        if(fluid != null){
            ResourceLocation loc;
            if(resources.containsKey(fluid)){
                loc = resources.get(fluid);
            } else {
                resources.put(fluid, loc = new ResourceLocation(fluid.getStill().getResourceDomain(), "textures/" + fluid.getStill().getResourcePath() + ".png"));
            }
            return loc;
        }
        return null;
    }

    public void draw(CustomFluidTank tank){
        if(tank != null){
            FluidStack fluidStack = tank.getFluid();
            if(fluidStack != null){
                Fluid fluid = fluidStack.getFluid();
                if(fluid != null){
                    GlStateManager.pushMatrix();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(GuiEnergyBar.LOCATION);
                    this.drawTexturedModalRect(this.x, this.y, 16, 62, 16, 62);
                    ResourceLocation loc = getFluidTexture(fluid);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
                    GlStateManager.enableBlend();
                    GlStateManager.disableAlpha();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    int i = fluidStack.amount * 62 / tank.getCapacity();
                    drawModalRectWithCustomSizedTexture(this.x, this.y + 62 - i, 36, 172, 16, i, 16, 512);
                    GlStateManager.disableBlend();
                    GlStateManager.enableAlpha();
                    GlStateManager.popMatrix();
                    if(this.cachedStack != null){
                        int change = tank.getFluidAmount() - this.cachedStack.amount;
                        if(change != 0 || System.currentTimeMillis() - this.lastChangeTime >= 500){
                            this.fluidChange = change;
                            this.lastChangeTime = System.currentTimeMillis();
                        }
                    }
                    this.cachedStack = tank.getFluid();
                }
            }
        }
    }

    public void drawMouseOver(GuiScreen gui, int mouseX, int mouseY, CustomFluidTank tank){
        if(mouseX >= this.x && mouseX <= this.x + 16 && mouseY >= this.y && mouseY <= this.y + 62){
            List<String> lines = new ArrayList<>();
            if(tank.getFluid() != null){
                lines.add(tank.getFluid().getLocalizedName());
                lines.add(String.format("%smB / %smB", tank.getFluidAmount(), tank.getCapacity()));
                if(this.cachedStack != null){
                    if(this.fluidChange > 0){
                        lines.add("Change: " + TextFormatting.GREEN + "+" + (this.fluidChange) + "mB" + TextFormatting.RESET);
                    } else if(this.fluidChange < 0){
                        lines.add("Change: " + TextFormatting.DARK_RED + (this.fluidChange) + "mB" + TextFormatting.RESET);
                    } else {
                        lines.add("Change: 0mB");
                    }
                }
            } else {
                lines.add(String.format("Empty 0mB / %smB", tank.getCapacity()));
            }
            gui.drawHoveringText(lines, mouseX, mouseY);
        }
    }

}
