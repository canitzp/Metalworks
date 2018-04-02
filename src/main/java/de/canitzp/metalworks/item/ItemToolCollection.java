package de.canitzp.metalworks.item;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Registry;
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
import java.util.Objects;

/**
 * @author canitzp
 */
//TODO maybe update shears too? or fishing rod?
public class ItemToolCollection {

    private final Item.ToolMaterial material;

    private final ItemPickaxe pickaxe;
    private final ItemSword sword;
    private final ItemSpade shovel;
    private final ItemAxe axe;

    public ItemToolCollection(Item.ToolMaterial material){
        this.material = material;
        this.pickaxe = new Pickaxe(material){{
            this.setRegistryName(new ResourceLocation(Metalworks.MODID, material.name() + "_pickaxe"));
            this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
            this.setCreativeTab(Registry.TAB);
        }};
        this.sword = new ItemSword(material){{
           this.setRegistryName(new ResourceLocation(Metalworks.MODID, material.name() + "_sword"));
           this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
           this.setCreativeTab(Registry.TAB);
        }};
        this.shovel = new ItemSpade(material){{
            this.setRegistryName(new ResourceLocation(Metalworks.MODID, material.name() + "_shovel"));
            this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
            this.setCreativeTab(Registry.TAB);
        }};
        this.axe = new Axe(material){{
            this.setRegistryName(new ResourceLocation(Metalworks.MODID, material.name() + "_axe"));
            this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
            this.setCreativeTab(Registry.TAB);
        }};
    }

    public void register(IForgeRegistry<Item> reg){
        reg.registerAll(pickaxe, shovel, sword, axe);
    }

    @SideOnly(Side.CLIENT)
    public void bakeModels(){
        ModelLoader.setCustomModelResourceLocation(pickaxe, 0, new ModelResourceLocation(Objects.requireNonNull(pickaxe.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(shovel, 0, new ModelResourceLocation(Objects.requireNonNull(shovel.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(sword, 0, new ModelResourceLocation(Objects.requireNonNull(sword.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(axe, 0, new ModelResourceLocation(Objects.requireNonNull(axe.getRegistryName()), "inventory"));
    }

    public void registerRecipes(final Map<ResourceLocation, IRecipe> recipes, Object ingotStackOrString){
        recipes.put(new ResourceLocation(Metalworks.MODID, "pickaxe_" + this.material.name()),
                new ShapedOreRecipe(null, pickaxe, "mmm", " s ", " s ", 'm', ingotStackOrString, 's', "stickWood"));
        recipes.put(new ResourceLocation(Metalworks.MODID, "axe_" + this.material.name()),
                new ShapedOreRecipe(null, axe, "mm", "ms", " s", 'm', ingotStackOrString, 's', "stickWood"));
        recipes.put(new ResourceLocation(Metalworks.MODID, "sword_" + this.material.name()),
                new ShapedOreRecipe(null, sword, "m", "m", "s", 'm', ingotStackOrString, 's', "stickWood"));
        recipes.put(new ResourceLocation(Metalworks.MODID, "shovel_" + this.material.name()),
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
            super(material, material.getAttackDamage() + 2.0F, -3.0F);
        }
    }

}
