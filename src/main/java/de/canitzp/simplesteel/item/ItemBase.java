package de.canitzp.simplesteel.item;

import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * @author canitzp
 */
public class ItemBase extends Item {

    public ItemBase(String name){
        this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setCreativeTab(Registry.TAB);
    }

}
