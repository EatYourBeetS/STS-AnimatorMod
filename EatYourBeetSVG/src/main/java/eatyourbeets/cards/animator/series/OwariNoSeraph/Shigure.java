package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Shigure extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shigure.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Green), true));

    public Shigure()
    {
        super(DATA);

        Initialize(7, 0, 5);
        SetUpgrade(3, 0, 0);

        SetAffinity_Light(1);
        SetAffinity_Green(2, 0, 1);

        SetAffinityRequirement(Affinity.Green, 2);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.ObtainAffinityToken(Affinity.Green, upgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.DaggerSpray()).duration);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        }
    }
}