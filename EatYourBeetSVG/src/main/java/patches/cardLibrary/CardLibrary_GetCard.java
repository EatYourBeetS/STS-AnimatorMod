package patches.cardLibrary;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;

@SpirePatch(clz = CardLibrary.class, method = "getCard", paramtypez = {String.class})
public class CardLibrary_GetCard
{
    @SpirePrefixPatch
    public static SpireReturn<AbstractCard> Prefix(String key)
    {
        if (key.startsWith("animator:ur:"))
        {
            String series = key.replace("animator:ur:", "").toLowerCase();
            for (AnimatorCard_UltraRare card : AnimatorCard_UltraRare.GetCards().values())
            {
                if (card.synergy.Name.toLowerCase().equals(series) && AnimatorCard_UltraRare.IsSeen(card.cardID))
                {
                    return SpireReturn.Return(card.makeCopy());
                }
            }
        }
        else if (AnimatorCard_UltraRare.IsSeen(key))
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