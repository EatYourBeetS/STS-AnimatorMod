package patches;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;

import java.util.ArrayList;

public class MainMusicPatches
{
    private static String folderPath;
    private static ArrayList<String> tracks = new ArrayList<>();

    public static void AddMusic(String fileName)
    {
        tracks.add(fileName);
        BaseMod.addAudio(fileName, folderPath + fileName);
    }

    public static void SetFolderPath(String path)
    {
        folderPath = path;
    }

    @SpirePatch(clz = MainMusic.class, method = "newMusic", paramtypez = {String.class})
    public static class MusicMasterPatches_playTempBgm
    {
        @SpirePrefixPatch
        public static SpireReturn<Music> Prefix(String key)
        {
            if (Gdx.audio != null)
            {
                for (String track : tracks)
                {
                    if (key.endsWith(track))
                    {
                        return SpireReturn.Return(Gdx.audio.newMusic(Gdx.files.internal(folderPath + track)));
                    }
                }
            }

            return SpireReturn.Continue();
        }
    }
}