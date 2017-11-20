package de.canitzp.simplesteel.item;

import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class ItemPickaxeBase extends ItemPickaxe {

    public ItemPickaxeBase() {
        super(Registry.MATERIAL_STEEL);
        this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, "steel_pickaxe"));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setCreativeTab(Registry.TAB);
    }

}
