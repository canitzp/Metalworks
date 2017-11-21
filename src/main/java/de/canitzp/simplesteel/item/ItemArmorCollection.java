package de.canitzp.simplesteel.item;

import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
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

/**
 * @author canitzp
 */
public class ItemArmorCollection {

    private ItemArmor.ArmorMaterial material;
    private ItemArmor head;
    private ItemArmor chestplate;
    private ItemArmor legs;
    private ItemArmor shoes;

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
        ModelLoader.setCustomModelResourceLocation(head, 0, new ModelResourceLocation(head.getRegistryName(), "invenory"));
        ModelLoader.setCustomModelResourceLocation(chestplate, 0, new ModelResourceLocation(chestplate.getRegistryName(), "invenory"));
        ModelLoader.setCustomModelResourceLocation(legs, 0, new ModelResourceLocation(legs.getRegistryName(), "invenory"));
        ModelLoader.setCustomModelResourceLocation(shoes, 0, new ModelResourceLocation(shoes.getRegistryName(), "invenory"));
    }

    public void registerRecipes(final Map<ResourceLocation, IRecipe> recipes, Object ingotStackOrString){
        recipes.put(new ResourceLocation(SimpleSteel.MODID, "head_" + this.material.name()),
                new ShapedOreRecipe(null, head, "sss", "s s", 's', ingotStackOrString));
        recipes.put(new ResourceLocation(SimpleSteel.MODID, "chest_" + this.material.name()),
                new ShapedOreRecipe(null, chestplate, "s s", "sss", "sss", 's', ingotStackOrString));
        recipes.put(new ResourceLocation(SimpleSteel.MODID, "legs_" + this.material.name()),
                new ShapedOreRecipe(null, legs, "sss", "s s", "s s", 's', ingotStackOrString));
        recipes.put(new ResourceLocation(SimpleSteel.MODID, "feet_" + this.material.name()),
                new ShapedOreRecipe(null, shoes, "s s", "s s", 's', ingotStackOrString));
    }

    public class Armor extends ItemArmor{
        public Armor(ArmorMaterial material, EntityEquipmentSlot equipmentSlot) {
            super(material, 0, equipmentSlot);
            this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, material.name() + "_" + equipmentSlot.getName()));
            this.setUnlocalizedName(this.getRegistryName().toString());
            this.setCreativeTab(Registry.TAB);
        }

        @Nullable
        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
            return SimpleSteel.MODID + ":" + "textures/armor/" + this.getArmorMaterial().name() + "_armor_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
        }
    }

}
