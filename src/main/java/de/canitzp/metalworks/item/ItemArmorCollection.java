package de.canitzp.metalworks.item;

import de.canitzp.metalworks.Metalworks;
import de.canitzp.metalworks.Registry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

/**
 * @author canitzp
 */
public class ItemArmorCollection {

    private final ItemArmor.ArmorMaterial material;
    private final ItemArmor head;
    private final ItemArmor chestplate;
    private final ItemArmor legs;
    private final ItemArmor shoes;

    public ItemArmorCollection(ItemArmor.ArmorMaterial material){
        this.material = material;
        head = new Armor(material, EntityEquipmentSlot.HEAD);
        chestplate = new Armor(material, EntityEquipmentSlot.CHEST);
        legs = new Armor(material, EntityEquipmentSlot.LEGS);
        shoes = new Armor(material, EntityEquipmentSlot.FEET);
    }

    public void register(IForgeRegistry<Item> reg){
        reg.registerAll(head, chestplate, legs, shoes);
    }

    @SideOnly(Side.CLIENT)
    public void bakeModels(){
        ModelLoader.setCustomModelResourceLocation(head, 0, new ModelResourceLocation(Objects.requireNonNull(head.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(chestplate, 0, new ModelResourceLocation(Objects.requireNonNull(chestplate.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation(Objects.requireNonNull(legs.getRegistryName()), "inventory"));
        ModelLoader.setCustomModelResourceLocation(shoes, 0, new ModelResourceLocation(Objects.requireNonNull(shoes.getRegistryName()), "inventory"));
    }

    public void registerRecipes(final Map<ResourceLocation, IRecipe> recipes, Object ingotStackOrString){
        recipes.put(new ResourceLocation(Metalworks.MODID, "head_" + this.material.name()),
                new ShapedOreRecipe(null, head, "sss", "s s", 's', ingotStackOrString));
        recipes.put(new ResourceLocation(Metalworks.MODID, "chest_" + this.material.name()),
                new ShapedOreRecipe(null, chestplate, "s s", "sss", "sss", 's', ingotStackOrString));
        recipes.put(new ResourceLocation(Metalworks.MODID, "legs_" + this.material.name()),
                new ShapedOreRecipe(null, legs, "sss", "s s", "s s", 's', ingotStackOrString));
        recipes.put(new ResourceLocation(Metalworks.MODID, "feet_" + this.material.name()),
                new ShapedOreRecipe(null, shoes, "s s", "s s", 's', ingotStackOrString));
    }

    public class Armor extends ItemArmor{
        public Armor(ArmorMaterial material, EntityEquipmentSlot equipmentSlot) {
            super(material, 0, equipmentSlot);
            this.setRegistryName(new ResourceLocation(Metalworks.MODID, material.name() + "_" + equipmentSlot.getName()));
            this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
            this.setCreativeTab(Registry.TAB);
        }

        @Nullable
        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
            return Metalworks.MODID + ":" + "textures/armor/" + this.getArmorMaterial().name() + "_armor_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
        }
    }

}
