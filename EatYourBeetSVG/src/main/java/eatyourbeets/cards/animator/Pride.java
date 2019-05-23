package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.MovePowerLeftAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PridePower;

public class Pride extends AnimatorCard
{
    public static final String ID = CreateFullID(Pride.class.getSimpleName());

    public Pride()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(0,0, 1);

        baseSecondaryValue = secondaryValue = 2;

        AddExtendedDescription();

        SetSynergy(Synergies.FullmetalAlchemist, true);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (HasActiveSynergy())
        {
            target = CardTarget.SELF_AND_ENEMY;
        }
        else
        {
            target = CardTarget.SELF;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActionsHelper.ChannelOrb(new Dark(), true);
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, m, new ConstrictedPower(m, p, this.secondaryValue), this.secondaryValue);
        }

        if (!p.hasPower(PridePower.POWER_ID))
        {
            GameActionsHelper.ApplyPower(p, p, new PridePower(p));
            GameActionsHelper.AddToBottom(new MovePowerLeftAction(p, PridePower.POWER_ID));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            //upgradeMagicNumber(1);
            upgradeSecondaryValue(2);
        }
    }
}