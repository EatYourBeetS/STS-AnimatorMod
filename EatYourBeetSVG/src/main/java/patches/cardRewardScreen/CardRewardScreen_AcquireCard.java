package patches.cardRewardScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.ui.CardRewardScreenPatch;

@SpirePatch(clz= CardRewardScreen.class, method="acquireCard")
public class CardRewardScreen_AcquireCard
{
    @SpirePostfixPatch
    public static void Postfix(CardRewardScreen __instance, AbstractCard hoveredCard)
    {
        CardRewardScreenPatch.AcquireCard(__instance, hoveredCard);
    }
}