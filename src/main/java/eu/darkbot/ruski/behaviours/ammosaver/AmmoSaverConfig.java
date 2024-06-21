package eu.darkbot.ruski.behaviours.ammosaver;

import eu.darkbot.api.config.annotations.Configuration;
import eu.darkbot.api.config.annotations.Number;

@Configuration("ammo_saver.config")
public class AmmoSaverConfig {

    public boolean USE_UCB_100 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int x4_ammo = 10000;

    public boolean USE_MCB_50 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int x3_ammo = 10000;

    public boolean USE_MCB_25 = false;

    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int x2_ammo = 10000;

    public boolean USE_A_BL = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int a_bl_ammo = 10000;

    public boolean USE_CBO_100 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int cbo_100_ammo = 10000;

    public boolean USE_CC_H = false;

    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int cc_h_ammo = 10000;

    public boolean USE_CC_G = false;

    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int cc_g_ammo = 10000;

    public boolean USE_CC_F = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int cc_f_ammo = 10000;

    public boolean USE_CC_E = false;

    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int cc_e_ammo = 10000;

    public boolean USE_CC_D = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int cc_d_ammo = 10000;

    public boolean USE_CC_C = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int cc_c_ammo = 10000;

    public boolean USE_CC_B = false;

    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int cc_b_ammo = 10000;

    public boolean USE_CC_A = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int cc_a_ammo = 10000;

    public boolean USE_EMAA_20 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int emaa_20_ammo = 10000;

    public boolean USE_IDB_125 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int idb_125_ammo = 10000;

    public boolean USE_JOB_100 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int job_100_ammo = 10000;

    public boolean USE_PIB_100 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int pib_100_ammo = 10000;

    public boolean USE_RB_214 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int rb_214_ammo = 10000;

    public boolean USE_RCB_140 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int rcb_140_ammo = 10000;

    public boolean USE_SBL_100 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int sbl_100_ammo = 10000;

    public boolean USE_VB_142 = false;
    @Number(min = 100.0D, max = 1.0E9D, step = 100000.0D)
    public int vb_142_ammo = 10000;
}
