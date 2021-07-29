package patches.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.ui.animator.cardReward.AnimatorCardRewardScreen;

import java.util.ArrayList;

public class CardRewardScreenPatches
{
    private static final AnimatorCardRewardScreen screen = AnimatorCardRewardScreen.Instance;

    @SpirePatch(clz= CardRewardScreen.class, method="update")
    public static class CardRewardScreen_Update
    {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance)
        {
            screen.Update();
        }
    }

    @SpirePatch(clz= CardRewardScreen.class, method="render")
    public static class CardRewardScreen_Render
    {
        @SpirePrefixPatch
        public static void Prefix(CardRewardScreen __instance, SpriteBatch sb)
        {
            screen.PreRender(sb);
        }

        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance, SpriteBatch sb)
        {
            screen.Render(sb);
        }
    }

    @SpirePatch(clz= CardRewardScreen.class, method="draftOpen")
    public static class CardRewardScreen_DraftOpen
    {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance)
        {
            screen.Open(null, null, null);
        }
    }

    @SpirePatch(clz= CardRewardScreen.class, method="chooseOneOpen", paramtypez = {ArrayList.class})
    public static class CardRewardScreen_ChooseOneOpen
    {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance, ArrayList<AbstractCard> choices)
        {
            screen.Open(choices, null, null);
        }
    }

    @SpirePatch(clz= CardRewardScreen.class, method="customCombatOpen", paramtypez = {ArrayList.class, String.class, boolean.class})
    public static class CardRewardScreen_CustomCombatOpen
    {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance, ArrayList<AbstractCard> choices, String text, boolean skippable)
        {
            screen.Open(choices, null, text);
        }
    }

    @SpirePatch(clz= CardRewardScreen.class, method="open", paramtypez = {ArrayList.class, RewardItem.class, String.class})
    public static class CardRewardScreen_Open
    {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance, ArrayList<AbstractCard> cards, RewardItem rItem, String header)
        {
            screen.Open(cards, rItem, header);

            for (AbstractCard c : cards)
            {
                if (c instanceof AnimatorCard_UltraRare)
                {
                    AnimatorCard_UltraRare.MarkAsSeen(c.cardID);
                }
            }
        }
    }

    @SpirePatch(clz= CardRewardScreen.class, method="onClose")
    public static class CardRewardScreen_OnClose
    {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance)
        {
            screen.Close();
        }
    }

    @SpirePatch(clz= CardRewardScreen.class, method="acquireCard")
    public static class CardRewardScreen_AcquireCard
    {
        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance, AbstractCard hoveredCard)
        {
            screen.OnCardObtained(hoveredCard);
        }
    }
}
