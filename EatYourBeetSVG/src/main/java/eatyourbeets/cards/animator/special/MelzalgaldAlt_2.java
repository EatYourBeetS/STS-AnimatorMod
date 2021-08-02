package eatyourbeets.cards.animator.special;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class MelzalgaldAlt_2 extends MelzalgaldAlt
{
    public static final EYBCardData DATA = Register(MelzalgaldAlt_2.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetSeries(SERIES);

    public MelzalgaldAlt_2()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(0, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.GainIntellect(magicNumber);
    }
}