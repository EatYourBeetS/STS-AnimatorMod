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
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();

    public Ara()
    {
        super(DATA);

        Initialize(3, 0, 1, 2);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Green(1, 1, 1);
        SetAffinity_Red(1);

        SetAffinityRequirement(Affinity.Orange, 3);
        SetHitCount(2);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
            SetAffinityRequirement(Affinity.Green, 3);
            SetAffinityRequirement(Affinity.Orange, 0);
        }
        else {
            this.cardText.OverrideDescription(null, true);
            SetAffinityRequirement(Affinity.Green, 0);
            SetAffinityRequirement(Affinity.Orange, 3);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SPEAR).forEach(d -> d.SetSoundPitch(1.1f, 1.3f));

        if (auxiliaryData.form == 1 && TrySpendAffinity(Affinity.Green))
        {
            GameActions.Bottom.GainEndurance(magicNumber);
        }
        else if (auxiliaryData.form == 0 && TrySpendAffinity(Affinity.Orange))
        {
            GameActions.Bottom.GainVelocity(magicNumber);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (info.TryActivateSemiLimited()) {
            GameActions.Bottom.Draw(GameUtilities.GetDebuffsCount(m.powers));
            GameActions.Bottom.DiscardFromHand(name, 1, false)
                    .SetOptions(false, false, false);
        }
    }
}