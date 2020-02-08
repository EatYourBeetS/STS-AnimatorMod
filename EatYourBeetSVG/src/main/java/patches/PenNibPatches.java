package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.PenNibPower;
import eatyourbeets.resources.GR;

@SpirePatch(clz = PenNibPower.class, method = "onUseCard")
public class PenNibPatches
{
    @SpirePrefixPatch
    public static SpireReturn Prefix(PenNibPower __instance, AbstractCard card, UseCardAction action)
    {
        if (card == null || card.hasTag(GR.Enums.CardTags.IGNORE_PEN_NIB))
        {
            return SpireReturn.Return(null);
        }
        else
        {
            return SpireReturn.Continue();
        }
    }
}
