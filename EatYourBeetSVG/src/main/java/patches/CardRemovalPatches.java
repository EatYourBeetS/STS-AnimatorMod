package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.Falling;
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain;
import eatyourbeets.utilities.GameUtilities;

public class CardRemovalPatches
{
    private static CardGroup MASTERDECK_CACHE;

    protected static void BeforeUse()
    {
        final CardGroup temp = new CardGroup(CardGroup.CardGroupType.MASTER_DECK);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (GameUtilities.CanRemoveFromDeck(c))
            {
                temp.group.add(c);
            }
        }

        MASTERDECK_CACHE = AbstractDungeon.player.masterDeck;
        AbstractDungeon.player.masterDeck = temp;
    }

    protected static void AfterUse()
    {
        AbstractDungeon.player.masterDeck = MASTERDECK_CACHE;
    }

    @SpirePatch(clz = WeMeetAgain.class, method = SpirePatch.CONSTRUCTOR)
    public static class CardRemovalPatches_WeMeetAgain_Ctor
    {
        @SpirePrefixPatch
        public static void Prefix(WeMeetAgain __instance)
        {
            BeforeUse();
        }

        @SpirePostfixPatch
        public static void Postfix(WeMeetAgain __instance)
        {
            AfterUse();
        }
    }

    @SpirePatch(clz = Falling.class, method = SpirePatch.CONSTRUCTOR)
    public static class CardRemovalPatches_FallingCtor
    {
        @SpirePrefixPatch
        public static void Prefix(Falling __instance)
        {
            BeforeUse();
        }

        @SpirePostfixPatch
        public static void Postfix(Falling __instance)
        {
            AfterUse();
        }
    }
}