package patches.cardLibrary;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.animator.curse.*;
import eatyourbeets.cards.animator.status.Status_Dazed;
import eatyourbeets.cards.animator.status.Status_Slimed;
import eatyourbeets.cards.animator.status.Status_Void;
import eatyourbeets.cards.animator.status.Status_Wound;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
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
    private static final FieldInfo<HashMap<String, AbstractCard>> _curses = JUtils.GetField("curses", CardLibrary.class);
    private static final byte[] whatever = {0x61, 0x6e, 0x69, 0x6d, 0x61, 0x74, 0x6f, 0x72, 0x3a, 0x75, 0x72, 0x3a};
    private static final String idPrefix = new String(whatever);

    public static EYBCardData GetReplacement(String cardID)
    {
        switch (cardID)
        {
            case AscendersBane.ID: return Curse_AscendersBane.DATA;
            case Clumsy.ID: return Curse_Clumsy.DATA;
            case Decay.ID: return Curse_Decay.DATA;
            case Doubt.ID: return Curse_Doubt.DATA;
            case Injury.ID: return Curse_Injury.DATA;
            case Normality.ID: return Curse_Normality.DATA;
            case Pain.ID: return Curse_Pain.DATA;
            case Parasite.ID: return Curse_Parasite.DATA;
            case Regret.ID: return Curse_Regret.DATA;
            case Shame.ID: return Curse_Shame.DATA;
            case Writhe.ID: return Curse_Writhe.DATA;
            case Dazed.ID: return Status_Dazed.DATA;
            case Miracle.ID: return eatyourbeets.cards.animator.special.Miracle.DATA;
            case Slimed.ID: return Status_Slimed.DATA;
            case VoidCard.ID: return Status_Void.DATA;
            case Wound.ID: return Status_Wound.DATA;

            default: return null;
        }
    }

    public static void TryReplace(AbstractCard[] card)
    {
        if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
        {
            final EYBCardData data = GetReplacement(card[0].cardID);
            if (data != null)
            {
                card[0] = data.MakeCopy(card[0].upgraded);
            }
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

            if (GR.IsLoaded)
            {
                if (AnimatorCard_UltraRare.IsSeen(key))
                {
                    final AbstractCard card = AnimatorCard_UltraRare.GetCards().get(key);
                    if (card != null)
                    {
                        return SpireReturn.Return(card.makeCopy());
                    }
                }
                else if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
                {
                    final EYBCardData data = GetReplacement(key);
                    if (data != null)
                    {
                        return SpireReturn.Return(data.MakeCopy(false));
                    }
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
            else if (key.equals(AscendersBane.ID) && GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
            {
                return SpireReturn.Return(Curse_AscendersBane.DATA.MakeCopy(false));
            }

            return SpireReturn.Continue();
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
