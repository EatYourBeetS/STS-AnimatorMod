package patches.cardLibrary;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.animator.special.CardNotFound;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.AnimatorClassicCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

import java.util.HashMap;
import java.util.Map;

public class CardLibraryPatches
{
    private static final HashMap<String, String> _cardIDs = new HashMap<>();
    private static final FieldInfo<HashMap<String, AbstractCard>> _curses = JUtils.GetField("curses", CardLibrary.class);
    private static final byte[] whatever = {0x61, 0x6e, 0x69, 0x6d, 0x61, 0x74, 0x6f, 0x72, 0x3a, 0x75, 0x72, 0x3a};
    private static final String idPrefix = new String(whatever);

    public static AbstractCard TryReplace(AbstractResources resources, AbstractCard card)
    {
        if (card.cardID.startsWith(resources.Prefix))
        {
            return card;
        }
        else
        {
            final EYBCardData data = GR.CardLibrary.GetCardData(resources, card.cardID);
            return (data == null || data.IsCard(card)) ? card : data.CreateNewInstance(card.upgraded);
        }
    }

    public static void TryReplace(AbstractCard[] card)
    {
        final EYBCardData data = GR.CardLibrary.GetCurrentClassCardData(card[0].cardID);
        if (data != null && !data.IsCard(card[0]))
        {
            card[0] = data.CreateNewInstance(card[0].upgraded);
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCard", paramtypez = {String.class})
    public static class CardLibraryPatches_GetCard
    {
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

            if (GR.IsLoaded())
            {
                if (AnimatorCard_UltraRare.IsSeen(key))
                {
                    final AbstractCard card = AnimatorCard_UltraRare.GetCards().get(key);
                    if (card != null)
                    {
                        return SpireReturn.Return(card.makeCopy());
                    }
                }
                if (AnimatorClassicCard_UltraRare.IsSeen(key))
                {
                    final AbstractCard card = AnimatorClassicCard_UltraRare.GetCards().get(key);
                    if (card != null)
                    {
                        return SpireReturn.Return(card.makeCopy());
                    }
                }

                final EYBCardData data = GR.CardLibrary.GetCurrentClassCardData(key);
                if (data != null)
                {
                    return SpireReturn.Return(data.CreateNewInstance(false));
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCopy", paramtypez = {String.class, int.class, int.class})
    public static class CardLibraryPatches_GetCopy
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

        @SpirePostfixPatch
        public static AbstractCard Postfix(AbstractCard result, String key, int upgradeTime, int misc)
        {
            if (result instanceof Madness && !Madness.ID.equals(key))
            {
                result = new CardNotFound();
                GameUtilities.ChangeCardName(result, key);
            }

            return result;
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCurse", paramtypez = {})
    public static class CardLibraryPatches_GetCurse
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix()
        {
            return CardLibraryPatches_GetCurse2.Prefix(null, AbstractDungeon.cardRng);
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCurse", paramtypez = {AbstractCard.class, Random.class})
    public static class CardLibraryPatches_GetCurse2
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(AbstractCard ignore, Random rng)
        {
            final RandomizedList<String> cards = new RandomizedList<>();
            for (Map.Entry<String, AbstractCard> entry : _curses.Get(null).entrySet())
            {
                final AbstractCard c = entry.getValue();
                if (c.rarity != AbstractCard.CardRarity.SPECIAL && (ignore == null || !c.cardID.equals(ignore.cardID)))
                {
                    cards.Add(entry.getKey());
                }
            }

            return SpireReturn.Return(CardLibrary.cards.get(cards.Retrieve(rng)));
        }
    }
}
