package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.audio.TempMusic;
import eatyourbeets.utilities.Field;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class MusicMasterPatch
{
    @SpirePatch(clz = MusicMaster.class, method = "playTempBgmInstantly", paramtypez = {String.class})
    public static class PlayTempBgmInstantly
    {
        private static Field<ArrayList<TempMusic>> tempTracksField;

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

        public static boolean AlreadyPlaying(MusicMaster instance, String key)
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
            tempTracksField = JavaUtilities.GetPrivateField("tempTrack", MusicMaster.class);
        }
    }
}