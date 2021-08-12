package patches;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class RelicLibraryPatches
{
    public static final String SEPARATOR = "~";

    public static AbstractRelic GetRelic(String key)
    {
        final int sepIndex = key.indexOf(SEPARATOR);
        if (GR.IsLoaded && sepIndex >= 0)
        {
            final String baseID = key.substring(0, sepIndex);
            final AbstractRelic relic = RelicLibrary.getRelic(baseID).makeCopy();
            final CustomSavable<?> r = JUtils.SafeCast(relic, CustomSavable.class);
            if (r != null && r.savedType() == Integer.class)
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
        @SpirePrefixPatch
        public static SpireReturn<AbstractRelic> Prefix(String key)
        {
            final AbstractRelic relic = GetRelic(key);
            return relic != null ? SpireReturn.Return(relic) : SpireReturn.Continue();
        }
    }
}