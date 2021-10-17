package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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

    public Raven()
    {
        super(DATA);

        Initialize(8, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Earth();
        SetAffinity_Poison();
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
            GameActions.Bottom.ApplyWeak(p, m, magicNumber);
        }
        else
        {
            GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
        }
    }
}