package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Guy extends AnimatorCard
{
    public static final String ID = CreateFullID(Guy.class.getSimpleName());

    public Guy()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);

        Initialize(0,0, 3);

        this.baseSecondaryValue = this.secondaryValue = 1;

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper.GainEnergy(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.AddToBottom(new GainBlockAction(m1, p, this.magicNumber));
            GameActionsHelper.ApplyPower(p, m1, new WeakPower(m1, this.secondaryValue, false), this.secondaryValue);
        }

        GameActionsHelper.CycleCardAction(1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }
}