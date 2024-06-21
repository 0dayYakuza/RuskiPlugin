package eu.darkbot.ruski.behaviours.autodisconnect;

import eu.darkbot.api.config.annotations.Configuration;
import eu.darkbot.api.config.annotations.Number;

@Configuration("auto_disconnect")
public class ADConfig {
    public boolean ENABLE = false;

    @Number(min = 0, max = 525600, step = 60)
    public int MAX_BOTTING_TIME = 0;

    @Number(min = 0, max = 1000000000, step = 50000)
    public int MAX_URIDIUM = 0;

    @Number(min = 0, max = 9999, step = 10)
    public int MAX_DEATHS_IN_HOUR = 0;

    @Number(min = 0, max = 100000, step = 500)
    public int MAX_PING = 0;

    @Number(min = 0, max = 350000, step = 50000)
    public int NOVA_ENERGY = 0;

    public boolean MAX_CARGO = false;

    public boolean FINISH_NPC = false;
    public boolean disconnect_bot = false;
    @Number(min = 0, max = 575600, step = 60)
    public int pause_bot = 0;
}
