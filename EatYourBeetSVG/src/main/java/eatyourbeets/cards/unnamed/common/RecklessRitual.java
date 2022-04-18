package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.unnamed.SummoningSicknessPower;
import eatyourbeets.utilities.GameActions;

public class RecklessRitual extends UnnamedCard
{
    public static final EYBCardData DATA = Register(RecklessRitual.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public RecklessRitual()
    {
        super(DATA);

        Initialize(0, 0, 1, 5);
        SetUpgrade(0, 0, 0,-2);

        SetSummon(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SummonDoll(1);
        GameActions.Bottom.DrawReduction(magicNumber);
        GameActions.Bottom.StackPower(new SummoningSicknessPower(p, secondaryValue));
    }
}