package de.canitzp.metalworks.block;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Props;
import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.TileBase;
import de.canitzp.metalworks.network.GuiHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public abstract class BlockContainerBase<T extends BlockContainerBase<T>> extends BlockBase<T> implements ITileEntityProvider{

    private boolean isActivatable = this.getDefaultState().getPropertyKeys().contains(Props.ACTIVE);
    private boolean hasMachineInterface;

    public BlockContainerBase(Material material, MapColor color, String name) {
        super(material, color, name);
        this.hasTileEntity = true;
    }

    public BlockContainerBase(Material material, String name) {
        this(material, material.getMaterialMapColor(), name);
    }

    @Override
    public T register() {
        GameRegistry.registerTileEntity(this.getTileEntityClass(), this.getRegistryName().toString());
        return super.register();
    }

    public T addInterface(Class<? extends IMachineInterface<? extends TileBase>> machineInterface){
        GuiHandler.interfaceMap.put(this.getTileEntityClass(), machineInterface);
        this.hasMachineInterface = true;
        return (T) this;
    }

    protected abstract Class<? extends TileBase> getTileEntityClass();

    @Nullable
    @Override
    public final TileEntity createNewTileEntity(World worldIn, int meta) {
        try {
            return this.getTileEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileBase){
            ((TileBase) tile).breakBlock();
        }
        world.removeTileEntity(pos);
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof IWorldNameable && ((IWorldNameable)te).hasCustomName()) {
            player.addStat(StatList.getBlockStats(this));
            player.addExhaustion(0.005F);
            if(!world.isRemote){
                ItemStack itemStack = new ItemStack(this.getItemDropped(state, world.rand, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)), this.quantityDropped(world.rand));
                if(!itemStack.isEmpty()){
                    itemStack.setStackDisplayName(((IWorldNameable)te).getName());
                    spawnAsEntity(world, pos, itemStack);
                }
            }
        }
        else {
            super.harvestBlock(world, player, pos, state, null, stack);
        }
    }

    /**
     * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On
     * the Server, this may perform additional changes to the world, like pistons replacing the block with an extended
     * base. On the client, the update may involve replacing tile entities or effects such as sounds or particles
     */
    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        if(this.isActivatable){
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileBase){
                state = state.withProperty(Props.ACTIVE, ((TileBase) tile).isWorking());
            }
        }
        return state;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && this.hasMachineInterface){
            this.openGui(player, pos);
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    public void openGui(EntityPlayer player, BlockPos pos){
        player.openGui(Metalworks.instance, -1, player.world, pos.getX(), pos.getY(), pos.getZ());
    }
}
