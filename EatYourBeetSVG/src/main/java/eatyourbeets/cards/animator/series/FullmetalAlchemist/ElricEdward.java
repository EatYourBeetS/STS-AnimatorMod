package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class ElricEdward extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ElricEdward.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public ElricEdward()
    {
        super(DATA);

        Initialize(4, 0, 1);
        SetUpgrade(4, 0, 0);

        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Orange(1, 1, 1);

        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Cycle(name, 1).AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                switch (cards.get(0).type)
                {
                    case ATTACK:
                        GameActions.Bottom.ChannelOrb(new Lightning());
                        break;

                    case SKILL:
                        GameActions.Bottom.ChannelOrb(new Frost());
                        break;

                    case POWER:
                        GameActions.Bottom.ChannelOrb(new Earth());
                        break;
                }
            }
        });
    }
}