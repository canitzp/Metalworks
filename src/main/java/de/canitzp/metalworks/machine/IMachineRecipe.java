package de.canitzp.metalworks.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * @author canitzp
 */
public interface IMachineRecipe extends IForgeRegistryEntry<IMachineRecipe>{

    default boolean checkInput(ItemStack[] inputs){
        return false;
    }

}
