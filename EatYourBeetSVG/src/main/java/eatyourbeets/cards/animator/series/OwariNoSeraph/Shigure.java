package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.stances.AgilityStance;
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

        Initialize(7, 0, 2, 2);
        SetUpgrade(2, 0, 1, 0);

        SetAffinity_Green(1, 1, 1);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.DaggerSpray()).duration);
        if (CheckPrimaryCondition(false)) {
            GameActions.Bottom.GainSupportDamage(magicNumber);
        }
        else {
            GameActions.Bottom.ApplyPoison(p, m, magicNumber);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (AgilityStance.IsActive())
        {
            GameActions.Bottom.Cycle(name, secondaryValue);
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return GetHandAffinity(Affinity.Light) > GetHandAffinity(Affinity.Green);
    }
}