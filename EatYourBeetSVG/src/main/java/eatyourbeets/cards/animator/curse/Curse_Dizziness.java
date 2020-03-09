package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCostIncrease;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Curse_Dizziness extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Dizziness.class).SetCurse(-2, EYBCardTarget.None);

    public Curse_Dizziness() {
        super(DATA);
        Initialize(0, 0, 0);
        SetSynergy(Synergies.TouhouProject);
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

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        GameActions.Bottom.MakeCardInDrawPile(new Dazed());
    }
}