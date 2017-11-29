package de.canitzp.simplesteel.network;

import de.canitzp.simplesteel.SimpleSteel;
import de.canitzp.simplesteel.network.packet.PacketSyncTile;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author canitzp
 */
public class NetworkHandler {

    public static final SimpleNetworkWrapper NET = new SimpleNetworkWrapper(SimpleSteel.MODID);

    public static void init(FMLInitializationEvent event){
        NET.registerMessage(PacketSyncTile.class, PacketSyncTile.class, 0, Side.CLIENT);
    }

}
