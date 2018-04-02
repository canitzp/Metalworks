package de.canitzp.metalworks.config;

import de.canitzp.metalworks.Metalworks;
import net.minecraftforge.common.config.Config;

/**
 * @author canitzp
 */
@SuppressWarnings("CanBeFinal")
@Config.LangKey("config." + Metalworks.MODID + ":machines.name")
@Config(modid = Metalworks.MODID, category = "machines")
public class ConfMachines {

    /**
     * Blast Furnace
     */
    @Config.RequiresMcRestart
    @Config.Name("Blast Furnace default energy usage per tick")
    @Config.Comment({"This defines the default energy consumption for the Blast Furnace.",
            "Most recipes are overriding this value to a custom one."})
    @Config.RangeInt(min = 0, max = 10000)
    public static int BF_DEFAULT_ENERGY = 100;

    @Config.RequiresMcRestart
    @Config.Name("Blast Furnace default burn time")
    @Config.Comment({"This defines the default burn time for the Blast Furnace.",
            "Most recipes are overriding this value to a custom one."})
    @Config.RangeInt(min = 1, max = 5000)
    public static int BF_DEFAULT_BURN_TIME = 300;

    /**
     * Crusher
     */
    @Config.RequiresMcRestart
    @Config.Name("Crusher default energy usage per tick")
    @Config.Comment({"This defines the default energy consumption for the Crusher.",
            "Most recipes are overriding this value to a custom one."})
    @Config.RangeInt(min = 0, max = 10000)
    public static int CR_DEFAULT_ENERGY = 65;

    @Config.RequiresMcRestart
    @Config.Name("Crusher default burn time")
    @Config.Comment({"This defines the default burn time for the Crusher.",
            "Most recipes are overriding this value to a custom one."})
    @Config.RangeInt(min = 1, max = 5000)
    public static int CR_DEFAULT_BURN_TIME = 200;

    /**
     * Duster
     */
    @Config.RequiresMcRestart
    @Config.Name("Duster default energy usage per tick")
    @Config.Comment({"This defines the default energy consumption for the Duster.",
            "Most recipes are overriding this value to a custom one."})
    @Config.RangeInt(min = 0, max = 10000)
    public static int DU_DEFAULT_ENERGY = 75;

    @Config.RequiresMcRestart
    @Config.Name("Duster default burn time")
    @Config.Comment({"This defines the default burn time for the Duster.",
            "Most recipes are overriding this value to a custom one."})
    @Config.RangeInt(min = 1, max = 5000)
    public static int DU_DEFAULT_BURN_TIME = 200;

    /**
     * Photovoltaic Panel
     */
    @Config.RequiresWorldRestart
    @Config.Name("Photovoltaic Panel default energy production per tick")
    @Config.RangeInt(min = 1, max = 25)
    public static int PP_ENERGY_PRODUCTION = 8;

    /**
     * Bio Generator
     */
    @Config.RequiresMcRestart
    @Config.Name("Bio Generator default energy production per tick")
    @Config.Comment({"This defines the default energy production per tick",
            "Most recipes are overriding this value to a custom one."})
    @Config.RangeInt(min = 1, max = 5000)
    public static int BG_DEFAULT_ENERGY = 5;

    @Config.RequiresMcRestart
    @Config.Name("Bio Generator default burn time")
    @Config.Comment({"This defines the default burn time for Bio Generator recipes",
            "Most recipes are overriding this value to a custom one."})
    @Config.RangeInt(min = 0, max = 5000)
    public static int BG_DEFAULT_BURN_TIME = 75;

}
