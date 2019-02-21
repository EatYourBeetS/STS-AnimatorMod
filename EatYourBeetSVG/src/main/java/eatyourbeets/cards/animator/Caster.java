package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Caster extends AnimatorCard
{
    public static final String ID = CreateFullID(Caster.class.getSimpleName());

    public Caster()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0,0, 1);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (HasActiveSynergy())
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

        if (HasActiveSynergy())
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
}