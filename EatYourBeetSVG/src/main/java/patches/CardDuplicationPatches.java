package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.Duplicator;
import com.megacrit.cardcrawl.relics.DollysMirror;
import eatyourbeets.utilities.GameUtilities;

public class CardDuplicationPatches
{
    private static CardGroup MASTERDECK_CACHE;

    protected static void BeforeUse()
    {
        final CardGroup temp = new CardGroup(CardGroup.CardGroupType.MASTER_DECK);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (GameUtilities.CanObtainCopy(c))
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

    @SpirePatch(clz = DollysMirror.class, method = "onEquip")
    public static class CardDuplicationPatches_DollysMirror_OnEquip
    {
        @SpirePrefixPatch
        public static void Prefix(DollysMirror __instance)
        {
            BeforeUse();
        }

        @SpirePostfixPatch
        public static void Postfix(DollysMirror __instance)
        {
            AfterUse();
        }
    }

    @SpirePatch(clz = Duplicator.class, method = "use")
    public static class CardDuplicationPatches_Duplicator_Use
    {
        @SpirePrefixPatch
        public static void Prefix(Duplicator __instance)
        {
            BeforeUse();
        }

        @SpirePostfixPatch
        public static void Postfix(Duplicator __instance)
        {
            AfterUse();
        }
    }
}