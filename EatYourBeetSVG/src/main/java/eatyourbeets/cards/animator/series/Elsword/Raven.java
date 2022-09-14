package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Raven extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Raven.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();
    protected boolean canGainBlock = false;

    public Raven()
    {
        super(DATA);

        Initialize(3, 0, 2, 5);
        SetUpgrade(4, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Green, 1);
        SetAffinityRequirement(Affinity.Red, 1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.ApplyWeak(p, m, 1);
        }
        else
        {
            GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
        }

        if (TryUseAffinities(Affinity.Red, Affinity.Green))
        {
            GameActions.Bottom.GainBlock(secondaryValue);
        }
    }
}