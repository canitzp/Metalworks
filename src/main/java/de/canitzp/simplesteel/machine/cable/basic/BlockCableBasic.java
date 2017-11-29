package de.canitzp.simplesteel.machine.cable.basic;

import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import de.canitzp.simplesteel.machine.cable.Network;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public static final PropertyBool STRAIGHT_WE = PropertyBool.create("straight_we");
    public static final PropertyBool STRAIGHT_UD = PropertyBool.create("straight_ud");

    public BlockCableBasic() {
        super(Material.IRON);
        this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, "cable_basic"));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setCreativeTab(Registry.TAB);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false)
                .withProperty(SOUTH, false).withProperty(WEST, false)
                .withProperty(EAST, false).withProperty(UP, false)
                .withProperty(DOWN, false).withProperty(STRAIGHT_NS, false)
                .withProperty(STRAIGHT_WE, false).withProperty(STRAIGHT_UD, false));
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
        return new BlockStateContainer(this, NORTH, SOUTH, WEST, EAST, UP, DOWN, STRAIGHT_NS, STRAIGHT_WE, STRAIGHT_UD);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        List<EnumFacing> validSides = isSideValid(world, pos);
        boolean north = validSides.contains(EnumFacing.NORTH);
        boolean south = validSides.contains(EnumFacing.SOUTH);
        boolean west = validSides.contains(EnumFacing.WEST);
        boolean east = validSides.contains(EnumFacing.EAST);
        boolean up = validSides.contains(EnumFacing.UP);
        boolean down = validSides.contains(EnumFacing.DOWN);
        boolean straight_ns = north && south && !west && !east && !down && !up;
        boolean straight_we = !north && !south && west && east && !down && !up;
        boolean straight_ud = !north && !south && !west && !east && down && up;
        return state
                .withProperty(NORTH, !straight_ns && north).withProperty(SOUTH, !straight_ns && south)
                .withProperty(WEST, !straight_we && east).withProperty(EAST, !straight_we && west)
                .withProperty(UP, !straight_ud && up).withProperty(DOWN, !straight_ud && down)
                .withProperty(STRAIGHT_NS, straight_ns).withProperty(STRAIGHT_WE, straight_we)
                .withProperty(STRAIGHT_UD, straight_ud);
    }

    private List<EnumFacing> isSideValid(IBlockAccess world, BlockPos pos){
        List<EnumFacing> sides = new ArrayList<>();
        for(EnumFacing side : EnumFacing.values()){
            TileEntity tile = world.getTileEntity(pos.offset(side));
            if(tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side)){
                IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side);
                if(storage != null && (storage.canReceive() || storage.canExtract())){
                    sides.add(side);
                }
            }
        }
        return sides;
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
        if(state.getValue(EAST) || state.getValue(STRAIGHT_WE)){
            def = def.union(new AxisAlignedBB(0, 6.5/16, 6.5/16, 6.5/16, 9.5/16, 9.5/16));
        }
        if(state.getValue(WEST) || state.getValue(STRAIGHT_WE)){
            def = def.union(new AxisAlignedBB(9.5/16, 6.5/16, 6.5/16, 1, 9.5/16, 9.5/16));
        }
        if(state.getValue(UP) || state.getValue(STRAIGHT_UD)){
            def = def.union(new AxisAlignedBB(6.5/16, 9.5/16, 6.5/16, 9.5/16, 1, 9.5/16));
        }
        if(state.getValue(DOWN) || state.getValue(STRAIGHT_UD)){
            def = def.union(new AxisAlignedBB(6.5/16, 0, 6.5/16, 9.5/16, 6.5/16, 9.5/16));
        }
        return def;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.onUpdate(world, pos);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        this.onUpdate(world, pos);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCableBasic();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        if(!world.isRemote){
            for(EnumFacing side : EnumFacing.values()){
                TileEntity tile = world.getTileEntity(pos.offset(side));
                if(tile != null && tile instanceof TileCableBasic){
                    ((TileCableBasic) tile).network = new Network(world);
                    ((TileCableBasic) tile).network.searchFor(pos.offset(side));
                }
            }
        }
    }

    public void onUpdate(IBlockAccess world, BlockPos pos){
        if(world instanceof World && !((World) world).isRemote){
            TileEntity tile = world.getTileEntity(pos);
            if(tile != null && tile instanceof TileCableBasic){
                ((TileCableBasic) tile).network.searchFor(pos);
            }
        }
    }
}
