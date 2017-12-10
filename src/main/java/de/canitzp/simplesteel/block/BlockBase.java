package de.canitzp.simplesteel.block;

import de.canitzp.simplesteel.Registry;
import de.canitzp.simplesteel.SimpleSteel;
import de.canitzp.simplesteel.item.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author canitzp
 */
public class BlockBase<T extends BlockBase<T>> extends Block {

    public static final List<BlockBase> BLOCKS = new ArrayList<>();
    public ItemBlock item;
    private List<Object[]> recipes = new ArrayList<>();
    private List<String> oreNames = new ArrayList<String>();
    private int infoState = 0; // 0=unchecked 1=checked, but no localisation 2=checked and localisation found

    public BlockBase(Material material, MapColor color, String name) {
        super(material, color);
        this.setRegistryName(new ResourceLocation(SimpleSteel.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setCreativeTab(Registry.TAB);
    }

    public BlockBase(Material material, String name){
        this(material, material.getMaterialMapColor(), name);
    }

    public T register(){
        BLOCKS.add(this);
        this.item = new ItemBlock(this);
        this.item.setRegistryName(this.getRegistryName());
        return (T) this;
    }

    public T addRecipes(Object... recipe){
        this.recipes.add(recipe);
        return (T) this;
    }

    public T addOreName(String... names){
        this.oreNames.addAll(Arrays.asList(names));
        return (T) this;
    }

    @SideOnly(Side.CLIENT)
    public void registerClient(){
        if(this.item != null){
            ModelLoader.setCustomModelResourceLocation(this.item, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
        }
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        if(this.infoState == 0){
            if(I18n.hasKey("desc." + this.getRegistryName().toString() + ".name")){
                this.infoState = 2;
            } else {
                this.infoState = 1;
            }
        }
        if(this.infoState == 2 || advanced.isAdvanced()){
            String local = I18n.format("desc." + this.getRegistryName().toString() + ".name");
            for(String s : local.split(" NL ")){
                tooltip.add(s.replace(" NL ", ""));
            }
        }
    }

}
