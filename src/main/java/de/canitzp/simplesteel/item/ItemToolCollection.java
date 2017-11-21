package de.canitzp.simplesteel.item;

import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;

/**
 * @author canitzp
 */
//TODO maybe add shears too? or fishing rod?
public class ItemToolCollection {

    private Item.ToolMaterial material;

    private ItemPickaxe pickaxe;
    private ItemSword sword;
    private ItemSpade shovel;
    private ItemAxe axe;

    public ItemToolCollection(Item.ToolMaterial material){
        this.material = material;
        this.pickaxe = new Pickaxe(material){{
            this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, material.name() + "_pickaxe"));
            this.setUnlocalizedName(this.getRegistryName().toString());
            this.setCreativeTab(Registry.TAB);
        }};
        this.sword = new ItemSword(material){{
           this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, material.name() + "_sword"));
           this.setUnlocalizedName(this.getRegistryName().toString());
           this.setCreativeTab(Registry.TAB);
        }};
        this.shovel = new ItemSpade(material){{
            this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, material.name() + "_shovel"));
            this.setUnlocalizedName(this.getRegistryName().toString());
            this.setCreativeTab(Registry.TAB);
        }};
        this.axe = new Axe(material){{
            this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, material.name() + "_axe"));
            this.setUnlocalizedName(this.getRegistryName().toString());
            this.setCreativeTab(Registry.TAB);
        }};
    }

    public void register(IForgeRegistry<Item> reg){
        reg.registerAll(pickaxe, shovel, sword, axe);
    }

    @SideOnly(Side.CLIENT)
    public void bakeModels(){
        ModelLoader.setCustomModelResourceLocation(pickaxe, 0, new ModelResourceLocation(pickaxe.getRegistryName(), "invenory"));
        ModelLoader.setCustomModelResourceLocation(shovel, 0, new ModelResourceLocation(shovel.getRegistryName(), "invenory"));
        ModelLoader.setCustomModelResourceLocation(sword, 0, new ModelResourceLocation(sword.getRegistryName(), "invenory"));
        ModelLoader.setCustomModelResourceLocation(axe, 0, new ModelResourceLocation(axe.getRegistryName(), "invenory"));
    }

    public void registerRecipes(final Map<ResourceLocation, IRecipe> recipes, Object ingotStackOrString){
        recipes.put(new ResourceLocation(SimpleSteel.MODID, "pickaxe_" + this.material.name()),
                new ShapedOreRecipe(null, pickaxe, "mmm", " s ", " s ", 'm', ingotStackOrString, 's', "stickWood"));
        recipes.put(new ResourceLocation(SimpleSteel.MODID, "axe_" + this.material.name()),
                new ShapedOreRecipe(null, axe, "mm", "ms", " s", 'm', ingotStackOrString, 's', "stickWood"));
        recipes.put(new ResourceLocation(SimpleSteel.MODID, "sword_" + this.material.name()),
                new ShapedOreRecipe(null, sword, "m", "m", "s", 'm', ingotStackOrString, 's', "stickWood"));
        recipes.put(new ResourceLocation(SimpleSteel.MODID, "shovel_" + this.material.name()),
                new ShapedOreRecipe(null, shovel, "m", "s", "s", 'm', ingotStackOrString, 's', "stickWood"));
    }

    public ItemPickaxe getPickaxe() {
        return pickaxe;
    }

    public ItemSword getSword() {
        return sword;
    }

    public ItemAxe getAxe() {
        return axe;
    }

    public ItemSpade getShovel() {
        return shovel;
    }

    private static class Pickaxe extends ItemPickaxe{
        public Pickaxe(ToolMaterial material) {
            super(material);
        }
    }

    private static class Axe extends ItemAxe{
        public Axe(ToolMaterial material) {
            super(material, material.getDamageVsEntity() + 2.0F, -3.0F);
        }
    }

}
