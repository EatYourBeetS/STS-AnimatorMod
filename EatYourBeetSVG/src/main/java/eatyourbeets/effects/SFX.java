package eatyourbeets.effects;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SFX
{
    public static final String ATTACK_DEFECT_BEAM = "ATTACK_DEFECT_BEAM";
    public static final String ATTACK_FIRE = "ATTACK_FIRE";
    public static final String ATTACK_HEAVY = "ATTACK_HEAVY";
    public static final String ATTACK_MAGIC_BEAM_SHORT = "ATTACK_MAGIC_BEAM_SHORT";
    public static final String ATTACK_WHIRLWIND = "ATTACK_WHIRLWIND";
    public static final String CARD_EXHAUST = "CARD_EXHAUST";
    public static final String EVENT_VAMP_BITE = "EVENT_VAMP_BITE";
    public static final String ORB_DARK_CHANNEL = "ORB_DARK_CHANNEL";
    public static final String ORB_DARK_EVOKE = "ORB_DARK_EVOKE";
    public static final String ORB_FROST_CHANNEL = "ORB_FROST_CHANNEL";
    public static final String ORB_FROST_EVOKE = "ORB_FROST_EVOKE";
    public static final String ORB_PLASMA_CHANNEL = "ORB_PLASMA_CHANNEL";
    public static final String ORB_PLASMA_EVOKE = "ORB_PLASMA_EVOKE";
    public static final String ORB_LIGHTNING_CHANNEL = "ORB_LIGHTNING_CHANNEL";
    public static final String ORB_LIGHTNING_EVOKE = "ORB_LIGHTNING_EVOKE";
    public static final String ORB_LIGHTNING_PASSIVE = "ORB_LIGHTNING_PASSIVE";
    public static final String UI_HOVER = "UI_HOVER";
    public static final String UI_CLICK_1 = "UI_CLICK_1";

    public static float Play(String key)
    {
        return Play(key, 1, 1);
    }

    public static float Play(String key, float pitch)
    {
        return Play(key, pitch, pitch);
    }

    public static float Play(String key, float pitchMin, float pitchMax)
    {
        return CardCrawlGame.sound.playA(key, ((pitchMin == pitchMax) ? pitchMin : MathUtils.random(pitchMin, pitchMax)) - 1) / 1000f;
    }
}
