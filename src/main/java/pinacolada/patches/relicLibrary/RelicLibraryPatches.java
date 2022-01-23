package pinacolada.patches.relicLibrary;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.resources.animator.AnimatorResources;
import pinacolada.relics.PCLRelic;
import pinacolada.relics.PCLReplacementRelic;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLResources;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class RelicLibraryPatches
{
    private static final String AnimatorBetaID = "animatorbeta";
    public static final String SEPARATOR = "~";

    public static boolean IsARelic(String relicID) {
        return (RelicLibrary.isARelic(relicID) || PCLReplacementRelic.RELICS.containsKey(relicID));
    }

    public static String GetEYBRelicReplacement(String relicID)
    {
        // Attempt to find the Animator replacement for PCL relics, then the AnimatorBeta replacement
        if (relicID.startsWith(PCLResources.ID)) {
            String replacementID = relicID.replace(PCLResources.ID, AnimatorResources.ID);
            if (IsARelic(replacementID)) {
                return replacementID;
            }
            replacementID = relicID.replace(PCLResources.ID, AnimatorBetaID);
            if (IsARelic(replacementID)) {
                return replacementID;
            }
        }

        return null;
    }

    public static String GetPCLRelicReplacement(String relicID)
    {
        // Attempt to find the PCL replacement for AnimatorBeta relics
        if (relicID.startsWith(AnimatorBetaID)) {
            String replacementID = relicID.replace(AnimatorBetaID, PCLResources.ID);
            if (IsARelic(replacementID)) {
                return replacementID;
            }
        }

        // Attempt to find the PCL replacement for EYB/Animator relics
        if (relicID.startsWith(AnimatorResources.ID)) {
            String replacementID = relicID.replace(AnimatorResources.ID, PCLResources.ID);
            if (IsARelic(replacementID)) {
                return replacementID;
            }
        }

        return null;
    }

    public static AbstractRelic GetRelic(String key)
    {
        if (GR.IsLoaded)
        {
            // Do not replace customsavable relics
            final int sepIndex = key.indexOf(SEPARATOR);
            if (sepIndex >= 0) {
                final String baseID = key.substring(0, sepIndex);
                final AbstractRelic relic = RelicLibrary.getRelic(baseID).makeCopy();
                final CustomSavable<?> r = PCLJUtils.SafeCast(relic, CustomSavable.class);
                if (r != null && r.savedType() == Integer.class)
                {
                    //noinspection CastCanBeRemovedNarrowingVariableType
                    ((CustomSavable<Integer>)r).onLoad(PCLJUtils.ParseInt(key.substring(sepIndex + 1), -1));
                }

                return relic;
            }

            String newKey = key;
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass)) {
                newKey = GetPCLRelicReplacement(key);
                if (PCLReplacementRelic.RELICS.containsKey(key)) {
                    return PCLReplacementRelic.RELICS.get(key).makeCopy();
                }
            }
            else if (PCLGameUtilities.IsPlayerClass(eatyourbeets.resources.GR.Animator.PlayerClass)) {
                newKey = GetEYBRelicReplacement(key);
            }
            if (newKey != null && !key.equals(newKey)) {
                return RelicLibrary.getRelic(newKey).makeCopy();
            }
        }

        return null;
    }

    public static void AddRelic(ArrayList<String> list, AbstractRelic relic)
    {
        if (relic instanceof PCLRelic && relic instanceof CustomSavable<?>)
        {
            final CustomSavable<?> r = PCLJUtils.SafeCast(relic, CustomSavable.class);
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
            AbstractRelic relic = GetRelic(key);
            return relic != null ? SpireReturn.Return(relic) : SpireReturn.Continue();
        }
    }
}