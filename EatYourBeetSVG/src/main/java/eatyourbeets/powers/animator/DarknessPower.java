package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.green.Adrenaline;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.cards.animator.DarknessAdrenaline;
import eatyourbeets.utilities.GameActionsHelper;
import patches.AbstractEnums;

public class DarknessPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(DarknessPower.class.getSimpleName());

    public DarknessPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public int onLoseHp(int damageAmount)
    {
        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        GameActionsHelper.AddToDefault(new ReducePowerAction(owner, owner, this, 1));
        GameActionsHelper.MakeCardInDrawPile(new DarknessAdrenaline(), 1, false);

        GameActionsHelper.ResetOrder();

        return super.onLoseHp(damageAmount);
    }
}
