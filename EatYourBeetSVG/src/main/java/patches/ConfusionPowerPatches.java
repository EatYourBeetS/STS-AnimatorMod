package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ConfusionPower;

public class ConfusionPowerPatches
{
    @SpirePatch(clz = ConfusionPower.class, method = "onCardDraw", paramtypez = {AbstractCard.class})
    public static class SoulPatches_Discard
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(ConfusionPower power, AbstractCard card)
        {
            if (card.cost > 3)
            {
                int newCost = AbstractDungeon.cardRandomRng.random(card.cost);
                if (card.costForTurn != newCost)
                {
                    card.costForTurn = newCost;
                    card.isCostModified = (card.costForTurn != card.cost);
                }

                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }
}

