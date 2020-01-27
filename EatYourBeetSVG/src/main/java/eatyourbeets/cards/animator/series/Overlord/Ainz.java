package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.AinzPower;
import eatyourbeets.utilities.GameActions;

public class Ainz extends AnimatorCard
{
    public static final String ID = Register(Ainz.class, EYBCardBadge.Drawn);
    public static final int BASE_COST = 7;

    public Ainz()
    {
        super(ID, BASE_COST, CardRarity.RARE, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, AinzPower.CHOICES);
        SetCostUpgrade(-1);

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
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new AinzPower(p, 1));
    }
}