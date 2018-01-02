package de.canitzp.metalworks.block;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Props;
import de.canitzp.metalworks.item.ItemBlockEnergetic;
import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.TileBase;
import de.canitzp.metalworks.machine.blastfurnace.TileBlastFurnace;
import de.canitzp.metalworks.network.GuiHandler;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * @author canitzp
 */
public abstract class BlockContainerBase<T extends BlockContainerBase<T>> extends BlockBase<T> implements ITileEntityProvider{

    private boolean isActivatable = this.getDefaultState().getPropertyKeys().contains(Props.ACTIVE);
    private boolean hasMachineInterface;
    private boolean energeticItemBlock;
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
            GameRegistry.registerTileEntity(this.getTileEntityClass(), this.getRegistryName().toString());
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
        return this.energeticItemBlock ? new ItemBlockEnergetic(this, this.capacity, this.maxReceive, this.maxExtract) : super.getItem();
    }

    protected T setEnergeticItem(int capacity, int maxReceive, int maxExtract){
        this.energeticItemBlock = true;
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
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
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileBase){
            ((TileBase) tile).breakBlock();
        }
        super.breakBlock(world, pos, state);
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
            player.addStat(StatList.getBlockStats(this));
            player.addExhaustion(0.005F);
            if (this.canSilkHarvest(world, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
                java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
                ItemStack itemstack = this.getSilkTouchDrop(state);

                if (!itemstack.isEmpty()) {
                    items.add(itemstack);
                }

                net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, world, pos, state, 0, 1.0f, true, player);
                for (ItemStack item : items) {
                    spawnAsEntity(world, pos, item);
                }
            }
            else {
                harvesters.set(player);
                int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
                NonNullList<ItemStack> drops = NonNullList.create();
                this.getDrops(drops, world, pos, state, te, stack, i);
                drops.forEach(drop -> spawnAsEntity(world, pos, drop));
                harvesters.set(null);
            }
        }
    }

    public void getDrops(NonNullList<ItemStack> drops, World world, BlockPos pos, IBlockState state, TileEntity tile, ItemStack harvestStack, int fortune){
        Random rand = world.rand;
        int count = quantityDropped(state, fortune, rand);
        for (int i = 0; i < count; i++) {
            Item item = this.getItemDropped(state, rand, fortune);
            if (item != Items.AIR) {
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
        if(this.hasMachineInterface){
            if(!world.isRemote){
                this.openGui(player, pos);
            }
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("TileData", Constants.NBT.TAG_COMPOUND) && this.energeticItemBlock){
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileBase){
                ((TileBase) tile).readNBT(stack.getTagCompound().getCompoundTag("TileData"), TileBase.NBTType.DROP);
            }
        }
    }

    public void openGui(EntityPlayer player, BlockPos pos){
        player.openGui(Metalworks.instance, -1, player.world, pos.getX(), pos.getY(), pos.getZ());
    }
}
