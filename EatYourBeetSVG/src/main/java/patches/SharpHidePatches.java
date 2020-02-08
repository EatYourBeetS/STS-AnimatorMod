package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.SharpHidePower;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.JavaUtilities;

@SpirePatch(clz = SharpHidePower.class, method = "onUseCard")
public class SharpHidePatches
{
    @SpirePrefixPatch
    public static SpireReturn Prefix(SharpHidePower __instance, AbstractCard card, UseCardAction action)
    {
        EYBCard c = JavaUtilities.SafeCast(card, EYBCard.class);
        if (c != null && c.attackType != null && c.attackType.bypassThorns)
        {
            return SpireReturn.Return(null);
        }
        else
        {
            return SpireReturn.Continue();
        }
    }
}
