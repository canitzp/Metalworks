package de.canitzp.simplesteel;

import de.canitzp.simplesteel.block.BlockBase;
import de.canitzp.simplesteel.item.*;
import de.canitzp.simplesteel.machine.IMachineRecipe;
import de.canitzp.simplesteel.machine.blastfurnace.BlockBlastFurnace;
import de.canitzp.simplesteel.block.cable.basic.BlockCableBasic;
import de.canitzp.simplesteel.machine.duster.BlockDuster;
import de.canitzp.simplesteel.machine.duster.RecipeDuster;
import de.canitzp.simplesteel.machine.geothermalgenerator.BlockGeothermalGenerator;
import de.canitzp.simplesteel.machine.geothermalgenerator.geoburnable.BlockGeoburnable;
import de.canitzp.simplesteel.machine.geothermalgenerator.geoburnable.IGeoburnable;
import de.canitzp.simplesteel.machine.photovoltaicpanel.BlockPhotovoltaicPanel;
import de.canitzp.simplesteel.recipe.OreDictStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
public class Registry {

    public static final Map<ResourceLocation, IRecipe> RECIPES = new ConcurrentHashMap<>();

    public static final Item.ToolMaterial MATERIAL_STEEL = EnumHelper.addToolMaterial("steel", 2, 1024, 7.0F, 5.0F, 16);
    public static final ItemArmor.ArmorMaterial ARMOR_STEEL = EnumHelper.addArmorMaterial("steel", "steel", 20, new int[]{2, 5, 7, 3}, 16, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5F);

    public static final CreativeTabs TAB = new CreativeTabs(SimpleSteel.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(blastFurnace);
        }
    };

    public static Item steelIngot = new ItemBase("steel_ingot"){
        @Override
        public float getSmeltingExperience(ItemStack item) {
            return 1.0F;
        }
    }.addRecipes("XXX", "XXX", "XXX", 'X', "nuggetSteel").addOreName("ingotSteel").register();
    public static Item steelNugget = new ItemBase("steel_nugget"){
        @Override
        public void registerRecipes(List<IRecipe> recipes) {
            recipes.add(new ShapelessOreRecipe(null, new ItemStack(steelNugget, 9), new OreIngredient("ingotSteel")));
        }
    }.addRecipes("###", "###", "###", '#', "nuggetSteel").addOreName("nuggetSteel").register();
    public static Item metalShielding = new ItemBase("metal_shielding"){
        @Override
        public void registerRecipes(List<IRecipe> recipes) {
            recipes.add(new ShapedOreRecipe(null, new ItemStack(metalShielding, 4), " # ", "#d#", " # ", '#', "ingotIron", 'd', "gemDiamond"));
        }
    }.register();
    public static Item quartzSandDust = new ItemBase("quartz_sand_dust").addOreName("dustQuartzSand").register();
    public static Item controlCircuit = new ItemBase("control_circuit").addOreName("circuitBasic").addRecipes("ggg", "rsr", "nnn", 'g', "dyeGreen", 'r', "dustRedstone", 's', "dustGlowstone", 'n', "nuggetGold").register();
    public static Item batteryLowDensity = new ItemBattery("battery_low_density", 25000).addRecipes(" i ", "srs", "sms", 'i', "nuggetIron", 's', quartzSandDust, 'r', "blockRedstone", 'm', metalShielding).register();
    public static Item photovoltaicCell = new ItemBase("photovoltaic_cell").addRecipes("isi", "sqs", "isi", 'i', "ingotIron", 's', quartzSandDust, 'q', "gemQuartz").register();

    public static BlockCableBasic cableBasic = new BlockCableBasic().register();
    public static BlockBlastFurnace blastFurnace = new BlockBlastFurnace().addRecipes("mmm", "fcf", "mmm", 'm', metalShielding, 'f', Blocks.FURNACE, 'c', "circuitBasic").register();
    public static BlockPhotovoltaicPanel photovoltaicPanel = new BlockPhotovoltaicPanel().addRecipes("ppp", "sis", "scs", 'p', photovoltaicCell, 'i', "blockIron", 'c', cableBasic, 's', metalShielding).register();
    public static BlockDuster duster = new BlockDuster().addRecipes("msm", "sis", "mcm", 'm', metalShielding, 's', "stone", 'i', "blockIron", 'c', "circuitBasic").register();
    public static BlockGeothermalGenerator geothermalGenerator = new BlockGeothermalGenerator().addRecipes(" m ", "mim", "mcm", 'm', metalShielding, 'i', "blockIron", 'c', "circuitBasic").register();

    public static ItemToolCollection steelTools = new ItemToolCollection(MATERIAL_STEEL);
    public static ItemArmorCollection steelArmor = new ItemArmorCollection(ARMOR_STEEL);

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        IForgeRegistry<Block> reg = event.getRegistry();

        BlockBase.BLOCKS.forEach(reg::register);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        IForgeRegistry<Item> reg = event.getRegistry();

        ItemBase.ITEMS.forEach(reg::register);
        BlockBase.BLOCKS.stream().filter(blockBase -> blockBase.item != null).forEach(blockBase -> reg.register(blockBase.item));

        steelTools.register(reg);
        steelArmor.register(reg);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event){
        steelTools.registerRecipes(RECIPES, "ingotSteel");
        steelArmor.registerRecipes(RECIPES, "ingotSteel");

        ItemBase.ITEMS.forEach(item -> {
            List<IRecipe> itemRecipes = new ArrayList<>();
            item.registerRecipes(itemRecipes);
            if(!itemRecipes.isEmpty()){
                for(int i = 0; i < itemRecipes.size(); i++){
                    RECIPES.put(new ResourceLocation(SimpleSteel.MODID, item.getRegistryName().getResourcePath() + "_" + i), itemRecipes.get(i));
                }
            }
        });

        BlockBase.BLOCKS.forEach(block -> {
            List<IRecipe> itemRecipes = new ArrayList<>();
            block.registerRecipes(itemRecipes);
            if(!itemRecipes.isEmpty()){
                for(int i = 0; i < itemRecipes.size(); i++){
                    RECIPES.put(new ResourceLocation(SimpleSteel.MODID, block.getRegistryName().getResourcePath() + "_" + i), itemRecipes.get(i));
                }
            }
        });

        for(Map.Entry<ResourceLocation, IRecipe> recipes : RECIPES.entrySet()){
            event.getRegistry().register(recipes.getValue().setRegistryName(recipes.getKey()));
        }
    }

    @SubscribeEvent
    public static void registerMachineRecipes(RegistryEvent.Register<IMachineRecipe> event){
        IForgeRegistry<IMachineRecipe> reg = event.getRegistry();
        reg.register(new RecipeDuster("quartz_sand_dust", new OreDictStack("gemQuartz"), new OreDictStack("sand"), new ItemStack(quartzSandDust, 2), 200, -1));
    }

    @SubscribeEvent
    public static void registerGeoBurnables(RegistryEvent.Register<IGeoburnable> event){
        IForgeRegistry<IGeoburnable> reg = event.getRegistry();
        reg.register(new BlockGeoburnable(Blocks.MAGMA, 50, 1));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ItemBase.ITEMS.forEach(ItemBase::registerClient);
        BlockBase.BLOCKS.forEach(BlockBase::registerClient);

        steelTools.bakeModels();
        steelArmor.bakeModels();
    }

}
