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
import eatyourbeets.utilities.GameUtilities;

public class HighElfArcher extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HighElfArcher.class)
            .SetAttack(0, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public HighElfArcher()
    {
        super(DATA);

        Initialize(3, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Green(1, 1, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX(SFX.ANIMATOR_ARROW);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowDagger(c.hb, 0.15f).SetColor(Color.TAN)).duration * 0.5f);

        if (info.IsStarter)
        {
            GameActions.Bottom.Draw(1);
        }

        if (!GameUtilities.HasArtifact(m))
        {
            GameActions.Bottom.ApplyLockOn(player, m, secondaryValue);
        }
    }

    @Override
    public void triggerOnAffinitySeal(boolean manual)
    {
        super.triggerOnAffinitySeal(manual);
        GameActions.Bottom.Draw(1);
    }
}