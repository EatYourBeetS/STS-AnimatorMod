package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TechnicPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(TechnicPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Silver;

    public TechnicPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
        this.chargeThreshold = 20;
        this.experiencePerLevel = 20;
    }

    @Override
    public void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m)
    {
        int cardDraw = (int) GetChargeMultiplier();

        if ((c.type == AbstractCard.CardType.ATTACK || c.type == AbstractCard.CardType.SKILL) && c.costForTurn > 0 && !c.purgeOnUse && !c.hasTag(GR.Enums.CardTags.PURGE) && TryUse(c))
        {
            GameUtilities.ModifyCostForCombat(c,-1,true);
            GameActions.Bottom.GainEnergy(1);
        }
    }

    @Override
    protected int GetMultiplierForDescription() {
        return 1;
    }

    @Override
    protected float GetChargeIncrease(int charge) {
        return 1f;
    }

    @Override
    protected int GetCurrentChargeCost() {return chargeThreshold;}
}