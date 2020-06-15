package patches.abstractPlayer;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

@SpirePatch(clz= AbstractPlayer.class, method = "updateInput")
public class AbstractPlayer_UpdateInput
{
    private static final FieldInfo<AbstractMonster> _hoveredMonster = JUtils.GetField("hoveredMonster", AbstractPlayer.class);

    @SpirePostfixPatch
    public static void Method(AbstractPlayer __instance)
    {
        if ((__instance.isDraggingCard || __instance.isHoveringDropZone) && __instance.hoveredCard instanceof EYBCard)
        {
            ((EYBCard)__instance.hoveredCard).OnDrag(_hoveredMonster.Get(__instance));
        }
    }
}