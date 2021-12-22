package pinacolada.patches.animator;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.JUtils;
import pinacolada.cards.base.PCLCard;
import pinacolada.powers.PCLCombatStats;

public class CombatStatsPatches
{
    @SpirePatch(clz = CombatStats.class, method = "atEndOfTurn")
    public static class CombatStatsPatches_AtEndOfTurn
    {
        @SpirePostfixPatch
        public static void Postfix(CombatStats __instance)
        {
            PCLCombatStats.AtEndOfTurn();
        }
    }

    @SpirePatch(clz = CombatStats.class, method = "atStartOfTurn")
    public static class CombatStatsPatches_AtStartOfTurn
    {
        @SpirePostfixPatch
        public static void Postfix(CombatStats __instance)
        {
            PCLCombatStats.AtStartOfTurn();
        }
    }

    @SpirePatch(clz = CombatStats.class, method = "onAfterCardPlayed")
    public static class CombatStatsPatches_OnAfterCardPlayed
    {
        @SpirePostfixPatch
        public static void Postfix(CombatStats __instance, AbstractCard card)
        {
            PCLCombatStats.OnAfterCardPlayedPostActions(card);
        }
    }

    @SpirePatch(clz = CombatStats.class, method = "onAfterUseCard")
    public static class CombatStatsPatches_OnAfterUseCard
    {
        @SpirePostfixPatch
        public static void Postfix(CombatStats __instance, AbstractCard card, UseCardAction action)
        {
            PCLCombatStats.OnAfterUseCardPostActions(card);
        }
    }

    @SpirePatch(clz = CombatStats.class, method = "OnCardCreated")
    public static class CombatStatsPatches_OnCardCreated
    {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard card, boolean startOfBattle)
        {

            PCLCard c = (PCLCard) JUtils.SafeCast(card, PCLCard.class);
            if (c != null) {
                c.triggerWhenCreated(startOfBattle);
            }
        }
    }

    @SpirePatch(clz = CombatStats.class, method = "onDeath")
    public static class CombatStatsPatches_OnDeath
    {
        @SpirePostfixPatch
        public static void Postfix(CombatStats __instance)
        {
            PCLCombatStats.OnDeath();
        }
    }


    @SpirePatch(clz = CombatStats.class, method = "onPlayCard")
    public static class CombatStatsPatches_OnPlayCard
    {
        @SpirePostfixPatch
        public static void Postfix(CombatStats __instance, AbstractCard card, AbstractMonster m)
        {
            PCLCombatStats.OnPlayCardPostActions(card);
        }
    }

    @SpirePatch(clz = CombatStats.class, method = "onVictory")
    public static class CombatStatsPatches_OnVictory
    {
        @SpirePostfixPatch
        public static void Postfix(CombatStats __instance)
        {
            PCLCombatStats.OnVictory();
        }
    }

}
