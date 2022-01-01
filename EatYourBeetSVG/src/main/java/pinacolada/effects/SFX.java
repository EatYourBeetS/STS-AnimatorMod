package pinacolada.effects;

import basemod.BaseMod;
import patches.MainMusicPatches;

public class SFX extends eatyourbeets.effects.SFX
{
    public static void Initialize()
    {
        BaseMod.addAudio(PCL_DECAPITATION, "audio/pcl/sound/DECAPITATION.ogg");
        BaseMod.addAudio(PCL_SPRAY, "audio/pcl/sound/SPRAY.ogg");
        BaseMod.addAudio(PCL_DARKNESS, "audio/pcl/sound/DARKNESS.ogg");
        BaseMod.addAudio(PCL_BOOST, "audio/pcl/sound/EE01_12_Encourage.ogg");
        BaseMod.addAudio(PCL_PSI, "audio/pcl/sound/PSI.ogg");
        BaseMod.addAudio(PCL_PUNCH, "audio/pcl/sound/PUNCH.ogg");
        BaseMod.addAudio(PCL_STAR, "audio/pcl/sound/STAR.ogg");
        BaseMod.addAudio(PCL_ORB_AIR_EVOKE, "audio/pcl/sound/ORB_AIR_EVOKE.ogg");
        BaseMod.addAudio(PCL_ORB_EARTH_EVOKE, "audio/pcl/sound/ORB_EARTH_EVOKE.ogg");
        BaseMod.addAudio(PCL_ORB_EARTH_CHANNEL, "audio/pcl/sound/ORB_EARTH_CHANNEL.ogg");
        BaseMod.addAudio(PCL_ORB_WATER_EVOKE, "audio/pcl/sound/ORB_WATER_EVOKE.ogg");
        BaseMod.addAudio(PCL_ORB_WATER_CHANNEL, "audio/pcl/sound/ORB_WATER_CHANNEL.ogg");
        BaseMod.addAudio(PCL_SUPPORT_DAMAGE, "audio/pcl/sound/EVFX02_11_QuickBlade.ogg");
        BaseMod.addAudio(PCL_STAR, "audio/pcl/sound/STAR.ogg");

        MainMusicPatches.SetFolderPath("audio/pcl/music/");
    }

    public static final String PCL_ARROW = ANIMATOR_ARROW;
    public static final String PCL_SPRAY = "PCL_SPRAY";
    public static final String PCL_BOOST = "PCL_BOOST";
    public static final String PCL_DARKNESS = "PCL_DARKNESS";
    public static final String PCL_DECAPITATION = "PCL_DECAPITATION";
    public static final String PCL_GUNSHOT = ANIMATOR_GUNSHOT;
    public static final String PCL_SPEAR_1 = ANIMATOR_SPEAR_1;
    public static final String PCL_SPEAR_2 = ANIMATOR_SPEAR_2;
    public static final String PCL_ORB_AIR_EVOKE = "PCL_ORB_AIR_EVOKE";
    public static final String PCL_ORB_EARTH_CHANNEL = "PCL_ORB_EARTH_CHANNEL";
    public static final String PCL_ORB_EARTH_EVOKE = "PCL_ORB_EARTH_EVOKE";
    public static final String PCL_ORB_WATER_CHANNEL = "PCL_ORB_WATER_CHANNEL";
    public static final String PCL_ORB_WATER_EVOKE = "PCL_ORB_WATER_EVOKE";
    public static final String PCL_PSI = "PCL_PSI";
    public static final String PCL_PUNCH = "PCL_PUNCH";
    public static final String PCL_STAR = "PCL_STAR";
    public static final String PCL_SUPPORT_DAMAGE = "PCL_SUPPORT_DAMAGE";
    public static final String PCL_WATER_DOME = "WATER_DOME.ogg";

    public SFX(String key) {
        super(key);
    }

    public SFX(String key, float pitchMin, float pitchMax) {
        super(key, pitchMin, pitchMax);
    }

    public SFX(String key, float pitchMin, float pitchMax, float volume) {
        super(key, pitchMin, pitchMax, volume);
    }
}
