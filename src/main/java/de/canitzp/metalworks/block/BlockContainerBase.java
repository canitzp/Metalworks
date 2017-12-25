package de.canitzp.metalworks.block;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public abstract class BlockContainerBase<T extends BlockContainerBase<T>> extends BlockBase<T> implements ITileEntityProvider{

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

    protected abstract Class<? extends TileEntity> getTileEntityClass();

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
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
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
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

}
