package de.canitzp.metalworks.recipe;

import de.canitzp.metalworks.machine.blastfurnace.RecipeBlastFurnace;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class SimpleSteelRecipeHandler {

    public static final Map<String, RecipeBlastFurnace> BLAST_FURNACE_RECIPES = new HashMap<>();

    @SuppressWarnings("UnusedReturnValue")
    @Nonnull
    public static String addBlastFurnaceRecipe(@Nonnull RecipeBlastFurnace recipe){
        String id = recipe.getID();
        if(!BLAST_FURNACE_RECIPES.containsKey(id)){
            BLAST_FURNACE_RECIPES.put(id, recipe);
            return id;
        } else {
            throw new RuntimeException("You tried to register the same recipe twice! This is no possible! Recipe: " + recipe.toString());
        }
    }

    @Nullable
    public static RecipeBlastFurnace getBlastFurnaceRecipe(@Nonnull ItemStack input1, @Nonnull ItemStack input2, @Nonnull ItemStack input3){
        for(RecipeBlastFurnace recipe : BLAST_FURNACE_RECIPES.values()){
            if(recipe.isRecipe(input1, input2, input3)){
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    public static String getIdForBlastFurnaceRecipe(@Nullable RecipeBlastFurnace recipe){
        for(Map.Entry<String, RecipeBlastFurnace> entry : BLAST_FURNACE_RECIPES.entrySet()){
            if(entry.getValue().equals(recipe)){
                return entry.getKey();
            }
        }
        return null;
    }

    @Nullable
    public static RecipeBlastFurnace getBlastFurnaceRecipeForId(String id){
        return BLAST_FURNACE_RECIPES.getOrDefault(id, null);
    }

}
