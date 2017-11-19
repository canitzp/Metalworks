package de.canitzp.simplesteel.network;

import de.canitzp.simplesteel.machine.blastfurnace.ContainerBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.GuiBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.TileBlastFurnace;
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
        return new ContainerBlastFurnace(player, (TileBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)));
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiBlastFurnace(player, (TileBlastFurnace) world.getTileEntity(new BlockPos(x, y, z)));
    }

}
