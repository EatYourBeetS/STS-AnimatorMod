package eatyourbeets.cards.animator.special;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Melzalgald_3 extends MelzalgaldAlt
{
    public static final EYBCardData DATA = Register(Melzalgald_3.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetSeries(SERIES);

    public Melzalgald_3()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(0, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.GainAgility(magicNumber);
    }
}