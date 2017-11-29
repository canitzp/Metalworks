package de.canitzp.simplesteel;

import de.canitzp.simplesteel.item.*;
import de.canitzp.simplesteel.machine.blastfurnace.BlockBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.TileBlastFurnace;
import de.canitzp.simplesteel.machine.cable.basic.BlockCableBasic;
import de.canitzp.simplesteel.machine.cable.basic.TileCableBasic;
import de.canitzp.simplesteel.machine.photovoltaicpanel.BlockPhotovoltaicPanel;
import de.canitzp.simplesteel.machine.photovoltaicpanel.TilePhotovoltaicPanel;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    public static BlockBlastFurnace blastFurnace = new BlockBlastFurnace();
    public static BlockPhotovoltaicPanel photovoltaicPanel = new BlockPhotovoltaicPanel();
    public static BlockCableBasic cableBasic = new BlockCableBasic();

    public static Item steelIngot = new ItemBase("steel_ingot"){
        @Override
        public float getSmeltingExperience(ItemStack item) {
            return 1.0F;
        }
    }.register();
    public static Item steelNugget = new ItemBase("steel_nugget").register();
    public static Item metalShielding = new ItemBase("metal_shielding").register();
    public static Item controlCircuit = new ItemBase("control_circuit").register();
    public static Item batteryLowDensity = new ItemBattery("battery_low_density", 25000).register();
    public static Item photovoltaicCell = new ItemBase("photovoltaic_cell").register();
    public static Item ironEnrichedSandDust = new ItemBase("iron_enriched_sand_dust").register();
    public static ItemToolCollection steelTools = new ItemToolCollection(MATERIAL_STEEL);
    public static ItemArmorCollection steelArmor = new ItemArmorCollection(ARMOR_STEEL);

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        IForgeRegistry<Block> reg = event.getRegistry();
        reg.register(blastFurnace);
        reg.register(photovoltaicPanel);
        reg.register(cableBasic);
        GameRegistry.registerTileEntity(TileBlastFurnace.class, SimpleSteel.MODID + ":blast_furnace");
        GameRegistry.registerTileEntity(TilePhotovoltaicPanel.class, SimpleSteel.MODID + ":photovoltaic_panel");
        GameRegistry.registerTileEntity(TileCableBasic.class, SimpleSteel.MODID + ":cable_basic");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        IForgeRegistry<Item> reg = event.getRegistry();
        reg.register(new ItemBlock(blastFurnace).setRegistryName(blastFurnace.getRegistryName()));
        reg.register(new ItemBlock(photovoltaicPanel).setRegistryName(photovoltaicPanel.getRegistryName()));
        reg.register(new ItemBlock(cableBasic).setRegistryName(cableBasic.getRegistryName()));

        for(ItemBase item : ItemBase.ITEMS){
            reg.register(item);
        }

        steelTools.register(reg);
        steelArmor.register(reg);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event){
        RECIPES.put(new ResourceLocation(SimpleSteel.MODID, "steel_nugget_1"),
                new ShapelessOreRecipe(null, NonNullList.withSize(1, new OreIngredient("ingotSteel")), new ItemStack(steelNugget, 9)));
        RECIPES.put(new ResourceLocation(SimpleSteel.MODID, "steel_ingot_1"),
                new ShapedOreRecipe(null, steelIngot, "###", "###", "###", '#', "nuggetSteel"));
        RECIPES.put(new ResourceLocation(SimpleSteel.MODID, "metal_shielding_1"),
                new ShapedOreRecipe(null, new ItemStack(metalShielding, 4), " # ", "#d#", " # ", '#', "ingotIron", 'd', "gemDiamond"));
        RECIPES.put(new ResourceLocation(SimpleSteel.MODID, "control_circuit_1"),
                new ShapedOreRecipe(null, controlCircuit, "ggg", "rsr", "nnn", 'g', "dyeGreen", 'r', "dustRedstone", 's', "dustGlowstone", 'n', "nuggetGold"));
        RECIPES.put(new ResourceLocation(SimpleSteel.MODID, "blast_furnace_1"),
                new ShapedOreRecipe(null, blastFurnace, "mmm", "fcf", "mmm", 'm', metalShielding, 'f', Blocks.FURNACE, 'c', "circuitBasic"));
        steelTools.registerRecipes(RECIPES, "ingotSteel");
        steelArmor.registerRecipes(RECIPES, "ingotSteel");

        for(Map.Entry<ResourceLocation, IRecipe> recipes : RECIPES.entrySet()){
            event.getRegistry().register(recipes.getValue().setRegistryName(recipes.getKey()));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event){
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blastFurnace), 0, new ModelResourceLocation(blastFurnace.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(photovoltaicPanel), 0, new ModelResourceLocation(photovoltaicPanel.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(cableBasic), 0, new ModelResourceLocation(cableBasic.getRegistryName(), "inventory"));

        for(ItemBase item : ItemBase.ITEMS){
            item.registerClient();
        }

        steelTools.bakeModels();
        steelArmor.bakeModels();

    }

}
