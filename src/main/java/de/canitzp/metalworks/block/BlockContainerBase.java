package de.canitzp.metalworks.block;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Props;
import de.canitzp.metalworks.Util;
import de.canitzp.metalworks.item.ItemMachineBlock;
import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.TileBase;
import de.canitzp.metalworks.network.GuiHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @author canitzp
 */
@SuppressWarnings({"deprecation", "unchecked", "SameParameterValue", "UnusedReturnValue"})
public abstract class BlockContainerBase<T extends BlockContainerBase<T>> extends BlockBase<T> implements ITileEntityProvider{

    private final boolean isActivatable = this.getDefaultState().getPropertyKeys().contains(Props.ACTIVE);
    private boolean hasMachineInterface;
    private boolean energeticItemBlock;
    private boolean rightClickChecks = true;
    private boolean hasMachineItemBlock;
    private int capacity, maxReceive, maxExtract;

    public BlockContainerBase(Material material, MapColor color, String name) {
        super(material, color, name);
        this.hasTileEntity = true;
    }

    public BlockContainerBase(Material material, String name) {
        this(material, material.getMaterialMapColor(), name);
    }

    @Override
    public T register() {
        if(shouldRegister()){
            GameRegistry.registerTileEntity(this.getTileEntityClass(), Objects.requireNonNull(this.getRegistryName()).toString());
        }
        return super.register();
    }

    public T addInterface(Class<? extends IMachineInterface<? extends TileBase>> machineInterface){
        GuiHandler.interfaceMap.put(this.getTileEntityClass(), machineInterface);
        this.hasMachineInterface = true;
        return (T) this;
    }

    @Override
    protected ItemBlock getItem() {
        return this.hasMachineItemBlock ? new ItemMachineBlock(this) : super.getItem();
    }

    protected T setMachineItemBlock(boolean b){
        this.hasMachineItemBlock = b;
        return (T) this;
    }

    protected T setRightClickChecks(boolean b){
        this.rightClickChecks = b;
        return (T) this;
    }

    public List<EnumFacing> getSidesToShowInTooltip(){
        return Collections.emptyList();
    }

    protected abstract Class<? extends TileBase> getTileEntityClass();

    @Nullable
    @Override
    public final TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        try {
            return this.getTileEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Called server side after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if(!this.hasMachineItemBlock){
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileBase){
                ((TileBase) tile).breakBlock();
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune){
        Random rand = new Random();
        int count = quantityDropped(state, fortune, rand);
        for (int i = 0; i < count; i++) {
            Item item = this.getItemDropped(state, rand, fortune);
            if (item != Items.AIR) {
                TileEntity tile = world.getTileEntity(pos);
                NBTTagCompound nbt = new NBTTagCompound();
                if (tile instanceof TileBase) {
                    NBTTagCompound tileData = new NBTTagCompound();
                    ((TileBase) tile).writeNBT(tileData, TileBase.NBTType.DROP);
                    nbt.setTag("TileData", tileData);
                }
                ItemStack stack = new ItemStack(item, 1, this.damageDropped(state), nbt);
                stack.setTagCompound(nbt);

                drops.add(stack);
            }
        }
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(@Nonnull World worldIn, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
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

    @Nonnull
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
        if(rightClickChecks){
            if (!world.isRemote) {
                if (!Util.tryToBucketOrUnbucketAFluid(world, pos, state, player, hand, facing, hitX, hitY, hitZ)) {
                    if (this.hasMachineInterface) {
                        this.openGui(player, pos);
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("TileData", Constants.NBT.TAG_COMPOUND) && this.hasMachineItemBlock){
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileBase){
                ((TileBase) tile).readNBT(stack.getTagCompound().getCompoundTag("TileData"), TileBase.NBTType.DROP);
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        ItemStack def = super.getPickBlock(state, target, world, pos, player);
        TileEntity tile = world.getTileEntity(pos);
        NBTTagCompound nbt = new NBTTagCompound();
        if (tile instanceof TileBase && this.hasMachineItemBlock) {
            NBTTagCompound tileData = new NBTTagCompound();
            ((TileBase) tile).writeNBT(tileData, TileBase.NBTType.DROP);
            nbt.setTag("TileData", tileData);
            def.setTagCompound(nbt);
        }
        return def;
    }

    public void openGui(EntityPlayer player, BlockPos pos){
        player.openGui(Metalworks.instance, -1, player.world, pos.getX(), pos.getY(), pos.getZ());
    }

}
