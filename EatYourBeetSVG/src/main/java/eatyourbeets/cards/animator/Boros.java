package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.BorosPower;
import eatyourbeets.utilities.GameActionsHelper2;

public class Boros extends AnimatorCard
{
    public static final String ID = Register(Boros.class.getSimpleName(), EYBCardBadge.Drawn);

    public Boros()
    {
        super(ID, 4, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 2, 1);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActionsHelper2.GainForce(secondaryValue);
        GameActionsHelper2.GainTemporaryHP(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper2.ApplyPower(p, p, new BorosPower(p));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(3);
        }
    }
}