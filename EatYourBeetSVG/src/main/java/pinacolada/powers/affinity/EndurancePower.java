package pinacolada.powers.affinity;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;

public class EndurancePower extends AbstractPCLAffinityPower
{
    public static final String POWER_ID = CreateFullID(EndurancePower.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Orange;

    public EndurancePower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        return isActive ? blockAmount * GetEffectiveMultiplier() : blockAmount;
    }

    @Override
    public void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m)
    {
        if (c.baseBlock > 0 && isActive)
        {
            isActive = false;
            flash();
        }
    }
}