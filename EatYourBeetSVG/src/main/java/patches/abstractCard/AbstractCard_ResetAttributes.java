package patches.abstractCard;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.powers.CombatStats;

@SpirePatch(clz = AbstractCard.class, method = "resetAttributes")
public class AbstractCard_ResetAttributes
{
    @SpirePostfixPatch
    public static void Method(AbstractCard __instance)
    {
        CombatStats.OnCardReset(__instance);
    }
}