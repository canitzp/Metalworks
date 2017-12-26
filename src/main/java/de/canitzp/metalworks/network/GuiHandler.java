package de.canitzp.metalworks.network;

import de.canitzp.metalworks.gui.GuiMachine;
import de.canitzp.metalworks.inventory.ContainerMachine;
import de.canitzp.metalworks.machine.IMachineInterface;
import de.canitzp.metalworks.machine.TileBase;
import de.canitzp.metalworks.machine.blastfurnace.ContainerBlastFurnace;
import de.canitzp.metalworks.machine.blastfurnace.GuiBlastFurnace;
import de.canitzp.metalworks.machine.blastfurnace.TileBlastFurnace;
import de.canitzp.metalworks.machine.duster.ContainerDuster;
import de.canitzp.metalworks.machine.duster.GuiDuster;
import de.canitzp.metalworks.machine.duster.TileDuster;
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

    public static Map<Class<? extends TileBase>, Class<? extends IMachineInterface<? extends TileBase>>> interfaceMap = new HashMap<Class<? extends TileBase>, Class<? extends IMachineInterface<? extends TileBase>>>(){{

    }};

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        IMachineInterface<? extends TileBase> machineInterface = this.getMachineInterface(tile);
        if(machineInterface != null){
            return new ContainerMachine((TileBase) tile, machineInterface);
        }
        switch (ID){
            case 0: return new ContainerBlastFurnace(player, (TileBlastFurnace) tile);
            case 1: return new ContainerDuster(player, (TileDuster) tile);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        IMachineInterface<? extends TileBase> machineInterface = this.getMachineInterface(tile);
        if(machineInterface != null){
            return new GuiMachine((TileBase) tile, machineInterface, new ContainerMachine((TileBase) tile, machineInterface));
        }
        switch (ID){
            case 0: return new GuiBlastFurnace(player, (TileBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)));
            case 1: return new GuiDuster(player, (TileDuster) world.getTileEntity(new BlockPos(x, y, z)));
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
