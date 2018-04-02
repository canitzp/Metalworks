package de.canitzp.metalworks.network.packet;

import de.canitzp.metalworks.machine.TileBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

/**
 * @author canitzp
 */
public class PacketSyncTile implements IMessage, IMessageHandler<PacketSyncTile, IMessage> {

    private NBTTagCompound syncTag;
    private BlockPos pos;

    public PacketSyncTile(){}

    public PacketSyncTile(NBTTagCompound syncTag, BlockPos pos){
        this.syncTag = syncTag;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            this.syncTag = new PacketBuffer(buf).readCompoundTag();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        new PacketBuffer(buf).writeCompoundTag(this.syncTag);
        buf.writeLong(this.pos.toLong());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(PacketSyncTile message, MessageContext ctx) {
        if(message.syncTag != null){
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if(tile instanceof TileBase){
                    ((TileBase) tile).readNBT(message.syncTag, TileBase.NBTType.SYNC);
                    ((TileBase) tile).onSyncPacket();
                }
            });
        }
        return null;
    }

}
