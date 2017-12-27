package de.canitzp.metalworks.client.gui;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.integration.jei.SimpleSteelJEIPlugin;
import de.canitzp.metalworks.inventory.ContainerMachine;
import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiMachine<T extends TileBase> extends GuiContainer {

    public static final ResourceLocation INVENTORY_LOCATION = new ResourceLocation(Metalworks.MODID, "textures/gui/inventory.png");
    public static  boolean isJeiLoaded;

    private IMachineInterface<T> machineInterface;
    private T tile;
    private TextureManager texture;
    private EntityPlayer player;
    private GuiEnergyBar energyBar;
    private IMachineInterface.JEIData jeiStuff;

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
        this.energyBar = this.machineInterface.getEnergyBar(this.tile, this, this.player, this.guiLeft, this.guiTop);
        if(isJeiLoaded){
            this.jeiStuff = this.machineInterface.getJEIClickArea(this.tile, this, this.player, this.guiLeft, this.guiTop);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawDefaultBackground();
        Pair<Integer, Integer> invCoords = this.machineInterface.getInventoryLocation(this.tile, this.player);
        if(invCoords != null){
            this.texture.bindTexture(INVENTORY_LOCATION);
            this.drawTexturedModalRect(invCoords.getLeft() + this.guiLeft, invCoords.getRight() + this.guiTop, 0, 0, 175, 84);
        }
        this.machineInterface.drawBackground(this.tile, this, this.texture, this.guiLeft, this.guiTop, mouseX, mouseY, partialTicks);
        if(this.energyBar != null){
            IEnergyStorage energy = this.tile.getEnergy(EnumFacing.NORTH);
            if(energy != null && energy.getMaxEnergyStored() > 0){
                this.energyBar.draw(energy.getEnergyStored(), energy.getMaxEnergyStored(), -1);
            }
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
        this.machineInterface.drawScreen(this.tile, this, this.guiLeft, this.guiTop, mouseX, mouseY, partialTicks);
        if(this.energyBar != null && this.tile.getCurrentEnergyUsage() > -1){
            IEnergyStorage energy = this.tile.getEnergy(EnumFacing.NORTH);
            if(energy != null && energy.getMaxEnergyStored() > 0){
                this.energyBar.mouseDraw(this, guiLeft, guiTop, mouseX, mouseY, energy.getEnergyStored(), this.tile.getCurrentEnergyUsage());
            }
        }
        if(this.jeiStuff != null){
            IMachineInterface.JEIData data = this.jeiStuff;
            if(mouseX >= data.x + this.guiLeft && mouseX <= data.x + this.guiLeft + data.width && mouseY >= data.y + this.guiTop && mouseY <= data.y + this.guiTop + data.height){
                this.drawHoveringText(I18n.format("jei.tooltip.show.recipes"), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(this.jeiStuff != null){
            IMachineInterface.JEIData data = this.jeiStuff;
            if(mouseX >= data.x + this.guiLeft && mouseX <= data.x + this.guiLeft + data.width && mouseY >= data.y + this.guiTop && mouseY <= data.y + this.guiTop + data.height){
                SimpleSteelJEIPlugin.openRecipes(data.cats);
            }
        }
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
