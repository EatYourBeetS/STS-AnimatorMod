package patches.cardLibrary;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.resources.GR;

@SpirePatch(clz = CardLibrary.class, method = "getCard", paramtypez = {String.class})
public class CardLibrary_GetCard
{
    @SpirePrefixPatch
    public static SpireReturn<AbstractCard> Prefix(String key)
    {
        if (GR.IsLoaded && AnimatorCard_UltraRare.IsSeen(key))
        {
            AbstractCard card = AnimatorCard_UltraRare.GetCards().get(key);
            if (card != null)
            {
                return SpireReturn.Return(card.makeCopy());
            }
        }

        return SpireReturn.Continue();
    }
}