package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.audio.TempMusic;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

@SpirePatch(clz = MusicMaster.class, method = "playTempBgmInstantly", paramtypez = {String.class})
public class MusicMasterPatches
{
    private static FieldInfo<ArrayList<TempMusic>> tempTracksField;

    @SpirePrefixPatch
    public static SpireReturn Prefix(MusicMaster instance, String key)
    {
        ArrayList<TempMusic> tempTracks = tempTracksField.Get(instance);

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
        ArrayList<TempMusic> tempTracks = tempTracksField.Get(instance);

        for (TempMusic m : tempTracks)
        {
            if (m.key.equals(key))
            {
                return true;
            }
        }

        return false;
    }

    static
    {
        tempTracksField = JavaUtilities.GetField("tempTrack", MusicMaster.class);
    }
}