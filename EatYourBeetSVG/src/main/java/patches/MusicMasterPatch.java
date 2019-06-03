package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.audio.TempMusic;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MusicMasterPatch
{
    @SpirePatch(clz = MusicMaster.class, method = "playTempBgmInstantly", paramtypez = {String.class})
    public static class CardCrawlGamePatches_loadPlayerSave
    {
        private static Field tempTracksField;

        @SpirePrefixPatch
        public static SpireReturn Prefix(MusicMaster instance, String key) throws IllegalAccessException
        {
            ArrayList<TempMusic> tempTracks = (ArrayList<TempMusic>) tempTracksField.get(instance);

            for (TempMusic m : tempTracks)
            {
                if (m.key.equals(key))
                {
                    return SpireReturn.Return(null);
                }
            }

            return SpireReturn.Continue();
        }

        static
        {
            try
            {
                tempTracksField = MusicMaster.class.getDeclaredField("tempTrack");
                tempTracksField.setAccessible(true);
            }
            catch (NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        }
    }
}