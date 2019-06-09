package eatyourbeets;

import basemod.BaseMod;

public class AnimatorResources_Audio
{
    public static final String TheUltimateCrystal = "ANIMATOR_THE_ULTIMATE_CRYSTAL.ogg";
    public static final String TheUnnamed = "ANIMATOR_THE_UNNAMED.ogg";
    public static final String TheHaunt = "ANIMATOR_THE_HAUNT.ogg";
    public static final String TheCreature = "ANIMATOR_THE_CREATURE.ogg";

    public static void LoadAudio()
    {
        BaseMod.addAudio("ANIMATOR_ORB_EARTH_EVOKE", "audio/sound/ANIMATOR_ORB_EARTH_EVOKE.ogg");
        BaseMod.addAudio("ANIMATOR_ORB_EARTH_CHANNEL", "audio/sound/ANIMATOR_ORB_EARTH_CHANNEL.ogg");
        BaseMod.addAudio("ANIMATOR_KIRA_POWER", "audio/sound/ANIMATOR_KIRA_POWER.ogg");
        BaseMod.addAudio("ANIMATOR_MEGUMIN_CHARGE", "audio/sound/ANIMATOR_MEGUMIN_CHARGE.ogg");
        //BaseMod.addAudio("ANIMATOR_EMONZAEMON_ATTACK", "audio/sound/ANIMATOR_EMONZAEMON_ATTACK.ogg");

        BaseMod.addAudio(TheHaunt, "audio/music/ANIMATOR_THE_HAUNT.ogg");
        BaseMod.addAudio(TheUnnamed, "audio/music/ANIMATOR_THE_UNNAMED.ogg");
        BaseMod.addAudio(TheUltimateCrystal, "audio/sound/ANIMATOR_THE_ULTIMATE_CRYSTAL.ogg");
        BaseMod.addAudio(TheCreature, "audio/music/ANIMATOR_THE_CREATURE.ogg");
    }
}
