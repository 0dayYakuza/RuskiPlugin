package eu.darkbot.ruski.modules.oreseller;

import eu.darkbot.api.config.annotations.Configuration;
import eu.darkbot.api.config.annotations.Dropdown;
import eu.darkbot.api.config.annotations.Number;
import eu.darkbot.api.config.annotations.Percentage;
import eu.darkbot.api.managers.OreAPI;

import java.util.*;
import java.util.stream.Collectors;

@Configuration("oreseller")
public class OreSellerConfig {
    public boolean ENABLE_FEATURE = false;

    public boolean SELL_ON_BASE = false;
    public boolean SELL_BY_PET = false;
    public boolean SELL_BY_DRONE = false;

    @Dropdown(options = Maps.class)
    public String SELL_MAP = "X-1";

    @Dropdown(options = OreSupplier.class, multi = true)
    public Set<OreAPI.Ore> ORE_LIST = EnumSet.noneOf(OreAPI.Ore.class);

    @Percentage
    @Number(min = 0, max = 1, step = 0.05)
    public double SELL_PERCENT = 0.95D;

    public boolean FINISH_TARGET_BEFORE_SELL = false;
    public boolean SELL_NO_NPC_IN_SIGHT = false;


    public static class OreSupplier implements Dropdown.Options<OreAPI.Ore> {
        private static final List<OreAPI.Ore> OPTIONS = Arrays.stream(OreAPI.Ore.values()).filter(OreAPI.Ore::isSellable).collect(Collectors.toList());

        public Collection<OreAPI.Ore> options() {
            return OPTIONS;
        }
    }
}