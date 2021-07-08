package patches.abstractCard;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;

public class AbstractCardPatches
{
    @SpirePatch(clz = AbstractCard.class, method = "canUse", paramtypez = {AbstractPlayer.class, AbstractMonster.class})
    public static class AbstractCard_CanUse
    {
        @SpirePostfixPatch
        public static boolean Method(boolean __result, AbstractCard __instance, AbstractPlayer p, AbstractMonster m)
        {
            return CombatStats.OnTryUseCard(__instance, p, m, __result);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "resetAttributes")
    public static class AbstractCard_ResetAttributes
    {
        @SpirePostfixPatch
        public static void Method(AbstractCard __instance)
        {
            CombatStats.OnCardReset(__instance);
        }
    }
}