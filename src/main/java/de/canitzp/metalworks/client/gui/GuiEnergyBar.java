package de.canitzp.metalworks.client.gui;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class GuiEnergyBar extends Gui{

    public static final ResourceLocation LOCATION = new ResourceLocation(Metalworks.MODID, "textures/gui/util.png");
    public static final Minecraft mc = Minecraft.getMinecraft();
    private final int x;
    private final int y;
    private final boolean standalone;

    public GuiEnergyBar(int x, int y, boolean standalone){
        this.x = x;
        this.y = y;
        this.standalone = standalone;
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
        mc.getTextureManager().bindTexture(LOCATION);
        int i = (int) ((energy / (cap * 1.0F)) * 62.0F);
        if(!standalone){
            applyColor(r, g, b, a);
            this.drawTexturedModalRect(this.x, this.y + 62 - i, 0, 62 - i, 16, i);
            //this.drawTexturedModalRect(this.x, this.y, 16, 0, 16, 62);
        } else {
            this.drawTexturedModalRect(this.x, this.y, 32, 0, 28, 74);
            applyColor(r, g, b, a);
            this.drawTexturedModalRect(this.x + 6, this.y + 62 + 6 - i, 0, 62 - i, 16, i);
            this.drawTexturedModalRect(this.x + 6, this.y + 6, 16, 0, 16, 62);
        }
        GlStateManager.popMatrix();
    }

    private void applyColor(float r, float g, float b,  float a){
        if(r < 0 || g < 0 || b <  0 || a < 0){
            Util.applyWheelColor(mc.world, 1.0F);
        } else {
            GlStateManager.color(r, g, b, a);
        }
    }

    public void mouseDrawTank(GuiScreen gui, int mouseX, int mouseY, int energy, int max, int usage){
        if(mouseX >= this.x && mouseX <= this.x + 16 && mouseY >= this.y && mouseY <= this.y + 62){
            List<String> lines = new ArrayList<>();
            lines.add(String.format("%s / %s", Util.formatEnergy(energy), Util.formatEnergy(max)));
            if(usage != 0){
                lines.add(String.format("%s: %s", usage > 0 ? "Usage" : "Generation", Util.formatEnergy(Math.abs(usage))));
            }
            gui.drawHoveringText(lines, mouseX, mouseY);
        }
    }

}
