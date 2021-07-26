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

    public static PlaySFX Create(String key, float pitchAdjustment, boolean variable)
    {
        return new PlaySFX(key, pitchAdjustment, !variable);
    }

    public static PlaySFX Bite(float pitch, boolean variable)
    {
        return Create(EVENT_VAMP_BITE, pitch, variable);
    }

    public static PlaySFX Fire(float pitch, boolean variable)
    {
        return Create(ATTACK_FIRE, pitch, variable);
    }

    public static PlaySFX FrostChannel(float pitch, boolean variable)
    {
        return Create(ORB_FROST_CHANNEL, pitch, variable);
    }

    public static PlaySFX FrostEvoke(float pitch, boolean variable)
    {
        return Create(ORB_FROST_EVOKE, pitch, variable);
    }

    public static PlaySFX LightningEvoke(float pitch, boolean variable)
    {
        return Create(ORB_LIGHTNING_EVOKE, pitch, variable);
    }

    public static PlaySFX LightningPassive(float pitch, boolean variable)
    {
        return Create(ORB_LIGHTNING_PASSIVE, pitch, variable);
    }

    public static PlaySFX MagicBeam_Short(float pitch, boolean variable)
    {
        return Create(ATTACK_MAGIC_BEAM_SHORT, pitch, variable)                ;
    }

    public static PlaySFX SweepingBeam(float pitch, boolean variable)
    {
        return Create(ATTACK_DEFECT_BEAM, pitch, variable);
    }

    public static PlaySFX Whirlwind(float pitch, boolean variable)
    {
        return Create(ATTACK_WHIRLWIND, pitch, variable);
    }
}
