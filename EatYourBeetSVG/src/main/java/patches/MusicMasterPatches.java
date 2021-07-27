package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.audio.TempMusic;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class MusicMasterPatches
{
    private static final FieldInfo<ArrayList<TempMusic>> _tempTracks = JUtils.GetField("tempTrack", MusicMaster.class);

    public static boolean IsAlreadyPlaying(MusicMaster instance, String key)
    {
        final ArrayList<TempMusic> tracks = _tempTracks.Get(instance);
        for (TempMusic m : tracks)
        {
            if (m.key.equals(key))
            {
                return true;
            }
        }

        return false;
    }

    // If a track is already playing, do not start playing the same one
    @SpirePatch(clz = MusicMaster.class, method = "playTempBgmInstantly", paramtypez = {String.class})
    @SpirePatch(clz = MusicMaster.class, method = "playTempBGM", paramtypez = {String.class})
    public static class MusicMasterPatches_playTempBgm
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(MusicMaster instance, String key)
        {
            return IsAlreadyPlaying(instance, key) ? SpireReturn.Return(null) : SpireReturn.Continue();
        }
    }
}