package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SuperchargePower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(SuperchargePower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Light;

    public SuperchargePower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        final AbstractCard last = GameUtilities.GetLastCardPlayed(true, 1);
        int applyAmount = (int) (power.amount * (GetChargeMultiplier() - 1));

        if (GameUtilities.IsPlayer(power.owner) && GameUtilities.IsCommonBuff(power) && TryUse(last)) {
            GameActions.Last.Callback(() -> {
                AbstractPower po = GameUtilities.GetPower(power.owner, power.ID);
                if (po != null) {
                    po.amount += applyAmount;
                    po.flash();
                    po.updateDescription();
                }
            });

            final AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
            if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
            {
                action.amount *= GetChargeMultiplier();
            }
        }
    }
}