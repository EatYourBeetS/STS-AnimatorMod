package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.Synergies;

public class GoblinShaman extends AnimatorCard_Status
{
    public static final String ID = Register(GoblinShaman.class);

    public GoblinShaman()
    {
        super(ID, 1, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0, 0);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.StackPower(new FrailPower(p, 1, false));
        }
    }
}