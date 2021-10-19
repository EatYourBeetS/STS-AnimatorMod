package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class AcuraTooru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AcuraTooru.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, false);
                }
            });

    public AcuraTooru()
    {
        super(DATA);

        Initialize(3, 0, 2, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Green(1, 0, 2);
        SetAffinity_Red(1);

        SetProtagonist(true);
        SetHarmonic(true);

        SetAffinityRequirement(Affinity.Green, 3);
        SetHitCount(2);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainBlock(magicNumber);
        GameActions.Bottom.GainAgility(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.CreateThrowingKnives(secondaryValue);

        if (TrySpendAffinity(Affinity.Green))
        {
            GameActions.Bottom.GainBlock(magicNumber);
            GameActions.Bottom.GainAgility(1);
        }
    }
}