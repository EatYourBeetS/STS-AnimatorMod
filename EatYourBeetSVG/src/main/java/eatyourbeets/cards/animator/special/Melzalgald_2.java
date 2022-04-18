package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Melzalgald_2 extends MelzalgaldAlt
{
    public static final EYBCardData DATA = Register(Melzalgald_2.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetSeries(SERIES);

    public Melzalgald_2()
    {
        super(DATA);

        SetAffinity_Blue(0, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainEnergyNextTurn(1);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        if (CheckAffinity(Affinity.General))
        {
            GameActions.Bottom.GainIntellect(magicNumber);
        }
    }
}