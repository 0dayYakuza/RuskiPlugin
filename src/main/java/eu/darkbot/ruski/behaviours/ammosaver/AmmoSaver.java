package eu.darkbot.ruski.behaviours.ammosaver;

import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.extensions.Configurable;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.extensions.selectors.LaserSelector;
import eu.darkbot.api.extensions.selectors.PrioritizedSupplier;
import eu.darkbot.api.game.entities.Npc;
import eu.darkbot.api.game.items.Item;
import eu.darkbot.api.game.items.SelectableItem;
import eu.darkbot.api.managers.AuthAPI;
import eu.darkbot.api.managers.ConfigAPI;
import eu.darkbot.api.managers.HeroAPI;
import eu.darkbot.api.managers.HeroItemsAPI;
import eu.darkbot.ruski.types.VerifierChecker;

import java.util.Arrays;
import java.util.Optional;

@Feature(name = "Ammo Saver", description = "Changes ammo for its saving")
public class AmmoSaver implements LaserSelector, PrioritizedSupplier<SelectableItem.Laser>, Configurable<AmmoSaverConfig> {

    protected final HeroAPI hero;
    protected final HeroItemsAPI items;
    protected final ConfigSetting<Character> ammoKey;
    public AmmoSaverConfig config;

    public AmmoSaver(HeroAPI hero, HeroItemsAPI items, ConfigAPI config, AuthAPI auth) {
        if (!Arrays.equals(VerifierChecker.class.getSigners(), getClass().getSigners())) throw new SecurityException();
        VerifierChecker.checkAuthenticity(auth);
        this.hero = hero;
        this.items = items;
        this.ammoKey = config.requireConfig("loot.ammo_key");
    }

    public void setConfig(ConfigSetting<AmmoSaverConfig> config) {
        this.config = config.getValue();
    }

    private long getQuantityOf(SelectableItem item) {
        return this.items.getItem(item, new eu.darkbot.api.game.items.ItemFlag[0]).map(value -> (long) value.getQuantity()).orElse(0L);
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }

    @Override
    public PrioritizedSupplier<SelectableItem.Laser> getLaserSupplier() {
        return this;
    }

    @Override
    public SelectableItem.Laser get() {
        Npc target = hero.getLocalTargetAs(Npc.class);
        if (target != null) {
            Optional<SelectableItem.Laser> ammo = target.getInfo().getAmmo();
            if (ammo.isPresent()) {
                SelectableItem.Laser laser = ammo.get();
                long Ammo_quantity = getQuantityOf(laser);
                if (laser == SelectableItem.Laser.UCB_100 && config.USE_UCB_100 && (Ammo_quantity > config.x4_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.MCB_50 && config.USE_MCB_50 && (Ammo_quantity > config.x3_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.MCB_25 && config.USE_MCB_25 && (Ammo_quantity > config.x2_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.CBO_100 && config.USE_CBO_100 && (Ammo_quantity > config.cbo_100_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.A_BL && config.USE_A_BL && (Ammo_quantity > config.a_bl_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.CC_A && config.USE_CC_A && (Ammo_quantity > config.cc_a_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.CC_B && config.USE_CC_B && (Ammo_quantity > config.cc_b_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.CC_C && config.USE_CC_C && (Ammo_quantity > config.cc_c_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.CC_D && config.USE_CC_D && (Ammo_quantity > config.cc_d_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.CC_E && config.USE_CC_E && (Ammo_quantity > config.cc_e_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.CC_F && config.USE_CC_F && (Ammo_quantity > config.cc_f_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.CC_G && config.USE_CC_G && (Ammo_quantity > config.cc_g_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.CC_H && config.USE_CC_H && (Ammo_quantity > config.cc_h_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.EMAA_20 && config.USE_EMAA_20 && (Ammo_quantity > config.emaa_20_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.IDB_125 && config.USE_IDB_125 && (Ammo_quantity > config.idb_125_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.JOB_100 && config.USE_JOB_100 && (Ammo_quantity > config.job_100_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.PIB_100 && config.USE_PIB_100 && (Ammo_quantity > config.pib_100_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.RB_214 && config.USE_RB_214 && (Ammo_quantity > config.rb_214_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.RCB_140 && config.USE_RCB_140 && (Ammo_quantity > config.rcb_140_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.SBL_100 && config.USE_SBL_100 && (Ammo_quantity > config.sbl_100_ammo))
                    return laser;
                if (laser == SelectableItem.Laser.VB_142 && config.USE_VB_142 && (Ammo_quantity > config.vb_142_ammo))
                    return laser;
            }
        }
        Item i = items.getItem(ammoKey.getValue());
        return i != null ? i.getAs(SelectableItem.Laser.class) : null;
    }
}