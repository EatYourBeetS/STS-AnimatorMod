package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Curse_Dizziness extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Dizziness.class)
            .SetCurse(-2, EYBCardTarget.None, true)
            .SetRarity(CardRarity.SPECIAL)
            .SetSeries(CardSeries.TouhouProject)
            .PostInitialize(data -> data.AddPreview(new FakeAbstractCard(new Dazed()), false));

    public Curse_Dizziness()
    {
        super(DATA, false);

        SetAffinity_Dark(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.MakeCardInDrawPile(new Dazed());
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}