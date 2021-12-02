package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ItamiYouji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ItamiYouji.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public ItamiYouji()
    {
        super(DATA);

        Initialize(2, 0, 3, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Silver(1);

        SetAffinityRequirement(Affinity.Orange, 6);

        SetExhaust(true);
        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        final int cardsPlayed = GameUtilities.GetTotalCardsPlayed(this, true);
        GameUtilities.ModifyHitCount(this, baseHitCount + Math.min(10, cardsPlayed), true);
    }

    @Override
    public int GetXValue() {
        return GameUtilities.GetTotalCardsPlayed(this, true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber).AddCallback(() -> {
            GameActions.Top.Reload(name, m, (enemy, cards) ->
            {
                for (int i = 0; i < cards.size(); i++) {
                    if (enemy != null && !GameUtilities.IsDeadOrEscaped(enemy))
                    {
                        GameActions.Bottom.DealDamage(this, enemy, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(1.3f, 1.5f));
                    }
                }
            });
        });

        if (info.IsSynergizing || TrySpendAffinity(Affinity.Orange))
        {
            GameActions.Bottom.GainSupportDamage(GetXValue());
        }
    }
}