package patches.soul;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.interfaces.subscribers.OnAddedToDrawPileSubscriber;

@SpirePatch(clz = Soul.class, method = "onToDeck", paramtypez = {AbstractCard.class, boolean.class, boolean.class})
public class Soul_OnToDeck
{
    @SpirePostfixPatch
    public static void Postfix(Soul soul, AbstractCard card, boolean randomSpot, boolean visualOnly)
    {
        if (card instanceof OnAddedToDrawPileSubscriber)
        {
            ((OnAddedToDrawPileSubscriber) card).OnAddedToDrawPile(visualOnly, randomSpot ? CardSelection.Mode.Random : CardSelection.Mode.Top);
        }
    }
}
