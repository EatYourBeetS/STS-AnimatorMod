package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Defend_TouhouProject extends Defend
{
    public static final String ID = Register(Defend_TouhouProject.class).ID;

    public Defend_TouhouProject()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 1);
        SetUpgrade(0, 3);

        SetSeries(CardSeries.TouhouProject);
        SetAffinity_Blue(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Top.Scry(magicNumber);
        GameActions.Top.GainBlock(block);
    }
}