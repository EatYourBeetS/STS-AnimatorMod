package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BiyorigoPower;

public class Biyorigo extends AnimatorCard
{
    public static final String ID = CreateFullID(Biyorigo.class.getSimpleName());

    public Biyorigo()
    {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0,2);

        this.baseSecondaryValue = this.secondaryValue = 0;

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (upgraded)
        {
            GameActionsHelper.ApplyPower(p, p, new ThornsPower(p, this.secondaryValue), this.secondaryValue);
        }

        GameActionsHelper.ApplyPower(p, p, new BiyorigoPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(3);
        }
    }
}