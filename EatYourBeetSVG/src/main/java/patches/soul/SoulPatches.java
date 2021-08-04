package patches.soul;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnAddedToDrawPileSubscriber;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class SoulPatches
{
    @SpirePatch(clz = Soul.class, method = "update")
    public static class SoulPatches_Update
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getMethodName().equals("applyPowers"))
                    {
                        m.replace("eatyourbeets.utilities.GameUtilities.RefreshHandLayout(true);");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = Soul.class, method = "discard", paramtypez = {AbstractCard.class, boolean.class})
    public static class SoulPatches_Discard
    {
        @SpirePostfixPatch
        public static void Postfix(Soul soul, AbstractCard card, boolean visualOnly)
        {
            if (card != null)
            {
                if (card.hasTag(GR.Enums.CardTags.LOYAL)) // For reasons, this also needs to work when visualOnly...
                {
                    soul.isReadyForReuse = true;
                    AbstractDungeon.player.discardPile.moveToDeck(card, true);
                }
                else if (card.hasTag(GR.Enums.CardTags.PURGING) && !visualOnly)
                {
                    soul.isReadyForReuse = true;
                    AbstractDungeon.player.discardPile.removeCard(card);
                    card.tags.remove(GR.Enums.CardTags.PURGING);
                }
            }
        }
    }

    @SpirePatch(clz = Soul.class, method = "obtain", paramtypez = {AbstractCard.class})
    public static class SoulPatches_Obtain
    {
        public static void Postfix(Soul soul, AbstractCard card)
        {
            GR.Animator.Dungeon.OnCardObtained(card);
        }
    }

    @SpirePatch(clz = Soul.class, method = "onToBottomOfDeck", paramtypez = {AbstractCard.class})
    public static class SoulPatches_OnToBottomOfDeck
    {
        @SpirePostfixPatch
        public static void Postfix(Soul soul, AbstractCard card)
        {
            if (card instanceof OnAddedToDrawPileSubscriber)
            {
                ((OnAddedToDrawPileSubscriber) card).OnAddedToDrawPile(false, CardSelection.Mode.Bottom);
            }
        }
    }

    @SpirePatch(clz = Soul.class, method = "onToDeck", paramtypez = {AbstractCard.class, boolean.class, boolean.class})
    public static class SoulPatches_OnToDeck
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
}
