package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.*;

public class ChaikaTrabant extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ChaikaTrabant.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public ChaikaTrabant()
    {
        super(DATA);

        Initialize(11, 0, 2);
        SetUpgrade(2, 0, 1);

        SetAffinity_Blue(2, 0, 2);
        SetAffinity_Light(2);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Last.Callback(() ->
        {
            boolean flash = true;
            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                if (m.hasPower(LockOnPower.POWER_ID))
                {
                    if (flash)
                    {
                        GameActions.Bottom.Flash(this);
                        flash = false;
                    }

                    calculateCardDamage(m);
                    GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE)
                    .SetDamageEffect(c -> GameEffects.List.Add(VFX.SmallLaser(player.hb, c.hb, Color.WHITE)).duration * 0.3f);
                }
            }
        });
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ApplyLockOn(TargetHelper.RandomEnemy(), magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainIntellect(1);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.SmallLaser(player.hb, c.hb, Color.WHITE)).duration * 0.3f);
    }
}