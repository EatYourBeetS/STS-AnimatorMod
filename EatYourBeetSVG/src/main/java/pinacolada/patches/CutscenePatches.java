package pinacolada.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class CutscenePatches
{
    private static final FieldInfo<Boolean> _isDone = PCLJUtils.GetField("isDone", Cutscene.class);
    private static final FieldInfo<ArrayList<CutscenePanel>> _panels = PCLJUtils.GetField("panels", Cutscene.class);

    @SpirePatch(clz = Cutscene.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractPlayer.PlayerClass.class})
    public static class CutscenePatches_ctor
    {
        @SpirePostfixPatch
        public static void Postfix(Cutscene __instance, AbstractPlayer.PlayerClass playerClass)
        {
            if (playerClass == GR.PCL.PlayerClass)
            {
                _isDone.Set(__instance, true);
                _panels.Get(__instance).clear();
            }
        }
    }
}
