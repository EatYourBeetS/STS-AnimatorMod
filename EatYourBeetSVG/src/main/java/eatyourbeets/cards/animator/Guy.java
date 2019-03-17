package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Guy extends AnimatorCard
{
    public static final String ID = CreateFullID(Guy.class.getSimpleName());

    public Guy()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(0,0, 5);

        this.baseSecondaryValue = this.secondaryValue = 2;

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
        GameActionsHelper.AddToBottom(new GainBlockAction(m, p, this.magicNumber));
        GameActionsHelper.ApplyPower(p, m, new WeakPower(m, this.secondaryValue, false), this.secondaryValue);
        GameActionsHelper.CycleCardAction(1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-2);
            upgradeSecondaryValue(1);
        }
    }
}