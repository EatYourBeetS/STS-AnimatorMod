package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.utilities.GameActions;

public class VelocityPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(VelocityPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Green;

    public VelocityPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m)
    {
        int cardDraw = (int) GetChargeIncrease(amount);

        if (c.type == AbstractCard.CardType.SKILL && TryUse(c))
        {
            GameActions.Bottom.Draw(cardDraw);
        }
    }

    @Override
    protected int GetMultiplierForDescription() {
        return (int) GetChargeIncrease(Math.max(amount,chargeThreshold));
    }
}