package de.canitzp.simplesteel.machine.cable.basic;

import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class BlockCableBasic extends BlockContainer {

    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool STRAIGHT_NS = PropertyBool.create("straight_ns");

    public BlockCableBasic() {
        super(Material.IRON);
        this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, "cable_basic"));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setCreativeTab(Registry.TAB);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(EAST, false).withProperty(UP, false).withProperty(DOWN, false).withProperty(STRAIGHT_NS, false));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, SOUTH, WEST, EAST, UP, DOWN, STRAIGHT_NS);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean north = isSideValid(world, pos, EnumFacing.NORTH);
        boolean south = isSideValid(world, pos, EnumFacing.SOUTH);
        boolean west = isSideValid(world, pos, EnumFacing.WEST);
        boolean east = isSideValid(world, pos, EnumFacing.EAST);
        boolean up = isSideValid(world, pos, EnumFacing.UP);
        boolean down = isSideValid(world, pos, EnumFacing.DOWN);
        boolean straight_ns = north && south && !west && !east && !down && !up;
        return super.getActualState(state, world, pos)
                .withProperty(NORTH, !straight_ns && north)
                .withProperty(SOUTH, !straight_ns && south)
                .withProperty(WEST, east)
                .withProperty(EAST, west)
                .withProperty(UP, up)
                .withProperty(DOWN, down)
                .withProperty(STRAIGHT_NS, straight_ns);
    }

    private boolean isSideValid(IBlockAccess world, BlockPos pos, EnumFacing side){
        TileEntity tile = world.getTileEntity(pos.offset(side));
        if(tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side)){
            IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side);
            return storage != null && (storage.canReceive() || storage.canExtract());
        }
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = getActualState(state, source, pos);
        AxisAlignedBB def = new AxisAlignedBB(6.5/16, 6.5/16, 6.5/16, 9.5/16, 9.5/16, 9.5/16);
        if(state.getValue(NORTH) || state.getValue(STRAIGHT_NS)){
            def = def.union(new AxisAlignedBB(6.5/16, 6.5/16, 0, 9.5/16, 9.5/16, 6.5/16));
        }
        if(state.getValue(SOUTH) || state.getValue(STRAIGHT_NS)){
            def = def.union(new AxisAlignedBB(6.5/16, 6.5/16, 9.5/16, 9.5/16, 9.5/16, 1));
        }
        if(state.getValue(WEST)){
            def = def.union(new AxisAlignedBB(0, 6.5/16, 6.5/16, 6.5/16, 9.5/16, 9.5/16));
        }
        if(state.getValue(EAST)){
            def = def.union(new AxisAlignedBB(9.5/16, 6.5/16, 6.5/16, 1, 9.5/16, 9.5/16));
        }
        if(state.getValue(UP)){
            def = def.union(new AxisAlignedBB(6.5/16, 9.5/16, 6.5/16, 9.5/16, 1, 9.5/16));
        }
        if(state.getValue(DOWN)){
            def = def.union(new AxisAlignedBB(6.5/16, 0, 6.5/16, 9.5/16, 6.5/16, 9.5/16));
        }
        return def;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCableBasic();
    }

}
