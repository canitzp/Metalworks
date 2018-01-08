package de.canitzp.metalworks;

import de.canitzp.metalworks.block.BlockBase;
import de.canitzp.metalworks.item.ItemBase;
import de.canitzp.metalworks.machine.IMachineRecipe;
import de.canitzp.metalworks.machine.blastfurnace.RecipeBlastFurnace;
import de.canitzp.metalworks.machine.crusher.RecipeCrusher;
import de.canitzp.metalworks.machine.geothermalgenerator.geoburnable.IGeoburnable;
import de.canitzp.metalworks.network.GuiHandler;
import de.canitzp.metalworks.network.NetworkHandler;
import de.canitzp.metalworks.recipe.OreDictStack;
import de.canitzp.metalworks.recipe.SimpleSteelRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.List;
import java.util.Objects;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
@Mod(modid = Metalworks.MODID, name = Metalworks.MODNAME, version = Metalworks.MODVERSION)
public class Metalworks {

    public static final String MODID = "metalworks";
    public static final String MODNAME = "Metalworks";
    public static final String MODVERSION = "@VERSION@";

    public static final IForgeRegistry<IMachineRecipe> MACHINE_RECIPE_REGISTRY = new RegistryBuilder<IMachineRecipe>()
            .setName(new ResourceLocation(Metalworks.MODID, "machineRecipes")).setType(IMachineRecipe.class)
            .setMaxID(10000).disableSaving().allowModification().create();

    public static final IForgeRegistry<IGeoburnable> GEOBURNABLE_REGISTRY = new RegistryBuilder<IGeoburnable>()
            .setName(new ResourceLocation(Metalworks.MODID, "geoburnables")).setType(IGeoburnable.class)
            .setMaxID(10000).disableSaving().allowModification().create();

    @Mod.Instance(MODID)
    public static Metalworks instance;

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
        if(event.getSide().isClient()){
            for(ItemBase item : ItemBase.ITEMS){
                item.registerColorHandler(Minecraft.getMinecraft().getItemColors());
            }
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        SimpleSteelRecipeHandler.addBlastFurnaceRecipe(new RecipeBlastFurnace(new OreDictStack("ingotIron").setStacksize(2), new OreDictStack(Items.COAL).setStacksize(3), OreDictStack.EMPTY, new ItemStack(Registry.steelIngot), ItemStack.EMPTY, 0, 400, -1));
        SimpleSteelRecipeHandler.addBlastFurnaceRecipe(new RecipeBlastFurnace(new OreDictStack("ingotIron").setStacksize(3), new OreDictStack("gemDiamond"), OreDictStack.EMPTY, new ItemStack(Registry.metalShielding, 4), new ItemStack(Items.IRON_NUGGET), 25, 250, -1));

        // Generic Crusher
        for(String name : OreDictionary.getOreNames()){
            if(name.startsWith("ore")){
                String dustName = name.replaceFirst("ore", "dust");
                NonNullList<ItemStack> oreEntries = OreDictionary.getOres(dustName);
                if(!oreEntries.isEmpty()){
                    ItemStack dust = oreEntries.get(0).copy();
                    dust.setCount(2);
                    MACHINE_RECIPE_REGISTRY.register(new RecipeCrusher(name + "_" + dustName,
                            new OreDictStack(name), dust, ItemStack.EMPTY, 0, 200, 0));
                }
            } else if(name.startsWith("ingot")){
                String dustName = name.replaceFirst("ingot", "dust");
                NonNullList<ItemStack> oreEntries = OreDictionary.getOres(dustName);
                if(!oreEntries.isEmpty()) {
                    ItemStack dust = oreEntries.get(0).copy();
                    dust.setCount(1);
                    MACHINE_RECIPE_REGISTRY.register(new RecipeCrusher(name + "_" + dustName,
                            new OreDictStack(name), dust, ItemStack.EMPTY, 0, 150, 0));
                }
            }
        }
    }

    @SubscribeEvent
    public static void mappingBlocks(RegistryEvent.MissingMappings<Block> map){
        for(RegistryEvent.MissingMappings.Mapping<Block> mapping : map.getAllMappings()){
            ResourceLocation targetName = mapping.key;
            if(targetName != null && Objects.equals(targetName.getResourceDomain(), "simplesteel")){
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(MODID, targetName.getResourcePath()));
                if(block != null){
                    mapping.remap(block);
                }
            }
        }
    }

    @SubscribeEvent
    public static void mappingItems(RegistryEvent.MissingMappings<Item> map){
        for(RegistryEvent.MissingMappings.Mapping<Item> mapping : map.getAllMappings()){
            ResourceLocation targetName = mapping.key;
            if(targetName != null && Objects.equals(targetName.getResourceDomain(), "simplesteel")){
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MODID, targetName.getResourcePath()));
                if(item != null){
                    mapping.remap(item);
                }
            }
        }
    }

}
