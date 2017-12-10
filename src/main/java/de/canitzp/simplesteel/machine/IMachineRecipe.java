package de.canitzp.simplesteel.machine;

import de.canitzp.simplesteel.integration.jei.SimpleSteelJEIPlugin;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * @author canitzp
 */
public interface IMachineRecipe extends IForgeRegistryEntry<IMachineRecipe>{

    default boolean checkInput(ItemStack[] inputs){
        return false;
    }

}
