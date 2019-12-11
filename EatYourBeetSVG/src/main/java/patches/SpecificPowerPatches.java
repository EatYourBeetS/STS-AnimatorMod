package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.SharpHidePower;

public class SpecificPowerPatches
{
    @SpirePatch(clz = SharpHidePower.class, method = "onUseCard")
    public static class SharpHidePatches
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(SharpHidePower __instance, AbstractCard card, UseCardAction action)
        {
            if (card.hasTag(AbstractEnums.CardTags.PIERCING))
            {
                return SpireReturn.Return(null);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }
}