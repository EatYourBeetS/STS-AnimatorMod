package eatyourbeets.cards.unnamed.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;

public class SummonDoll extends UnnamedCard
{
    public static final EYBCardData DATA = Register(SummonDoll.class)
            .SetSkill(2, CardRarity.BASIC, EYBCardTarget.None);

    public SummonDoll()
    {
        super(DATA);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetSummon(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SummonDoll(1);
    }
}