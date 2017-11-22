package de.canitzp.simplesteel;

import de.canitzp.simplesteel.machine.blastfurnace.RecipeBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.TileBlastFurnace;
import de.canitzp.simplesteel.network.GuiHandler;
import de.canitzp.simplesteel.recipe.OreDictStack;
import de.canitzp.simplesteel.recipe.SimpleSteelRecipeHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
@Mod(modid = SimpleSteel.MODID, name = SimpleSteel.MODNAME, version = SimpleSteel.MODVERSION)
public class SimpleSteel {

    public static final String MODID = "simplesteel";
    public static final String MODNAME = "Simple Steel";
    public static final String MODVERSION = "@VERSION@";

    @Mod.Instance(MODID)
    public static SimpleSteel instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        OreDictionary.registerOre("ingotSteel", Registry.steelIngot);
        OreDictionary.registerOre("nuggetSteel", Registry.steelNugget);
        OreDictionary.registerOre("circuitBasic", Registry.controlCircuit);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        SimpleSteelRecipeHandler.addBlastFurnaceRecipe(new RecipeBlastFurnace(new OreDictStack("ingotIron").setStacksize(2), new OreDictStack(Items.COAL).setStacksize(3), OreDictStack.EMPTY, new ItemStack(Registry.steelIngot), ItemStack.EMPTY, 0, 400, -1));
        SimpleSteelRecipeHandler.addBlastFurnaceRecipe(new RecipeBlastFurnace(new OreDictStack("ingotIron").setStacksize(3), new OreDictStack("gemDiamond"), OreDictStack.EMPTY, new ItemStack(Registry.metalShielding, 4), new ItemStack(Items.IRON_NUGGET), 25, 250, -1));
    }

}
