package patches;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class RelicLibraryPatches
{
    public static final String SEPARATOR = "~";

    public static AbstractRelic GetRelic(String key)
    {
        if (key != null && GR.IsLoaded())
        {
            AbstractRelic relic = null;
            final int sepIndex = key.indexOf(SEPARATOR);
            final String baseID = sepIndex >= 0 ? key.substring(0, sepIndex) : key;
            final AbstractPlayer.PlayerClass pc = GameUtilities.GetPlayerClass();
            if (pc == GR.Animator.PlayerClass)
            {
                if (baseID.startsWith(GR.AnimatorClassic.Prefix))
                {
                    RelicLibraryPatches_GetRelic.AllowRecursion = false;
                    relic = RelicLibrary.getRelic(GR.AnimatorClassic.ConvertID(baseID, false));
                }
            }
            else if (pc == GR.AnimatorClassic.PlayerClass)
            {
                if (baseID.startsWith(GR.Animator.Prefix))
                {
                    RelicLibraryPatches_GetRelic.AllowRecursion = false;
                    relic = RelicLibrary.getRelic(GR.AnimatorClassic.ConvertID(baseID, true));
                }
            }

            if (relic == null)
            {
                RelicLibraryPatches_GetRelic.AllowRecursion = false;
                relic = RelicLibrary.getRelic(baseID).makeCopy();
            }

            final CustomSavable<?> r = JUtils.SafeCast(relic, CustomSavable.class);
            if (sepIndex >= 0 && r != null && r.savedType() == Integer.class)
            {
                //noinspection CastCanBeRemovedNarrowingVariableType
                ((CustomSavable<Integer>)r).onLoad(JUtils.ParseInt(key.substring(sepIndex + 1), -1));
            }

            return relic;
        }

        return null;
    }

    public static void AddRelic(ArrayList<String> list, AbstractRelic relic)
    {
        if (relic instanceof EYBRelic && relic instanceof CustomSavable<?>)
        {
            final CustomSavable<?> r = JUtils.SafeCast(relic, CustomSavable.class);
            if (r != null && r.savedType() == Integer.class)
            {
                list.add(relic.relicId + SEPARATOR + r.onSave());
                return;
            }
        }

        list.add(relic.relicId);
    }

    @SpirePatch(clz = RelicLibrary.class, method = "getRelic", paramtypez = {String.class})
    public static class RelicLibraryPatches_GetRelic
    {
        public static boolean AllowRecursion = true;

        @SpirePrefixPatch
        public static SpireReturn<AbstractRelic> Prefix(String key)
        {
            if (AllowRecursion)
            {
                final AbstractRelic relic = GetRelic(key);
                return relic != null ? SpireReturn.Return(relic) : SpireReturn.Continue();
            }

            AllowRecursion = true;
            return SpireReturn.Continue();
        }
    }
}