package de.canitzp.simplesteel.machine;

import de.canitzp.simplesteel.SimpleSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
@Mod.EventBusSubscriber
public class MachineRecipe extends IForgeRegistryEntry.Impl<IMachineRecipe> implements IMachineRecipe {

    public static <E extends IMachineRecipe> E getRecipe(ResourceLocation res){
        return (E) SimpleSteel.MACHINE_RECIPE_REGISTRY.getValue(res);
    }

    public static <E extends IMachineRecipe> E getRecipe(Class<? extends E> clazz, ItemStack... inputs){
        if(clazz != null && inputs.length > 0){
            for(E recipe : getRegisteredForType(clazz)){
                if(recipe.checkInput(inputs)){
                    return recipe;
                }
            }
        }
        return null;
    }

    public static <E extends IMachineRecipe> List<E> getRegisteredForType(Class<E> clazz){
        List<E> list = new ArrayList<>();
        for(IMachineRecipe recipe : SimpleSteel.MACHINE_RECIPE_REGISTRY){
            if(clazz.isAssignableFrom(recipe.getClass())){
                list.add((E) recipe);
            }
        }
        return list;
    }

}
