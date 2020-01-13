package patches.cardRewardScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.ui.screens.CardRewardScreenPatch;

import java.util.ArrayList;

@SpirePatch(clz= CardRewardScreen.class, method="open")
public class CardRewardScreen_Open
{
    @SpirePostfixPatch
    public static void Postfix(CardRewardScreen __instance, ArrayList<AbstractCard> cards, RewardItem rItem, String header)
    {
        CardRewardScreenPatch.Open(__instance, cards, rItem, header);

        for (AbstractCard c : cards)
        {
            if (c instanceof AnimatorCard_UltraRare)
            {
                AnimatorCard_UltraRare.MarkAsSeen(c.cardID);
            }
        }
    }
}