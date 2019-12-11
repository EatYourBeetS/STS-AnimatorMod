package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCostIncrease;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Curse_Greed extends AnimatorCard_Curse
{
    public static final String ID = Register(Curse_Greed.class.getSimpleName());

    public Curse_Greed()
    {
        super(ID, -2, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Add(new RandomCostIncrease(1, false));
        this.flash();
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {

    }
}