package de.canitzp.metalworks.config;

import de.canitzp.metalworks.Metalworks;
import net.minecraftforge.common.config.Config;

/**
 * @author canitzp
 */
@Config.LangKey("config." + Metalworks.MODID + ":enable.name")
@Config(modid = Metalworks.MODID, category = "enable")
public class ConfEnable {

    @Config.RequiresMcRestart
    @Config.Name("Is Blast Furnace enabled")
    public static boolean BLAST_FURNACE = true;

    @Config.RequiresMcRestart
    @Config.Name("Is Crusher enabled")
    public static boolean CRUSHER = true;

    @Config.RequiresMcRestart
    @Config.Name("Is Duster enabled")
    public static boolean DUSTER = true;

    @Config.RequiresMcRestart
    @Config.Name("Is Geothermal Generator enabled")
    public static boolean GEOTHERMAL_GENERATOR = true;

    @Config.RequiresMcRestart
    @Config.Name("Is Photovoltaic Panel enabled")
    public static boolean PHOTOVOLTAIC_PANEL = true;

}
