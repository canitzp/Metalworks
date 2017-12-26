package de.canitzp.metalworks.network;

import de.canitzp.metalworks.gui.GuiMachine;
import de.canitzp.metalworks.inventory.ContainerMachine;
import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class GuiHandler implements IGuiHandler {

    public static Map<Class<? extends TileBase>, Class<? extends IMachineInterface<? extends TileBase>>> interfaceMap = new HashMap<>();

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        IMachineInterface<? extends TileBase> machineInterface = this.getMachineInterface(tile);
        if(machineInterface != null){
            return new ContainerMachine(player, (TileBase) tile, machineInterface);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        IMachineInterface<? extends TileBase> machineInterface = this.getMachineInterface(tile);
        if(machineInterface != null){
            return new GuiMachine(player, (TileBase) tile, machineInterface, new ContainerMachine(player, (TileBase) tile, machineInterface));
        }
        return null;
    }

    private IMachineInterface<? extends TileBase> getMachineInterface(TileEntity tile){
        if(tile instanceof TileBase && interfaceMap.containsKey(tile.getClass())){
            try {
                return interfaceMap.get(tile.getClass()).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
