package de.canitzp.metalworks.machine.supercharger;

import de.canitzp.metalworks.Props;
import de.canitzp.metalworks.block.BlockContainerBase;
import de.canitzp.metalworks.client.renderer.RenderSuperCharger;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author canitzp
 */
@SuppressWarnings("deprecation")
public class BlockSuperCharger extends BlockContainerBase<BlockSuperCharger> {

    public static final PropertyInteger STATE = PropertyInteger.create("charging_state", 0, 10);
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST);

    public BlockSuperCharger() {
        super(Material.IRON, "super_charger");
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(4.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(STATE, 0).withProperty(Props.ACTIVE, false));
        this.setMachineItemBlock(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        super.registerClient();
        ClientRegistry.bindTileEntitySpecialRenderer(TileSuperCharger.class, new RenderSuperCharger());
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(3 & meta)).withProperty(Props.ACTIVE, (meta & 4) == 4).withProperty(STATE, 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(Props.ACTIVE) ? 4 : 0);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STATE, FACING, Props.ACTIVE);
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    protected Class<? extends TileBase> getTileEntityClass() {
        return TileSuperCharger.class;
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSuperCharger) {
            state = state.withProperty(STATE, ((TileSuperCharger) tile).getChargingState());
        }
        return super.getActualState(state, world, pos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if(!stack.isEmpty()) {
            if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
                if (!world.isRemote) {
                    TileEntity tile = world.getTileEntity(pos);
                    if (tile instanceof TileSuperCharger) {
                        if (Objects.requireNonNull(((TileSuperCharger) tile).getInventory(facing)).getStackInSlot(0).isEmpty()) {
                            player.setHeldItem(hand, Objects.requireNonNull(((TileSuperCharger) tile).getInventory(facing)).insertItem(0, stack, false));
                        }
                        ((TileSuperCharger) tile).syncToClients();
                    }
                }
                return true;
            }
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileSuperCharger) {
                if (!Objects.requireNonNull(((TileSuperCharger) tile).getInventory(facing)).getStackInSlot(0).isEmpty()) {
                    if (!world.isRemote) {
                        player.setHeldItem(hand, Objects.requireNonNull(((TileSuperCharger) tile).getInventory(facing)).getStackInSlot(0));
                        ((IItemHandlerModifiable) Objects.requireNonNull(((TileSuperCharger) tile).getInventory(facing))).setStackInSlot(0, ItemStack.EMPTY);
                        ((TileSuperCharger) tile).syncToClients();
                    }
                    return true;
                }
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

}
