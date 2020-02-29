package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ConfusionPower;

// If card cost is greater than 3, randomize its cost between 0 and its base cost, instead of 0-3
@SpirePatch(clz = ConfusionPower.class, method = "onCardDraw", paramtypez = {AbstractCard.class})
public class ConfusionPowerPatches
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