package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.common.TaintedPower;
import eatyourbeets.utilities.GameActions;

public class DesecrationPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(DesecrationPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Dark;

    public DesecrationPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m)
    {
        int applyAmount = (int) GetChargeMultiplier();

        if (m != null && TryUse(c))
        {
            GameActions.Bottom.StackPower(new TaintedPower(m, applyAmount)).ShowEffect(true, true).IgnoreArtifact(true);
        }
    }

    @Override
    protected int GetMultiplierForDescription() {
        return (int) GetChargeIncrease(Math.max(amount,chargeThreshold));
    }

    @Override
    protected float GetChargeMultiplier() {
        return super.GetChargeMultiplier() * 2;
    }
}