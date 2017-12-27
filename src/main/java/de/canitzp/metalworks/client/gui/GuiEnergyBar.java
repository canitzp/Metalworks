package de.canitzp.metalworks.client.gui;

import com.google.common.collect.Lists;
import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class GuiEnergyBar extends Gui{

    public static final ResourceLocation LOCATION = new ResourceLocation(Metalworks.MODID, "textures/gui/energy_bar.png");
    public static final Minecraft mc = Minecraft.getMinecraft();
    private int x, y;

    public GuiEnergyBar(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(int energy, int cap, int color){
        if(color < 0){
            this.draw(energy, cap, -1, -1, -1, -1);
        } else {
            float a = (float)(color >> 24 & 255) / 255.0F;
            float r = (float)(color >> 16 & 255) / 255.0F;
            float g = (float)(color >> 8 & 255) / 255.0F;
            float b = (float)(color & 255) / 255.0F;
            this.draw(energy, cap, r, g, b, a);
        }
    }

    public void draw(int energy, int cap, float r, float g, float b,  float a){
        GlStateManager.pushMatrix();
        if(r < 0 || g < 0 || b <  0 || a < 0){
            Util.applyWheelColor(mc.world, 1.0F);
        } else {
            GlStateManager.color(r, g, b, a);
        }
        mc.getTextureManager().bindTexture(LOCATION);
        int i = (int) ((energy / (cap * 1.0F)) * 62.0F);
        this.drawTexturedModalRect(this.x, this.y + 62 - i, 0, 62 - i, 16, i);
        this.drawTexturedModalRect(this.x, this.y, 16, 0, 16, 62);
        GlStateManager.popMatrix();
    }

    public void mouseDraw(GuiScreen gui, int guiLeft, int guiTop, int mouseX, int mouseY, int energy, int usage){
        if(mouseX >= guiLeft + 11 && mouseX <= guiLeft + 11 + 16 && mouseY >= guiTop + 11 && mouseY <= guiTop + 11 + 62){
            gui.drawHoveringText(Lists.newArrayList(Util.formatEnergy(energy), "Usage: " + Util.formatEnergy(usage)), mouseX, mouseY);
        }
    }

}
