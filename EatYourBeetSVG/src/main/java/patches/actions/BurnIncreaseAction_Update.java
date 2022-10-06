package patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.unique.BurnIncreaseAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;

@SpirePatch(clz = BurnIncreaseAction.class, method = "update")
public class BurnIncreaseAction_Update
{
    @SpirePostfixPatch
    public static void Postfix(BurnIncreaseAction __instance)
    {
        if (__instance.isDone)
        {
            for (AbstractCard c : AbstractDungeon.player.drawPile.group)
            {
                UpgradeBurn(c);
            }

            for (AbstractCard c : AbstractDungeon.player.discardPile.group)
            {
                UpgradeBurn(c);
            }
        }
    }

    private static void UpgradeBurn(AbstractCard card)
    {
        if (card instanceof EYBCard && !card.upgraded && Burn.ID.equals(GR.CardLibrary.GetBaseID(card.cardID)))
        {
            card.upgrade();
        }
    }
}
