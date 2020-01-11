package patches.cardLibrary;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;

@SpirePatch(clz = CardLibrary.class, method = "getCopy", paramtypez = {String.class, int.class, int.class})
public class CardLibrary_GetCopy
{
    @SpirePrefixPatch
    public static SpireReturn<AbstractCard> Prefix(String key, int upgradeTime, int misc)
    {
        AbstractCard card = AnimatorCard_UltraRare.GetCards().get(key);
        if (card != null)
        {
            card = card.makeCopy();
            card.misc = misc;

            for (int i = 0; i < upgradeTime; ++i)
            {
                card.upgrade();
            }

            return SpireReturn.Return(card);
        }

        return SpireReturn.Continue();
    }
}