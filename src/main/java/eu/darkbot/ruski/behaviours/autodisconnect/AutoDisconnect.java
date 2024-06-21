package eu.darkbot.ruski.behaviours.autodisconnect;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.extensions.Behavior;
import eu.darkbot.api.extensions.Configurable;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.managers.*;
import eu.darkbot.ruski.types.VerifierChecker;
import eu.darkbot.shared.legacy.LegacyModuleAPI;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Feature(name = "Auto disconnect", description = "Auto disconnects from game")
public class AutoDisconnect implements Behavior, Configurable<ADConfig> {
    private final BotAPI bot;
    private final HeroAPI hero;
    private final StatsAPI stats;
    private final LegacyModuleAPI legacyModuleAPI;
    private final RepairAPI repair;

    private ADConfig config;
    private boolean disconnecting = false;
    private Instant lastPauseTime;

    public AutoDisconnect(BotAPI bot, HeroAPI hero, StatsAPI stats, LegacyModuleAPI legacyModuleAPI, RepairAPI repair, AuthAPI auth) {
        if (!Arrays.equals(VerifierChecker.class.getSigners(), getClass().getSigners())) throw new SecurityException();
        VerifierChecker.verifyAuthApi(auth);
        this.bot = bot;
        this.hero = hero;
        this.stats = stats;
        this.legacyModuleAPI = legacyModuleAPI;
        this.repair = repair;
        this.lastPauseTime = Instant.now().minus(Duration.ofDays(1)); // initialize last time?
    }

    @Override
    public void onTickBehavior() {
        long maxBottingTime = config.MAX_BOTTING_TIME * 60L * 1000L;
        long currentBottingTime = stats.getRunningTime().getSeconds() * 1000L;

        if (!config.ENABLE || disconnecting) return;
        if (hero.isAttacking() && config.FINISH_NPC) return;

        long deathsInLastHour = repair.getDeathAmount() / Math.max(stats.getRunningTime().getSeconds() / 3600, 1);

        // Check if enough time has passed since the last pause
        Duration timeSinceLastPause = Duration.between(lastPauseTime, Instant.now());

        if (config.disconnect_bot) {
            if (config.MAX_BOTTING_TIME > 0 && currentBottingTime >= maxBottingTime) disconnect();
            if (config.MAX_URIDIUM > 0 && stats.getTotalUridium() >= config.MAX_URIDIUM) disconnect();
            if (config.MAX_DEATHS_IN_HOUR > 0 && deathsInLastHour >= config.MAX_DEATHS_IN_HOUR) disconnect();
            if (config.MAX_CARGO && stats.getCargo() >= stats.getMaxCargo()) disconnect();
            if (config.MAX_PING > 0 && stats.getPing() > config.MAX_PING) disconnect();
            if (config.NOVA_ENERGY > 0 && config.NOVA_ENERGY >= stats.getNovaEnergy()) disconnect();
        }

        if (config.pause_bot > 0 && timeSinceLastPause.toMinutes() >= config.pause_bot) {
            if (config.MAX_BOTTING_TIME > 0 && currentBottingTime >= maxBottingTime) pause_bot();
            if (config.MAX_URIDIUM > 0 && stats.getTotalUridium() >= config.MAX_URIDIUM) pause_bot();
            if (config.MAX_DEATHS_IN_HOUR > 0 && deathsInLastHour >= config.MAX_DEATHS_IN_HOUR) pause_bot();
            if (config.MAX_CARGO && stats.getCargo() >= stats.getMaxCargo()) pause_bot();
            if (config.MAX_PING > 0 && stats.getPing() > config.MAX_PING) pause_bot();
            if (config.NOVA_ENERGY > 0 && config.NOVA_ENERGY >= stats.getNovaEnergy()) pause_bot();
        }
    }

    @Override
    public void onStoppedBehavior() {
        lastPauseTime = Instant.now(); // refresh last time
        disconnecting = false;
    }

    @Override
    public void setConfig(ConfigSetting<ADConfig> config) {
        this.config = config.getValue();
    }

    private void disconnect() {
        bot.setModule(legacyModuleAPI.getDisconnectModule(null, "Auto disconnect: Triggered by config settings"));
        disconnecting = true;
    }

    private void pause_bot() {
        long pause_duration = config.pause_bot * 60L * 1000L;
        bot.setModule(legacyModuleAPI.getDisconnectModule(pause_duration, "Auto pause: Triggered by config settings"));
        lastPauseTime = Instant.now(); // Update the last pause time
    }
}