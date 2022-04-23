package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Shigure extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shigure.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Green), true));

    public Shigure()
    {
        super(DATA);

        Initialize(6, 0, 3, 2);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Light(1);
        SetAffinity_Green(1, 1, 1);

        SetAffinityRequirement(Affinity.Green, 2);
        SetAffinityRequirement(Affinity.Light, 2);
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

        if (TryUseAffinity(Affinity.Green))
        {
            GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        }

        if (TryUseAffinity(Affinity.Light))
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, secondaryValue));
        }
    }
}