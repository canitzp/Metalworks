package de.canitzp.metalworks.network;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.network.packet.PacketSyncTile;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author canitzp
 */
public class NetworkHandler {

    public static final SimpleNetworkWrapper NET = new SimpleNetworkWrapper(Metalworks.MODID);

    public static void init(FMLInitializationEvent event){
        NET.registerMessage(PacketSyncTile.class, PacketSyncTile.class, 0, Side.CLIENT);
    }

}
