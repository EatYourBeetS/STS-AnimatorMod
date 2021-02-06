package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.audio.TempMusic;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

// If a track is already playing, do not start playing the same one
@SpirePatch(clz = MusicMaster.class, method = "playTempBgmInstantly", paramtypez = {String.class})
public class MusicMasterPatches
{
    private static final FieldInfo<ArrayList<TempMusic>> _tempTracks = JUtils.GetField("tempTrack", MusicMaster.class);

    @SpirePrefixPatch
    public static SpireReturn Prefix(MusicMaster instance, String key)
    {
        ArrayList<TempMusic> tempTracks = _tempTracks.Get(instance);

        for (TempMusic m : tempTracks)
        {
            if (m.key.equals(key))
            {
                return SpireReturn.Return(null);
            }
        }

        return SpireReturn.Continue();
    }

    public static boolean IsAlreadyPlaying(MusicMaster instance, String key)
    {
        ArrayList<TempMusic> tempTracks = _tempTracks.Get(instance);

        for (TempMusic m : tempTracks)
        {
            if (m.key.equals(key))
            {
                return true;
            }
        }

        return false;
    }
}