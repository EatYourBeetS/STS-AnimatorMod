package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.ui.CardRewardScreenPatch;

import java.util.ArrayList;

@SpirePatch(clz= CardRewardScreen.class, method="open")
public class CardRewardScreenPatch_Open
{
    @SpirePostfixPatch
    public static void _(CardRewardScreen __instance, ArrayList<AbstractCard> cards, RewardItem rItem, String header)
    {
        CardRewardScreenPatch.Open(__instance, cards, rItem, header);
    }
}