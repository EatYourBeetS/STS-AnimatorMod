package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Defend_Konosuba extends Defend
{
    public static final String ID = Register(Defend_Konosuba.class).ID;

    public Defend_Konosuba()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 3, 2);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Konosuba);
        SetAffinity_Light(1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainTemporaryHP(magicNumber);
    }
}