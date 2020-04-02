package patches.cardLibrary;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

@SpirePatch(clz = CardLibrary.class, method = "getCard", paramtypez = {String.class})
public class CardLibrary_GetCard
{
    private static final byte[] whatever = {0x61, 0x6e, 0x69, 0x6d, 0x61, 0x74, 0x6f, 0x72, 0x3a, 0x75, 0x72, 0x3a};
    private static final String idPrefix = new String(whatever);

    @SpirePrefixPatch
    public static SpireReturn<AbstractCard> Prefix(String key)
    {
        if (key.startsWith(idPrefix))
        {
            AnimatorLoadout loadout = GR.Animator.Data.GetByName(key.replace(idPrefix, ""));
            if (loadout != null && loadout.GetUltraRare() != null)
            {
                key = loadout.GetUltraRare().ID;
            }
        }

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