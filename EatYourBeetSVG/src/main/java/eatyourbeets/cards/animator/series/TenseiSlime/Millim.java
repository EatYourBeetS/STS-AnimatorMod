package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class Millim extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Millim.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public Millim()
    {
        super(DATA);

        Initialize(6, 0, 2);

        SetAffinity_Star(1, 0, 1);

        SetAffinityRequirement(Affinity.General, 4);
        SetUnique(true, true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 1)
        {
            upgradeMagicNumber(1);
            upgradeDamage(1);
        }
        else
        {
            upgradeMagicNumber(0);
            upgradeDamage(2);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainRandomAffinityPower(1, false);
        }

        if (CheckAffinity(Affinity.General))
        {
            switch (rng.random(2))
            {
                case 0: GameActions.Bottom.ChannelOrb(new Fire()); break;
                case 1: GameActions.Bottom.ChannelOrb(new Lightning()); break;
                case 2: GameActions.Bottom.ChannelOrb(new Dark()); break;
            }
        }
    }
}