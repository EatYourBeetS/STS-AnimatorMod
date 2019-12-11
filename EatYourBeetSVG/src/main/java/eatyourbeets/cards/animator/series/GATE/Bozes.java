package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.BozesPower;
import eatyourbeets.utilities.GameActions;

public class Bozes extends AnimatorCard
{
    public static final String ID = Register(Bozes.class.getSimpleName(), EYBCardBadge.Special);

    public Bozes()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(7,0,2, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.Motivate(magicNumber);
        GameActions.Bottom.StackPower(new BozesPower(p, this.secondaryValue));
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