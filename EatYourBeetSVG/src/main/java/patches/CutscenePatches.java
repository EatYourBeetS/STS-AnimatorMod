package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class CutscenePatches
{
    private static final FieldInfo<Boolean> _isDone = JUtils.GetField("isDone", Cutscene.class);
    private static final FieldInfo<ArrayList<CutscenePanel>> _panels = JUtils.GetField("panels", Cutscene.class);

    @SpirePatch(clz = Cutscene.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractPlayer.PlayerClass.class})
    public static class CutscenePatches_ctor
    {
        @SpirePostfixPatch
        public static void Postfix(Cutscene __instance, AbstractPlayer.PlayerClass playerClass)
        {
            if (playerClass == GR.Animator.PlayerClass)
            {
                _isDone.Set(__instance, true);
                _panels.Get(__instance).clear();
            }
        }
    }
}
