package de.canitzp.metalworks.gui;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.inventory.ContainerMachine;
import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiMachine<T extends TileBase> extends GuiContainer {

    public static final ResourceLocation INVENTORY_LOCATION = new ResourceLocation(Metalworks.MODID, "textures/gui/inventory.png");

    private IMachineInterface<T> machineInterface;
    private T tile;
    private TextureManager texture;
    private EntityPlayer player;

    public GuiMachine(EntityPlayer player, T tile, IMachineInterface<T> machineInterface, ContainerMachine<T> con) {
        super(con);
        this.machineInterface = machineInterface;
        this.tile = tile;
        this.player = player;
    }

    @Override
    public void initGui() {
        this.machineInterface.initGui(this.tile, this);
        super.initGui();
        this.texture = this.mc.getTextureManager();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawDefaultBackground();
        this.texture.bindTexture(INVENTORY_LOCATION);
        this.machineInterface.drawBackground(this.tile, this, this.texture, this.guiLeft, this.guiTop, mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
        this.machineInterface.drawScreen(this.tile, this, this.guiLeft, this.guiTop, mouseX, mouseY, partialTicks);
    }

    public GuiMachine<T> setSize(int xSize, int ySize){
        this.xSize = xSize;
        this.ySize = ySize;
        return this;
    }

    public int getXSize(){
        return this.xSize;
    }

    public int getYSize(){
        return this.ySize;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
