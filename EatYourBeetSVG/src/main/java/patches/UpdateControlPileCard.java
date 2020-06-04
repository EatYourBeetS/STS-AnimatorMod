package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.ControllableCard;

//These patches are needed because calling applyPowers and glowcheck in OnPhaseChange wasn't good enough
public class UpdateControlPileCard {

    @SpirePatch(
            clz = CardGroup.class,
            method = "applyPowers"
    )
    public static class ApplyPowers
    {
        @SpirePostfixPatch
        public static void apply(CardGroup __instance)
        {
            for (ControllableCard c : CombatStats.ControlPile.controllers) {
                AbstractCard card = c.card;
                card.applyPowers();
            }
        }
    }
    @SpirePatch(
            clz = CardGroup.class,
            method = "glowCheck"
    )
    public static class GlowCheck
    {
        @SpirePostfixPatch
        public static void apply(CardGroup __instance)
        {
            for (ControllableCard c : CombatStats.ControlPile.controllers) {
                AbstractCard card = c.card;
                glowCheck(card);
                card.triggerOnGlowCheck();
            }
        }
    }

    public static void glowCheck(AbstractCard c)
    {
        if (c.canUse(AbstractDungeon.player, null) && !AbstractDungeon.isScreenUp) {
            c.beginGlowing();
        } else {
            c.stopGlowing();
        }
    }
}