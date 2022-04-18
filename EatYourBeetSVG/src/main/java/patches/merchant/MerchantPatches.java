package patches.merchant;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.Merchant;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;

public class MerchantPatches
{
    @SpirePatch(clz = Merchant.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {float.class, float.class, int.class})
    public static class MerchantPatches_initCards
    {
        private static final HashMap<Merchant, MerchantData> map = new HashMap<>();

        public static class MerchantData
        {
            public CardGroup colorless;
            public CardGroup common;
            public CardGroup uncommon;
            public CardGroup rare;

            protected CardGroup GetReplacement(CardGroup group)
            {
                final CardGroup replacement = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
                for (AbstractCard c : group.group)
                {
                    final EYBCard card = JUtils.SafeCast(c, EYBCard.class);
                    if (card != null && card.cardData.ShouldCancel())
                    {
                        continue;
                    }
                    if (c instanceof OnAddingToCardRewardListener && ((OnAddingToCardRewardListener)c).ShouldCancel())
                    {
                        continue;
                    }

                    replacement.group.add(c);
                }

                return replacement;
            }
        }

        @SpirePrefixPatch
        public static void Prefix(Merchant __instance, float x, float y, int newShopScreen)
        {
            final MerchantData data = new MerchantData();
            map.put(__instance, data);

            data.colorless = AbstractDungeon.colorlessCardPool;
            AbstractDungeon.colorlessCardPool = data.GetReplacement(data.colorless);

            data.common = AbstractDungeon.commonCardPool;
            AbstractDungeon.commonCardPool = data.GetReplacement(data.common);

            data.uncommon = AbstractDungeon.uncommonCardPool;
            AbstractDungeon.uncommonCardPool = data.GetReplacement(data.uncommon);

            data.rare = AbstractDungeon.rareCardPool;
            AbstractDungeon.rareCardPool = data.GetReplacement(data.rare);
        }

        @SpirePostfixPatch
        public static void Postfix(Merchant __instance, float x, float y, int newShopScreen)
        {
            final MerchantData data = map.remove(__instance);

            AbstractDungeon.colorlessCardPool = data.colorless;
            AbstractDungeon.commonCardPool = data.common;
            AbstractDungeon.uncommonCardPool = data.uncommon;
            AbstractDungeon.rareCardPool = data.rare;
        }
    }
}
