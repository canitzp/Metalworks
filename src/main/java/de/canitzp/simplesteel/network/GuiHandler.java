package de.canitzp.simplesteel.network;

import de.canitzp.simplesteel.machine.blastfurnace.ContainerBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.GuiBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.TileBlastFurnace;
import de.canitzp.simplesteel.machine.duster.ContainerDuster;
import de.canitzp.simplesteel.machine.duster.GuiDuster;
import de.canitzp.simplesteel.machine.duster.TileDuster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class GuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0: return new ContainerBlastFurnace(player, (TileBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)));
            case 1: return new ContainerDuster(player, (TileDuster) world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0: return new GuiBlastFurnace(player, (TileBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)));
            case 1: return new GuiDuster(player, (TileDuster) world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }

}
