package eatyourbeets.cards.animator.series.Elsword;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ara.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Ara()
    {
        super(DATA);

        Initialize(3, 0, 1, 2);
        SetUpgrade(2, 0);

        SetAffinity_Green(1, 1, 1);
        SetAffinity_Red(1);

        SetAffinityRequirement(AffinityType.Red, 2);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(GameUtilities.GetDebuffsCount(m.powers));
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(false, false, false);

        if (CheckAffinity(AffinityType.Red))
        {
            GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
        }
    }
}