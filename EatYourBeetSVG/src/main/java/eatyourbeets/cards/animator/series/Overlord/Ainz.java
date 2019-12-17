package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.AinzPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Ainz extends AnimatorCard
{
    public static final String ID = Register(Ainz.class.getSimpleName(), EYBCardBadge.Drawn);
    public static final int BASE_COST = 7;

    public Ainz()
    {
        super(ID, BASE_COST, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, AinzPower.CHOICES);

        SetHealing(true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (this.cost > 0)
        {
            this.updateCost(-1);

            GameActions.Bottom.GainRandomStat(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.StackPower(new AinzPower(p, 1));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(6);
        }
    }
}