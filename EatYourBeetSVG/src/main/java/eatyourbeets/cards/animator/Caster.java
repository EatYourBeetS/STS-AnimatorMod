package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class Caster extends AnimatorCard_Boost
{
    public static final String ID = CreateFullID(Caster.class.getSimpleName());

    public Caster()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0,0, 1);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (this.secondaryValue > 0)
        {
            this.target = CardTarget.SELF_AND_ENEMY;
        }
        else
        {
            this.target = CardTarget.SELF;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new EvokeOrbAction(1));
        GameActionsHelper.ChannelOrb(new Dark(), true);

        if (ProgressBoost())
        {
            GameActionsHelper.ApplyPower(p, m, new StrengthPower(m, -this.magicNumber), -this.magicNumber);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    protected int GetBaseBoost()
    {
        return 2;
    }
}