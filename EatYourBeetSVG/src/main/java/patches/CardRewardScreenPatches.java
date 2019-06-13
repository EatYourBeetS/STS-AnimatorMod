package patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.ui.CardRewardScreenPatch;

import java.util.ArrayList;

public class CardRewardScreenPatches
{
    @SpirePatch(clz= CardRewardScreen.class, method="acquireCard")
    public static class CardRewardScreenPatch_AcquireCard
    {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance, AbstractCard hoveredCard)
        {
            CardRewardScreenPatch.AcquireCard(__instance, hoveredCard);
        }
    }

    @SpirePatch(clz= CardRewardScreen.class, method="onClose")
    public static class CardRewardScreenPatch_OnClose
    {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance)
        {
            CardRewardScreenPatch.OnClose(__instance);
        }
    }

    @SpirePatch(clz= CardRewardScreen.class, method="open")
    public static class CardRewardScreenPatch_Open
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

    @SpirePatch(clz= CardRewardScreen.class, method="render")
    public static class CardRewardScreenPatch_Render
    {
        @SpirePrefixPatch
        public static void Prefix(CardRewardScreen __instance, SpriteBatch sb)
        {
            CardRewardScreenPatch.PreRender(__instance, sb);
        }

        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance, SpriteBatch sb)
        {
            CardRewardScreenPatch.PostRender(__instance, sb);
        }
    }

    @SpirePatch(clz= CardRewardScreen.class, method="update")
    public static class CardRewardScreenPatch_Update
    {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance)
        {
            CardRewardScreenPatch.Update(__instance);
        }
    }
}