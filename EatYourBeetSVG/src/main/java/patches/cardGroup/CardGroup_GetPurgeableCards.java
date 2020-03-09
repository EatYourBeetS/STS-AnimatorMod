package patches.cardGroup;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.utilities.GameUtilities;

@SpirePatch(clz = CardGroup.class, method = "getPurgeableCards")
public class CardGroup_GetPurgeableCards
{
    @SpirePrefixPatch
    public static SpireReturn<CardGroup> Postfix(CardGroup __instance)
    {
        CardGroup result = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : __instance.group)
        {
            if (GameUtilities.CanRemoveFromDeck(c))
            {
                result.group.add(c);
            }
        }

        return SpireReturn.Return(result);
    }
}
