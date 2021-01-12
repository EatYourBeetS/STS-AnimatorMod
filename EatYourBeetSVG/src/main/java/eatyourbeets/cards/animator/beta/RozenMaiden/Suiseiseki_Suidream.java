/*

package eatyourbeets.cards.animator.beta.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

@Deprecated
class Suiseiseki_Suidream extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Suiseiseki_Suidream.class)
    		.SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None);

    public Suiseiseki_Suidream()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 0);

        SetPurge(true);

        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainAgility(magicNumber, upgraded);
    }
}


 */