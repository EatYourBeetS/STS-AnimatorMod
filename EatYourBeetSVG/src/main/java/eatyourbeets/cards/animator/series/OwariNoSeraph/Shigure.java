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
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Air), true));

    public Shigure()
    {
        super(DATA);

        Initialize(7, 0, 3);
        SetUpgrade(1, 0, 3);

        SetAffinity_Poison();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.DaggerSpray()).duration);
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);
    }
}