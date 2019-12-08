package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCostReduction;
import eatyourbeets.cards.AnimatorCard_Curse;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameActionsHelper2;

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

        GameActionsHelper2.AddToBottom(new RandomCostReduction(1, false));
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