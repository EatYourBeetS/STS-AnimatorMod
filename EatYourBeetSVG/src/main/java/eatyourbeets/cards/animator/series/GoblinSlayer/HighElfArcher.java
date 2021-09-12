package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class HighElfArcher extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HighElfArcher.class)
            .SetAttack(0, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public HighElfArcher()
    {
        super(DATA);

        Initialize(2, 0, 2, 1);
        SetUpgrade(1, 0);

        SetAffinity_Green(1, 1, 1);

        SetAffinityRequirement(Affinity.Green, 3);
        SetAffinityRequirement(Affinity.Blue, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX(SFX.ANIMATOR_ARROW);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowDagger(c.hb, 0.15f).SetColor(Color.TAN)).duration * 0.5f);

        if (CheckAffinity(Affinity.Green))
        {
            GameActions.Bottom.ApplyPoison(player, m, magicNumber);
        }
        if (CheckAffinity(Affinity.Blue))
        {
            GameActions.Bottom.ApplyLockOn(player, m, secondaryValue);
        }

        if (info.IsStarter)
        {
            GameActions.Bottom.Draw(1);
        }
    }
}