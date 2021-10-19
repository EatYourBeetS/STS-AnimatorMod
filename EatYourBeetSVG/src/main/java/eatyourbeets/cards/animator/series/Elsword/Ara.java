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

public class Ara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ara.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Ara()
    {
        super(DATA);

        Initialize(3, 0, 1, 2);
        SetUpgrade(1, 0);

        SetAffinity_Green(1, 1, 1);
        SetAffinity_Red(1);

        SetAffinityRequirement(Affinity.Orange, 4);
        SetHitCount(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).forEach(d -> d.SetSoundPitch(1.1f, 1.3f));

        if (TrySpendAffinity(Affinity.Orange))
        {
            GameActions.Bottom.GainAgility(magicNumber, upgraded);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(GameUtilities.GetDebuffsCount(m.powers));
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(false, false, false);
    }
}