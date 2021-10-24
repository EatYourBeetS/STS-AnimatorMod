package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class AcuraTooru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AcuraTooru.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public AcuraTooru()
    {
        super(DATA);

        Initialize(3, 0, 4, 6);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Air(2);
        SetAffinity_Mind(1);

        SetProtagonist(true);

        SetAffinityRequirement(Affinity.Mind, 5);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.RaiseAirLevel(secondaryValue);
        GameActions.Bottom.CreateThrowingKnives(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i=0; i<magicNumber; i++) {
            AbstractGameAction.AttackEffect effect = AttackEffects.SLASH_DIAGONAL;

            if (i % 2 == 0)
            {
                effect = AttackEffects.SLASH_VERTICAL;
            }

            GameActions.Bottom.DealDamage(this, m, effect);
        }

        if (CheckAffinity(Affinity.Mind))
        {
            GameActions.Bottom.GainBlur(1);
        }
    }
}