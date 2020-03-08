package patches.soul;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import eatyourbeets.actions.cardManipulation.MakeTempCard;
import eatyourbeets.interfaces.subscribers.OnAddedToDrawPileSubscriber;

@SpirePatch(clz = Soul.class, method = "onToBottomOfDeck", paramtypez = {AbstractCard.class})
public class Soul_OnToBottomOfDeck
{
    @SpirePostfixPatch
    public static void Postfix(Soul soul, AbstractCard card)
    {
        if (card instanceof OnAddedToDrawPileSubscriber)
        {
            ((OnAddedToDrawPileSubscriber) card).OnAddedToDrawPile(false, MakeTempCard.Destination.Bottom);
        }
    }
}
