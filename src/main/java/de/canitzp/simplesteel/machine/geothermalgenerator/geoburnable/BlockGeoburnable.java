package de.canitzp.simplesteel.machine.geothermalgenerator.geoburnable;

import de.canitzp.simplesteel.SimpleSteel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author canitzp
 */
public class BlockGeoburnable extends DefaultGeoBurnable {

    public IBlockState state;
    public boolean ignoreStates = false;
    public int energy = DEFAULT_ENERGY, burn = DEFAULT_BURN_TIME;
    private int cooldown = 0;

    public BlockGeoburnable(IBlockState state, int burnTime, int energy){
        this.state = state;
        this.energy = energy;
        this.burn = burnTime;
        this.cooldown = burnTime / 4;
        ResourceLocation stateLoc = new ResourceLocation(state.toString());
        this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, "geoburnable." + stateLoc.getResourceDomain() + "." + stateLoc.getResourcePath()));
    }

    public BlockGeoburnable(Block block, int burnTime, int energy){
        this(block.getDefaultState(), burnTime, energy);
        this.ignoreStates = true;
    }

    public BlockGeoburnable(IBlockState state, int burnTime){
        this(state, burnTime, 0);
    }

    public BlockGeoburnable(Block block, int burnTime){
        this(block, burnTime, 0);
    }

    public BlockGeoburnable(IBlockState state){
        this(state, DEFAULT_BURN_TIME, 0);
    }

    public BlockGeoburnable(Block block){
        this(block, DEFAULT_BURN_TIME, 0);
    }

    @Override
    public int getBurnTime(World world, BlockPos pos, IBlockState state, EnumFacing side) {
        return this.burn;
    }

    @Override
    public int getBurnEnergy(World world, BlockPos pos, IBlockState state, EnumFacing side) {
        return this.energy;
    }

    @Override
    public boolean isBurnable(IBlockState state, EnumFacing side) {
        return state.equals(this.state) || (this.ignoreStates && state.getBlock().equals(this.state.getBlock()));
    }

    @Override
    public int getCooldown(World world, BlockPos pos, IBlockState state, EnumFacing side) {
        return this.cooldown;
    }

    @Override
    public ItemStack getJEIIcon() {
        return new ItemStack(this.state.getBlock());
    }

    @Override
    public void drawJEIText(FontRenderer font) {
        font.drawString("Burn Time: " + this.burn, 21, -4, 0xFFFFFF, false);
        font.drawString("Cooldown : " + this.cooldown, 21, 6, 0xFFFFFF, false);
        font.drawString("Energy   : " + this.energy, 21, 16, 0xFFFFFF, false);
    }

    public BlockGeoburnable setCooldown(int cooldown){
        this.cooldown = cooldown;
        return this;
    }
}
