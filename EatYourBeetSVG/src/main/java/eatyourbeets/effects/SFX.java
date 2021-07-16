package eatyourbeets.effects;

import eatyourbeets.actions.special.PlaySFX;

public class SFX
{
    public static PlaySFX Bite(float pitch, boolean variable)
    {
        return Create("EVENT_VAMP_BITE", pitch, variable);
    }

    public static PlaySFX Create(String key, float pitchAdjustment, boolean variable)
    {
        return new PlaySFX(key, pitchAdjustment, !variable);
    }

    public static PlaySFX Fire(float pitch, boolean variable)
    {
        return Create("ATTACK_FIRE", pitch, variable);
    }

    public static PlaySFX FrostChannel(float pitch, boolean variable)
    {
        return Create("ORB_FROST_CHANNEL", pitch, variable);
    }

    public static PlaySFX FrostEvoke(float pitch, boolean variable)
    {
        return Create("ORB_FROST_EVOKE", pitch, variable);
    }

    public static PlaySFX LightningEvoke(float pitch, boolean variable)
    {
        return Create("ORB_LIGHTNING_EVOKE", pitch, variable);
    }

    public static PlaySFX LightningPassive(float pitch, boolean variable)
    {
        return Create("ORB_LIGHTNING_PASSIVE", pitch, variable);
    }

    public static PlaySFX SweepingBeam(float pitch, boolean variable)
    {
        return Create("ATTACK_DEFECT_BEAM", pitch, variable);
    }

    public static PlaySFX Whirlwind(float pitch, boolean variable)
    {
        return Create("ATTACK_WHIRLWIND", pitch, variable);
    }
}
