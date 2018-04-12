package de.canitzp.metalworks.client.gui;

import de.canitzp.metalworks.CustomFluidTank;
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
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author canitzp
 */
@SideOnly(Side.CLIENT)
public class GuiMachine<T extends TileBase> extends GuiContainer {

    public static final ResourceLocation INVENTORY_LOCATION = new ResourceLocation(Metalworks.MODID, "textures/gui/inventory.png");
    public static  boolean isJeiLoaded;

    private final IMachineInterface<T> machineInterface;
    private final T tile;
    private TextureManager texture;
    private final EntityPlayer player;
    private GuiEnergyBar energyBar;
    private final Map<EnumFacing, GuiFluidBar> fluidBars = new ConcurrentHashMap<>();
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
        Map<EnumFacing, GuiFluidBar> interfaceBars = this.machineInterface.getFluidBar(this.tile, this, this.player, this.guiLeft, this.guiTop);
        if(interfaceBars != null){
            this.fluidBars.putAll(interfaceBars);
        }
        if(isJeiLoaded){
            this.jeiStuff = this.machineInterface.getJEIClickArea(this.tile, this, this.player, this.guiLeft, this.guiTop);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Pair<Integer, Integer> invCoords = this.machineInterface.getInventoryLocation(this.tile, this.player);
        if(invCoords != null){
            this.texture.bindTexture(INVENTORY_LOCATION);
            this.drawTexturedModalRect(invCoords.getLeft() + this.guiLeft, invCoords.getRight() + this.guiTop, 0, 0, 176, 85);
        }
        this.machineInterface.drawBackground(this.tile, this, this.texture, this.guiLeft, this.guiTop, mouseX, mouseY, partialTicks);
        if(this.energyBar != null){
            IEnergyStorage energy = this.tile.getEnergy(EnumFacing.NORTH);
            if(energy != null && energy.getMaxEnergyStored() > 0){
                this.energyBar.draw(energy.getEnergyStored(), energy.getMaxEnergyStored(), -1);
            }
        }
        if(!this.fluidBars.isEmpty()) {
            this.fluidBars.forEach((side, fluidBar) -> {
                IFluidHandler fluid = this.tile.getTank(side);
                if (fluid instanceof CustomFluidTank && ((FluidTank) fluid).getCapacity() > 0) {
                    fluidBar.draw((CustomFluidTank) fluid);
                }
            });
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
        this.machineInterface.drawScreen(this.tile, this, this.guiLeft, this.guiTop, mouseX, mouseY, partialTicks);
        if(this.energyBar != null){
            IEnergyStorage energy = this.tile.getEnergy(EnumFacing.NORTH);
            if(energy != null && energy.getMaxEnergyStored() > 0){
                this.energyBar.mouseDrawTank(this, mouseX, mouseY, energy.getEnergyStored(), energy.getMaxEnergyStored(), tile.getCurrentEnergyUsage());
            }
        }
        if(!this.fluidBars.isEmpty()) {
            this.fluidBars.forEach((side, fluidBar) -> {
                IFluidHandler fluid = this.tile.getTank(side);
                if (fluid instanceof CustomFluidTank && ((FluidTank) fluid).getCapacity() > 0) {
                    fluidBar.drawMouseOver(this, mouseX, mouseY, (CustomFluidTank) fluid);
                }
            });
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

    @SuppressWarnings("UnusedReturnValue")
    public GuiMachine<T> setSize(int xSize, int ySize){
        this.xSize = xSize;
        this.ySize = ySize + (this.machineInterface.getInventoryLocation(this.tile, this.player) != null ? 85 : 0);
        return this;
    }

    public int getXSize(){
        return this.xSize;
    }

    public int getYSize(){
        return this.ySize - (this.machineInterface.getInventoryLocation(this.tile, this.player) != null ? 85 : 0);
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
