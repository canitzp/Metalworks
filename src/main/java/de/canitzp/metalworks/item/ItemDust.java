package de.canitzp.metalworks.item;

import de.canitzp.metalworks.Metalworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import org.apache.commons.lang3.StringUtils;

/**
 * @author canitzp
 */
public class ItemDust extends ItemBase {

    private final int color;

    public ItemDust(String name, int color) {
        super(name + "_dust");
        this.color = color;
        this.addOreName("dust" + StringUtils.capitalize(name));
    }

    @Override
    public void registerClient() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(new ResourceLocation(Metalworks.MODID, "dust"), "inventory"));
    }

    @Override
    public void registerColorHandler(ItemColors itemColors) {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> this.color, this);
    }
}
