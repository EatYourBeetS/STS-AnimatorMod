package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Lust extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lust.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    protected static boolean flipVfx;

    public Lust()
    {
        super(DATA);

        Initialize(2, 0, 2, 3);
        SetUpgrade(1, 0, 0, 1);

        SetAffinity_Star(2);
        SetAffinity_Green(0, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && m.hasPower(VulnerablePower.POWER_ID))
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainTemporaryHP(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
            .SetVFX(true, false)
            .SetDamageEffect(enemy -> GameEffects.List.Add(VFX.Claw(enemy.hb, Color.WHITE, Color.VIOLET)
                                      .FlipX(flipVfx ^= true).SetScale(0.7f)).duration);
        }

        if (m.hasPower(VulnerablePower.POWER_ID))
        {
            GameActions.Bottom.ApplyWeak(p, m, 1);
        }
    }
}