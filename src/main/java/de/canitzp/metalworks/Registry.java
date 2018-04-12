package de.canitzp.metalworks;

import de.canitzp.metalworks.block.BlockBase;
import de.canitzp.metalworks.block.cable.basic.BlockCableBasic;
import de.canitzp.metalworks.block.plant.papyrus.BlockPapyrus;
import de.canitzp.metalworks.config.ConfEnable;
import de.canitzp.metalworks.item.*;
import de.canitzp.metalworks.machine.IGeneratorFuel;
import de.canitzp.metalworks.machine.IMachineRecipe;
import de.canitzp.metalworks.machine.ItemBioGeneratorFuel;
import de.canitzp.metalworks.machine.battery.BlockBattery;
import de.canitzp.metalworks.machine.biogenerator.BlockBioGenerator;
import de.canitzp.metalworks.machine.blastfurnace.BlockBlastFurnace;
import de.canitzp.metalworks.machine.crusher.BlockCrusher;
import de.canitzp.metalworks.machine.crusher.RecipeCrusher;
import de.canitzp.metalworks.machine.duster.BlockDuster;
import de.canitzp.metalworks.machine.duster.RecipeDuster;
import de.canitzp.metalworks.machine.geothermalgenerator.BlockGeothermalGenerator;
import de.canitzp.metalworks.machine.geothermalgenerator.geoburnable.BlockGeoburnable;
import de.canitzp.metalworks.machine.geothermalgenerator.geoburnable.IGeoburnable;
import de.canitzp.metalworks.machine.photovoltaicpanel.BlockPhotovoltaicPanel;
import de.canitzp.metalworks.machine.supercharger.BlockSuperCharger;
import de.canitzp.metalworks.machine.tank.BlockTank;
import de.canitzp.metalworks.recipe.OreDictStack;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
public class Registry {

    public static final Map<ResourceLocation, IRecipe> RECIPES = new ConcurrentHashMap<>();

    public static final Item.ToolMaterial MATERIAL_STEEL = EnumHelper.addToolMaterial("steel", 2, 1024, 7.0F, 5.0F, 16);
    public static final ItemArmor.ArmorMaterial ARMOR_STEEL = EnumHelper.addArmorMaterial("steel", "steel", 20, new int[]{2, 5, 7, 3}, 16, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5F);
    public static final MapColor DEFAULT_COLOR = MapColor.BLACK_STAINED_HARDENED_CLAY;

    public static final CreativeTabs TAB = new CreativeTabs(Metalworks.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(blastFurnace);
        }
    };

    public static final Item steelIngot = new ItemBase("steel_ingot"){
        @Override
        public float getSmeltingExperience(ItemStack item) {
            return 1.0F;
        }
    }.addRecipes("XXX", "XXX", "XXX", 'X', "nuggetSteel").addOreName("ingotSteel").register();
    public static final Item steelNugget = new ItemBase("steel_nugget"){
        @Override
        public void registerRecipes(List<IRecipe> recipes) {
            recipes.add(new ShapelessOreRecipe(null, new ItemStack(steelNugget, 9), new OreIngredient("ingotSteel")));
        }
    }.addRecipes("###", "###", "###", '#', "nuggetSteel").addOreName("nuggetSteel").register();
    public static Item steelDust = new ItemDust("steel", 0x535559).register();
    public static final Item metalShielding = new ItemBase("metal_shielding"){
        @Override
        public void registerRecipes(List<IRecipe> recipes) {
            recipes.add(new ShapedOreRecipe(null, new ItemStack(metalShielding, 4), " # ", "#d#", " # ", '#', "ingotIron", 'd', "gemDiamond"));
        }
    }.register();
    public static final Item quartzSandDust = new ItemBase("quartz_sand_dust").addOreName("dustQuartzSand").register();
    public static Item controlCircuit = new ItemBase("control_circuit").addOreName("circuitBasic").addRecipes("ggg", "rsr", "nnn", 'g', "dyeGreen", 'r', "dustRedstone", 's', "dustGlowstone", 'n', "nuggetGold").register();
    public static final Item batteryLowDensity = new ItemBattery("battery_low_density", 25000).addRecipes(" i ", "srs", "sms", 'i', "nuggetIron", 's', "dustQuartzSand", 'r', "blockRedstone", 'm', metalShielding).register();
    public static final Item photovoltaicCell = new ItemBase("photovoltaic_cell").addRecipes("isi", "sqs", "isi", 'i', "ingotIron", 's', "dustQuartzSand", 'q', "gemQuartz").register();
    public static final Item bioWaste = new ItemBase("bio_waste").register();

    public static final BlockCableBasic cableBasic = new BlockCableBasic(){
        @Override
        public void registerRecipes(List<IRecipe> recipes) {
            recipes.add(new ShapedOreRecipe(null, new ItemStack(this, 8), " s ", "sws", " s ", 's', "dustQuartzSand", 'w', Blocks.WOOL));
        }
    }.register();
    public static final BlockBlastFurnace blastFurnace = new BlockBlastFurnace().setRegistrationParameter(ConfEnable.BLAST_FURNACE).addRecipes("mmm", "fcf", "mmm", 'm', metalShielding, 'f', Blocks.FURNACE, 'c', "circuitBasic").register();
    public static BlockPhotovoltaicPanel photovoltaicPanel = new BlockPhotovoltaicPanel().setRegistrationParameter(ConfEnable.PHOTOVOLTAIC_PANEL).addRecipes("ppp", "sis", "scs", 'p', photovoltaicCell, 'i', "blockIron", 'c', cableBasic, 's', metalShielding).register();
    public static final BlockDuster duster = new BlockDuster().setRegistrationParameter(ConfEnable.DUSTER).addRecipes("msm", "sis", "mcm", 'm', metalShielding, 's', "stone", 'i', "blockIron", 'c', "circuitBasic").register();
    public static final BlockGeothermalGenerator geothermalGenerator = new BlockGeothermalGenerator().setRegistrationParameter(ConfEnable.GEOTHERMAL_GENERATOR).addRecipes(" m ", "mim", "mcm", 'm', metalShielding, 'i', "blockIron", 'c', "circuitBasic").register();
    public static final BlockCrusher crusher = new BlockCrusher().setRegistrationParameter(ConfEnable.CRUSHER).addRecipes("mim", "ibi", "ici", 'm', metalShielding, 'i', "ingotIron", 'b',"blockIron", 'c', "circuitBasic").register();
    public static BlockSuperCharger superCharger = new BlockSuperCharger().setRegistrationParameter(ConfEnable.SUPER_CHARGER).addRecipes("mmm", "cbi", "mmm", 'm', metalShielding, 'c', cableBasic, 'i', "blockIron", 'b', "circuitBasic").register();
    public static BlockBattery lowDensity = new BlockBattery(new BlockBattery.Type("low", 100000, 5000)).addRecipes("ibi", "bbb", "iii", 'i', "ingotIron", 'b', batteryLowDensity).register();
    public static final BlockBioGenerator bioGenerator = new BlockBioGenerator().setRegistrationParameter(ConfEnable.BIO_GENERATOR).addRecipes("scs", "kgk", "scs", 's', "ingotSteel", 'c', "circuitBasic", 'k', cableBasic, 'g', "blockGlass").register();
    public static final BlockTank tank = new BlockTank().register();
    //public static final BlockPapyrus papyrus = new BlockPapyrus();

    public static final ItemToolCollection steelTools = new ItemToolCollection(MATERIAL_STEEL);
    public static final ItemArmorCollection steelArmor = new ItemArmorCollection(ARMOR_STEEL);

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        IForgeRegistry<Block> reg = event.getRegistry();

        BlockBase.BLOCKS.forEach(reg::register);

        //reg.register(papyrus);
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
                    RECIPES.put(new ResourceLocation(Metalworks.MODID, Objects.requireNonNull(item.getRegistryName()).getResourcePath() + "_" + i), itemRecipes.get(i));
                }
            }
        });

        BlockBase.BLOCKS.forEach(block -> {
            List<IRecipe> itemRecipes = new ArrayList<>();
            block.registerRecipes(itemRecipes);
            if(!itemRecipes.isEmpty()){
                for(int i = 0; i < itemRecipes.size(); i++){
                    RECIPES.put(new ResourceLocation(Metalworks.MODID, Objects.requireNonNull(block.getRegistryName()).getResourcePath() + "_" + i), itemRecipes.get(i));
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

        //Duster
        reg.register(new RecipeDuster("quartz_sand_dust", new OreDictStack("gemQuartz"), new OreDictStack("sand"), new ItemStack(quartzSandDust, 2), 0, 0));

        //Crusher
        reg.register(new RecipeCrusher("wool_string", new OreDictStack(new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)), new ItemStack(Items.STRING, 2), new ItemStack(Items.STRING), 50, 100, 50));
    }

    @SubscribeEvent
    public static void registerGeoBurnables(RegistryEvent.Register<IGeoburnable> event){
        IForgeRegistry<IGeoburnable> reg = event.getRegistry();
        reg.register(new BlockGeoburnable(Blocks.MAGMA, 40, 1));
        reg.register(new BlockGeoburnable(Blocks.FIRE, 80, 2){
            @Override
            public ItemStack getJEIIcon() {
                return new ItemStack(Items.FLINT_AND_STEEL);
            }
        });
        reg.register(new BlockGeoburnable(Blocks.LAVA, 160, 3){
            @Override
            public ItemStack getJEIIcon() {
                return FluidUtil.getFilledBucket(new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME));
            }
        });
    }

    @SubscribeEvent
    public static void registerGeneratorFuel(RegistryEvent.Register<IGeneratorFuel> event){
        ItemBioGeneratorFuel.registerDefaultRecipes(event.getRegistry());
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
