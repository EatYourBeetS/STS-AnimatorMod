package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.animator.EarthenThornsPower;

public class Defend_Katanagatari extends Defend
{
    public static final String ID = Register(Defend_Katanagatari.class).ID;

    public Defend_Katanagatari()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 2);
        SetUpgrade(0, 3);

        SetSeries(CardSeries.Katanagatari);
        SetAffinity_Red(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new EarthenThornsPower(p, magicNumber));
    }
}