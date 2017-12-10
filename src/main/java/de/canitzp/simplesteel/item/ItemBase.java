package de.canitzp.simplesteel.item;

import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public class ItemBase extends Item {

    public static final List<ItemBase> ITEMS = new ArrayList<>();
    private List<Object[]> recipes = new ArrayList<>();
    private List<String> oreNames = new ArrayList<>();

    public ItemBase(String name){
        this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setCreativeTab(Registry.TAB);
    }

    public ItemBase register(){
        ITEMS.add(this);
        return this;
    }

    public ItemBase addRecipes(Object... recipe){
        this.recipes.add(recipe);
        return this;
    }

    public ItemBase addOreName(String... names){
        this.oreNames.addAll(Arrays.asList(names));
        return this;
    }

    @SideOnly(Side.CLIENT)
    public void registerClient(){
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    public void registerRecipes(final List<IRecipe> recipes){
        if(!this.recipes.isEmpty()){
            for(Object[] rec : this.recipes){
                recipes.add(new ShapedOreRecipe(null, this, rec));
            }
        }
    }

    public List<String> getOreNames(){
        return this.oreNames;
    }

    public boolean hasOreNames(){
        return getOreNames() != null && !getOreNames().isEmpty();
    }

}
