package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Defend_Konosuba extends Defend
{
    public static final String ID = Register(Defend_Konosuba.class);

    public Defend_Konosuba()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 3, 2);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.GainTemporaryHP(magicNumber);
    }
}