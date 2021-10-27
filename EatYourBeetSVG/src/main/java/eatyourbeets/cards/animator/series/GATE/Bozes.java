package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.animator.BozesPower;
import eatyourbeets.utilities.GameActions;

public class Bozes extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Bozes.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Bozes()
    {
        super(DATA);

        Initialize(4, 0, 1, 4);
        SetUpgrade(0, 0, 1);

        SetAffinity_Fire(2);
        SetAffinity_Steel(1);

        SetAffinityRequirement(Affinity.Fire, 12);
        SetAffinityRequirement(Affinity.Steel, 12);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.StackPower(new BozesPower(p, this.secondaryValue));

        if (CheckAffinity(Affinity.Fire))
        {
            GameActions.Bottom.GainRage(secondaryValue);
        }

        if (CheckAffinity(Affinity.Steel))
        {
            GameActions.Bottom.GainRage(secondaryValue);
        }
    }
}