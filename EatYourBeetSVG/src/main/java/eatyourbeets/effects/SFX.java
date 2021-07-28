package eatyourbeets.effects;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.actions.special.PlaySFX;

public class SFX
{
    public static final String ATTACK_MAGIC_BEAM_SHORT = "ATTACK_MAGIC_BEAM_SHORT";
    public static final String ORB_LIGHTNING_PASSIVE = "ORB_LIGHTNING_PASSIVE";
    public static final String ORB_LIGHTNING_EVOKE = "ORB_LIGHTNING_EVOKE";
    public static final String ATTACK_DEFECT_BEAM = "ATTACK_DEFECT_BEAM";
    public static final String ATTACK_WHIRLWIND = "ATTACK_WHIRLWIND";
    public static final String ORB_FROST_EVOKE = "ORB_FROST_EVOKE";
    public static final String ORB_FROST_CHANNEL = "ORB_FROST_CHANNEL";
    public static final String ATTACK_FIRE = "ATTACK_FIRE";
    public static final String EVENT_VAMP_BITE = "EVENT_VAMP_BITE";
    public static final String UI_HOVER = "UI_HOVER";
    public static final String UI_CLICK_1 = "UI_CLICK_1";

    public static void Play(String key, float pitchAdjustment, boolean variable)
    {
        if (variable)
        {
            CardCrawlGame.sound.play(key, pitchAdjustment);
        }
        else
        {
            CardCrawlGame.sound.playA(key, pitchAdjustment);
        }
    }

    public static void Play(String key)
    {
        CardCrawlGame.sound.play(key);
    }

    public static PlaySFX Create(String key, float pitchAdjustment, boolean variable)
    {
        return new PlaySFX(key, pitchAdjustment, !variable);
    }

    public static PlaySFX Bite(float pitchAdj, boolean variable)
    {
        return Create(EVENT_VAMP_BITE, pitchAdj, variable);
    }

    public static PlaySFX Fire(float pitchAdj, boolean variable)
    {
        return Create(ATTACK_FIRE, pitchAdj, variable);
    }

    public static PlaySFX FrostChannel(float pitchAdj, boolean variable)
    {
        return Create(ORB_FROST_CHANNEL, pitchAdj, variable);
    }

    public static PlaySFX FrostEvoke(float pitchAdj, boolean variable)
    {
        return Create(ORB_FROST_EVOKE, pitchAdj, variable);
    }

    public static PlaySFX LightningEvoke(float pitchAdj, boolean variable)
    {
        return Create(ORB_LIGHTNING_EVOKE, pitchAdj, variable);
    }

    public static PlaySFX LightningPassive(float pitchAdj, boolean variable)
    {
        return Create(ORB_LIGHTNING_PASSIVE, pitchAdj, variable);
    }

    public static PlaySFX MagicBeam_Short(float pitchAdj, boolean variable)
    {
        return Create(ATTACK_MAGIC_BEAM_SHORT, pitchAdj, variable);
    }

    public static PlaySFX SweepingBeam(float pitchAdj, boolean variable)
    {
        return Create(ATTACK_DEFECT_BEAM, pitchAdj, variable);
    }

    public static PlaySFX Whirlwind(float pitchAdj, boolean variable)
    {
        return Create(ATTACK_WHIRLWIND, pitchAdj, variable);
    }
}
