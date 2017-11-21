package de.canitzp.simplesteel;

import de.canitzp.simplesteel.item.ItemArmorCollection;
import de.canitzp.simplesteel.item.ItemToolCollection;
import de.canitzp.simplesteel.machine.blastfurnace.BlockBlastFurnace;
import de.canitzp.simplesteel.machine.blastfurnace.TileBlastFurnace;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
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

    public static Item steelIngot = new Item(){{
        setRegistryName(new ResourceLocation(SimpleSteel.MODID, "steel_ingot"));
        setUnlocalizedName(this.getRegistryName().toString());
        setCreativeTab(TAB);
    }
        @Override
        public float getSmeltingExperience(ItemStack item) {
            return 1.0F;
        }
    };

    public static Item steelNugget = new Item(){{
        setRegistryName(new ResourceLocation(SimpleSteel.MODID, "steel_nugget"));
        setUnlocalizedName(this.getRegistryName().toString());
        setCreativeTab(TAB);
    }};

    public static Item metalShielding = new Item(){{
        setRegistryName(new ResourceLocation(SimpleSteel.MODID, "metal_shielding"));
        setUnlocalizedName(this.getRegistryName().toString());
        setCreativeTab(TAB);
    }};

    public static Item controlCircuit = new Item(){{
        setRegistryName(new ResourceLocation(SimpleSteel.MODID, "control_circuit"));
        setUnlocalizedName(this.getRegistryName().toString());
        setCreativeTab(TAB);
    }};

    public static ItemToolCollection steelTools = new ItemToolCollection(MATERIAL_STEEL);
    public static ItemArmorCollection steelArmor = new ItemArmorCollection(ARMOR_STEEL);

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        IForgeRegistry<Block> reg = event.getRegistry();
        reg.register(blastFurnace);
        GameRegistry.registerTileEntity(TileBlastFurnace.class, SimpleSteel.MODID + ":blast_furnace");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        IForgeRegistry<Item> reg = event.getRegistry();
        reg.register(new ItemBlock(blastFurnace).setRegistryName(blastFurnace.getRegistryName()));

        reg.register(steelIngot);
        reg.register(steelNugget);
        reg.register(metalShielding);
        reg.register(controlCircuit);
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
        ModelLoader.setCustomModelResourceLocation(steelIngot, 0, new ModelResourceLocation(steelIngot.getRegistryName(), "invenory"));
        ModelLoader.setCustomModelResourceLocation(steelNugget, 0, new ModelResourceLocation(steelNugget.getRegistryName(), "invenory"));
        ModelLoader.setCustomModelResourceLocation(metalShielding, 0, new ModelResourceLocation(metalShielding.getRegistryName(), "invenory"));
        ModelLoader.setCustomModelResourceLocation(controlCircuit, 0, new ModelResourceLocation(controlCircuit.getRegistryName(), "invenory"));
        steelTools.bakeModels();
        steelArmor.bakeModels();
    }

}
