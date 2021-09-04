package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.IchigoKurosaki_Bankai;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class IchigoKurosaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IchigoKurosaki.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new IchigoKurosaki_Bankai(), false));

    public IchigoKurosaki()
    {
        super(DATA);

        Initialize(2, 0, 0, 5);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1, 1, 1);
        SetAffinity_Green(1, 1, 1);

        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HORIZONTAL);

        GameActions.Bottom.GainForce(1, true);
        GameActions.Bottom.GainAgility(1, true);

        GameActions.Bottom.Callback(() -> {
            if (CombatStats.Affinities.GetPowerAmount(Affinity.Red) >= secondaryValue)
            {
                GameActions.Bottom.Exhaust(this);
                GameActions.Bottom.MakeCardInDrawPile(new IchigoKurosaki_Bankai());
            }
        });

    }
}