package eu.darkbot.ruski.modules.oreseller;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.extensions.Behavior;
import eu.darkbot.api.extensions.Configurable;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.game.entities.Portal;
import eu.darkbot.api.game.entities.Station;
import eu.darkbot.api.game.enums.PetGear;
import eu.darkbot.api.game.items.SelectableItem;
import eu.darkbot.api.game.other.EntityInfo;
import eu.darkbot.api.game.other.GameMap;
import eu.darkbot.api.game.other.Location;
import eu.darkbot.api.managers.*;
import eu.darkbot.api.utils.ItemNotEquippedException;
import eu.darkbot.ruski.types.VerifierChecker;
import eu.darkbot.shared.modules.TemporalModule;
import eu.darkbot.shared.utils.MapTraveler;
import eu.darkbot.shared.utils.PortalJumper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static java.lang.Boolean.TRUE;

@Feature(name = "Ore seller", description = "Sells ores")
public class OreSeller extends TemporalModule implements Behavior, Configurable<OreSellerConfig> {
    protected final PluginAPI api;
    protected final BotAPI bot;
    private final HeroAPI hero;
    protected final PetAPI pet;
    private final StatsAPI stats;
    private final OreAPI oreTrade;
    private final MapTraveler traveler;
    private final MovementAPI drive;
    protected final StarSystemAPI starSystem;
    private final HeroItemsAPI items;

    protected final ConfigSetting<Boolean> petEnabled;
    protected boolean petWasEnabled = false;

    protected OreSellerConfig config;
    protected Iterator<OreAPI.Ore> ores = null;
    protected Portal ggExitPortal;
    protected PortalJumper jumper;

    private final Collection<? extends Station> bases;
    private final Collection<? extends Npc> npcs;
    protected long SELL_DELAY = 0;
    protected long cargoFullTime = 0L;
    private State currentStatus;

    // sell maps?
    private final GameMap SELL_11;
    private final GameMap SELL_18;
    private final GameMap SELL_21;
    private final GameMap SELL_28;
    private final GameMap SELL_31;
    private final GameMap SELL_38;
    private final GameMap SELL_52;



    public OreSeller(PluginAPI api, BotAPI bot, HeroAPI hero, StatsAPI stats, OreAPI oreTrade,
                     MovementAPI drive, PortalJumper jumper, MapTraveler traveler, StarSystemAPI starSystem,
                     HeroItemsAPI items, EntitiesAPI entitiesAPI, PetAPI pet, ConfigAPI configAPI, AuthAPI auth) {
        super(bot);
        if (!Arrays.equals(VerifierChecker.class.getSigners(), getClass().getSigners())) throw new SecurityException();
        VerifierChecker.verifyAuthApi(auth);
        this.api = api;
        this.bot = bot;
        this.hero = hero;
        this.pet = pet;
        this.stats = stats;
        this.oreTrade = oreTrade;
        this.items = items;

        this.petEnabled = configAPI.requireConfig("pet.enabled");

        this.traveler = traveler;
        this.drive = drive;
        this.jumper = jumper;
        this.starSystem = starSystem;

        this.bases = entitiesAPI.getStations();
        this.npcs = entitiesAPI.getNpcs();

        // sell maps
        this.SELL_11 = starSystem.getOrCreateMap(1); // 1-1
        this.SELL_21 = starSystem.getOrCreateMap(5); // 2-1
        this.SELL_31 = starSystem.getOrCreateMap(9); // 3-1

        this.SELL_18 = starSystem.getOrCreateMap(20); // 1-8
        this.SELL_28 = starSystem.getOrCreateMap(24); // 2-8
        this.SELL_38 = starSystem.getOrCreateMap(28); // 3-8

        this.SELL_52 = starSystem.getOrCreateMap(92); // 5-2
    }

    public void setConfig(ConfigSetting<OreSellerConfig> config) {
        this.config = config.getValue();
    }

    public String getStatus() {
        return "Ore Seller | " + currentStatus.message; // lastMessage in the end of code
    }

    public boolean canRefresh() {
        return false;
    }

    public void onTickBehavior() {

        if (!config.ENABLE_FEATURE || config.ORE_LIST.isEmpty()) return;
        if (stats.getMaxCargo() == 0 || isStuckInGG()) return;
        if (config.FINISH_TARGET_BEFORE_SELL && hero.isAttacking()) return;
        if (config.SELL_NO_NPC_IN_SIGHT && !npcs.isEmpty()) return;
        if (config.SELL_BY_PET && pet.hasCooldown(PetGear.Cooldown.TRADE)) return;

        if ((stats.getCargo() >= (stats.getMaxCargo() * config.SELL_PERCENT)) && (bot.getModule() != this)) {
            if (cargoFullTime == 0) { cargoFullTime = System.currentTimeMillis(); }
            else if ((System.currentTimeMillis() - cargoFullTime) >= 2000) {
                sellingStarted();
                bot.setModule(this);
            }
        } else { cargoFullTime = 0L; }
    }

    public void onTickModule() {
        if (config.SELL_ON_BASE) travelToMap();
        if (config.SELL_BY_PET) sellByPet();
        if (config.SELL_BY_DRONE) sellByDrone();
    }

    // -------- TRAVEL TO BASE --------
    public void travelToMap() {
        hero.setRoamMode();
        if (hero.getMap().isGG() && ggExitPortal != null) {
            exitGG();
            return;
        }
        currentStatus = State.TRAVEL_TO_BASE;
        if (config.SELL_MAP.equals("X-1")) {
            if (hero.getEntityInfo().getFaction() == EntityInfo.Faction.MMO && (hero.getMap() != SELL_11)) {
                if (!traveler.isDone()) traveler.setTarget(SELL_11);
                traveler.tick();
            }
            if (hero.getEntityInfo().getFaction() == EntityInfo.Faction.EIC && (hero.getMap() != SELL_21)) {
                if (!traveler.isDone()) traveler.setTarget(SELL_21);
                traveler.tick();
            }
            if (hero.getEntityInfo().getFaction() == EntityInfo.Faction.VRU && (hero.getMap() != SELL_31)) {
                if (!traveler.isDone()) traveler.setTarget(SELL_31);
                traveler.tick();
            }
        } else if (config.SELL_MAP.equals("X-8")) {
            if (hero.getEntityInfo().getFaction() == EntityInfo.Faction.MMO && (hero.getMap() != SELL_18)) {
                if (!traveler.isDone()) traveler.setTarget(SELL_18);
                traveler.tick();
            }
            if (hero.getEntityInfo().getFaction() == EntityInfo.Faction.EIC && (hero.getMap() != SELL_28)) {
                if (!traveler.isDone()) traveler.setTarget(SELL_28);
                traveler.tick();
            }
            if (hero.getEntityInfo().getFaction() == EntityInfo.Faction.VRU && (hero.getMap() != SELL_38)) {
                if (!traveler.isDone()) traveler.setTarget(SELL_38);
                traveler.tick();
            }
        } else if (config.SELL_MAP.equals("5-2") && (hero.getMap() != SELL_52)) {
            if (!traveler.isDone()) traveler.setTarget(SELL_52);
            traveler.tick();
        }
        travelToStation();
    }

    public void travelToStation() {
        Station.Refinery base = bases.stream()
                .filter(b -> (b instanceof Station.Refinery &&
                        b.getLocationInfo().isInitialized()))
                .map(Station.Refinery.class::cast).findFirst().orElse(null);
        if (base == null) return;
        if (drive.getDestination().distanceTo(base) > 200) {
            double angle = base.angleTo(hero) + Math.random() * 0.2 - 0.1;
            drive.moveTo(Location.of(base, angle, 100 + 100 * Math.random()));
        } else if (!hero.isMoving() && oreTrade.showTrade(true, base)) {
            sellOres();
        }
    }

    // ------ SELL BY CPU ------
    private void sellByPet() {
        currentStatus = State.USE_PET;
        petEnabled.setValue(TRUE);
        pet.setEnabled(true);
        try { this.pet.setGear(PetGear.TRADER); }
        catch (ItemNotEquippedException ignored) {}
        sellOres();
    }

    private void sellByDrone() {
        if (api.requireAPI(HeroItemsAPI.class).getItem(SelectableItem.Cpu.HMD_07).get().isAvailable() &&
                api.requireAPI(HeroItemsAPI.class).getItem(SelectableItem.Cpu.HMD_07).get().getQuantity() >= 1) {
            currentStatus = State.USE_HMD07;
            items.useItem(SelectableItem.Cpu.HMD_07);
            sellOres();
        } else if (api.requireAPI(HeroItemsAPI.class).getItem(SelectableItem.Cpu.HMD_07).get().getQuantity() == 0) {
            travelToMap();
        }
    }

    // ----- SELLING ORES -----
    private void sellingStarted() {
        if (config.SELL_BY_PET || config.SELL_BY_DRONE) drive.stop(true);
        if (config.SELL_BY_PET) petWasEnabled = petEnabled.getValue();
        ores = config.ORE_LIST.iterator();
    }

    private void sellOres() {
        currentStatus = State.SELLING;
        if (!oreTrade.canSellOres()) SELL_DELAY = System.currentTimeMillis() + 2500;
        if (SELL_DELAY > System.currentTimeMillis()) return;
        SELL_DELAY = System.currentTimeMillis() + 500;
        if (ores != null && ores.hasNext()) { oreTrade.sellOre(ores.next()); }
        else { goBack(); }
    }

    // ------------------------
    private void exitGG() {
        if (ggExitPortal.getLocationInfo().distanceTo(hero) > 150) {
            drive.moveTo(ggExitPortal);
            return;
        }
        jumper.jump(ggExitPortal);
    }

    private boolean isStuckInGG() {
        return (hero.getMap().isGG() && !hero.getMap().getName().equals("LoW") && ggExitPortal == null);
    }

    public void goBack() {
        oreTrade.showTrade(false, null);
        ores = null;
        ggExitPortal = null;
        petEnabled.setValue(petWasEnabled);
        super.goBack();
    }

    public enum State {
        SELLING("Selling ores"),
        TRAVEL_TO_BASE("Traveling to base"),
        USE_PET("Using PET Trade CPU"),
        USE_HMD07("Using Trade Drone HMD_07");

        public final String message;

        State(String message) {
            this.message = message;
        }
    }
}