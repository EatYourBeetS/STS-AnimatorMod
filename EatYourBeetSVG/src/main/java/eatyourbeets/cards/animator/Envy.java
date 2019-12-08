package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EnvyPower;

public class Envy extends AnimatorCard
{
    public static final String ID = Register(Envy.class.getSimpleName(), EYBCardBadge.Special);

    public Envy()
    {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        SetEthereal(true);
        SetSynergy(Synergies.FullmetalAlchemist, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new EnvyPower(p, 1), 1);

        int tempHP = Math.floorDiv(p.maxHealth - p.currentHealth, 5);
        if (tempHP > 0)
        {
            GameActionsHelper.GainTemporaryHP(p, tempHP);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            SetEthereal(false);
        }
    }
}