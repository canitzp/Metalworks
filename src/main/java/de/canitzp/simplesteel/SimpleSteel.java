package de.canitzp.simplesteel;

import de.canitzp.simplesteel.block.BlockBase;
import de.canitzp.simplesteel.item.ItemBase;
import de.canitzp.simplesteel.machine.IMachineRecipe;
import de.canitzp.simplesteel.machine.blastfurnace.RecipeBlastFurnace;
import de.canitzp.simplesteel.machine.geothermalgenerator.geoburnable.IGeoburnable;
import de.canitzp.simplesteel.network.GuiHandler;
import de.canitzp.simplesteel.network.NetworkHandler;
import de.canitzp.simplesteel.recipe.OreDictStack;
import de.canitzp.simplesteel.recipe.SimpleSteelRecipeHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.List;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
@Mod(modid = SimpleSteel.MODID, name = SimpleSteel.MODNAME, version = SimpleSteel.MODVERSION)
public class SimpleSteel {

    public static final String MODID = "simplesteel";
    public static final String MODNAME = "Simple Steel";
    public static final String MODVERSION = "@VERSION@";

    public static final IForgeRegistry<IMachineRecipe> MACHINE_RECIPE_REGISTRY = new RegistryBuilder<IMachineRecipe>()
            .setName(new ResourceLocation(SimpleSteel.MODID, "machineRecipes")).setType(IMachineRecipe.class)
            .setMaxID(10000).disableSaving().allowModification().create();

    public static final IForgeRegistry<IGeoburnable> GEOBURNABLE_REGISTRY = new RegistryBuilder<IGeoburnable>()
            .setName(new ResourceLocation(SimpleSteel.MODID, "geoburnables")).setType(IGeoburnable.class)
            .setMaxID(10000).disableSaving().allowModification().create();

    @Mod.Instance(MODID)
    public static SimpleSteel instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        NetworkHandler.init(event);
        for(ItemBase item : ItemBase.ITEMS){
            if(item.hasOreNames()){
                for(String name : item.getOreNames()){
                    OreDictionary.registerOre(name, item);
                }
            }
        }
        for(BlockBase block : BlockBase.BLOCKS){
            if(block.hasOreNames()){
                for(String name : ((List<String>) block.getOreNames())){
                    OreDictionary.registerOre(name, block);
                }
            }
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        SimpleSteelRecipeHandler.addBlastFurnaceRecipe(new RecipeBlastFurnace(new OreDictStack("ingotIron").setStacksize(2), new OreDictStack(Items.COAL).setStacksize(3), OreDictStack.EMPTY, new ItemStack(Registry.steelIngot), ItemStack.EMPTY, 0, 400, -1));
        SimpleSteelRecipeHandler.addBlastFurnaceRecipe(new RecipeBlastFurnace(new OreDictStack("ingotIron").setStacksize(3), new OreDictStack("gemDiamond"), OreDictStack.EMPTY, new ItemStack(Registry.metalShielding, 4), new ItemStack(Items.IRON_NUGGET), 25, 250, -1));

    }

}
