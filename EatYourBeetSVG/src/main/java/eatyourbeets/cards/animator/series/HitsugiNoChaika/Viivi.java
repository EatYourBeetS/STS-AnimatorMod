package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Viivi extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(Viivi.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged, EYBCardTarget.Random)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, false);
                }
            });

    public Viivi()
    {
        super(DATA);

        Initialize(3, 0, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(2, 0, 1);
        SetAffinity_Light(1, 0, 0);

        SetAffinityRequirement(Affinity.Green, 3);

        SetHitCount(3,1);
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }


    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.CreateThrowingKnives(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE).forEach(d -> d
                .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.DaggerSpray()).duration));

        if (IsStarter())
        {
            GameActions.Bottom.GainVelocity(1);
            GameActions.Bottom.Draw(1);
        }

        if (TrySpendAffinity(Affinity.Green)) {
            GameActions.Bottom.CreateThrowingKnives(1);
        }
    }
}