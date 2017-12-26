package de.canitzp.metalworks.recipe;

import de.canitzp.metalworks.Util;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class OreDictStack {

    public static final OreDictStack EMPTY = new OreDictStack(ItemStack.EMPTY);

    private String oreDictEntry;
    private ItemStack primaryStack = ItemStack.EMPTY;
    private NonNullList<ItemStack> entries;
    private int stacksize = 1;

    public OreDictStack(String oreDictEntry){
        this.oreDictEntry = oreDictEntry;
        this.entries = OreDictionary.getOres(oreDictEntry);
        if(!this.entries.isEmpty()){
            this.primaryStack = this.entries.get(0);
        }
    }

    public OreDictStack(ItemStack stack){
        this.primaryStack = stack;
        this.entries = NonNullList.withSize(1, stack);
    }

    public OreDictStack(Item item){
        this(new ItemStack(item));
    }

    public OreDictStack setStacksize(int stacksize){
        this.stacksize = stacksize;
        return this;
    }

    public String getOreDictEntry() {
        return oreDictEntry;
    }

    public ItemStack getPrimaryStack() {
        return primaryStack;
    }

    public NonNullList<ItemStack> getEntries() {
        return entries;
    }

    public boolean isMergeable(ItemStack other){
        if(this.primaryStack.isEmpty() && other.isEmpty()) return true;
        for(ItemStack stack : this.entries){
            if(Util.canItemStacksStack(stack, other)){
                return true;
            }
        }
        return false;
    }

    public boolean isSubstractable(ItemStack other){
        if(this.primaryStack.isEmpty() && other.isEmpty()) return true;
        for(ItemStack stack : this.entries){
            if(Util.canItemStacksStackIgnoreStacksize(stack, other) && other.getCount() >= this.getStacksize()){
                return true;
            }
        }
        return false;
    }

    public String getName(){
        return this.primaryStack.getUnlocalizedName();
    }

    public int getStacksize() {
        return stacksize;
    }

    public List<ItemStack> getListForJEI(){
        List<ItemStack> stacks = new ArrayList<>();
        for(ItemStack stack : this.entries){
            ItemStack copy = stack.copy();
            copy.setCount(this.getStacksize());
            stacks.add(copy);
        }
        return stacks;
    }
}
