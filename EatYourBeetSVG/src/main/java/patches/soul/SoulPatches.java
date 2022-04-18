package patches.soul;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.subscribers.OnAddedToDrawPileSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.ListSelection;
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
                    card.tags.remove(GR.Enums.CardTags.PURGING);
                    AbstractDungeon.player.discardPile.removeCard(card);
                    CombatStats.OnCardPurged(card);
                }
            }
        }
    }

    @SpirePatch(clz = SoulGroup.class, method = "obtain", paramtypez = {AbstractCard.class, boolean.class})
    public static class SoulGroupPatches_Obtain
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(SoulGroup __instance, AbstractCard card, boolean obtain)
        {
            if (obtain && !GR.Animator.Dungeon.TryObtainCard(card))
            {
                SFX.Play(SFX.CARD_BURN, 0.8f, 1.2f, 0.5f);
                GameEffects.TopLevelQueue.Add(new CardPoofEffect(card.current_x, card.current_y));
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = Soul.class, method = "obtain", paramtypez = {AbstractCard.class})
    public static class SoulPatches_Obtain
    {
        @SpirePostfixPatch
        public static void Postfix(Soul __instance, AbstractCard card)
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
                ((OnAddedToDrawPileSubscriber) card).OnAddedToDrawPile(false, ListSelection.Mode.First);
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
                ((OnAddedToDrawPileSubscriber) card).OnAddedToDrawPile(visualOnly, randomSpot ? ListSelection.Mode.Random : ListSelection.Mode.Last);
            }
        }
    }
}
