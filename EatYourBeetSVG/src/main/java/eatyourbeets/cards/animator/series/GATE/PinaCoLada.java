package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.PinaCoLadaPower;

public class PinaCoLada extends AnimatorCard
{
    public static final String ID = Register(PinaCoLada.class);

    public PinaCoLada()
    {
        super(ID, 2, CardRarity.RARE, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 1);
        SetUpgrade(0, 8, 0);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (block > 0)
        {
            GameActions.Bottom.GainBlock(this.block);
        }

        GameActions.Bottom.StackPower(new PinaCoLadaPower(p, 1));
    }
}