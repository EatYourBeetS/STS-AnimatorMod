package patches.cardGroup;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.interfaces.subscribers.OnRemoveFromDeckSubscriber;

@SpirePatch(clz = CardGroup.class, method = "removeCard", paramtypez = {AbstractCard.class})
public class CardGroup_RemoveCard
{
    public static void Postfix(CardGroup __instance, AbstractCard c)
    {
        if (__instance.type == CardGroupType.MASTER_DECK)
        {
            OnRemoveFromDeckSubscriber card = JUtils.SafeCast(c, OnRemoveFromDeckSubscriber.class);
            if (card != null)
            {
                card.OnRemoveFromDeck();
            }
        }
    }
}
