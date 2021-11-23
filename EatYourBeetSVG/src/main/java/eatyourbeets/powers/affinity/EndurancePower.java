package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;

public class EndurancePower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(EndurancePower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Orange;

    public EndurancePower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        return enabled ? blockAmount * GetChargeMultiplier() : blockAmount;
    }

    @Override
    public void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m)
    {
        if (c.baseBlock > 0)
        {
            TryUse(c);
        }
    }
}