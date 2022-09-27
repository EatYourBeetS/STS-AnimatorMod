package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Noah extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Noah.class)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Noah()
    {
        super(DATA);

        Initialize(14, 0, 1, 7);
        SetUpgrade(2, 0, 0);

        SetAffinity_Green(1);
        SetAffinity_Dark(2, 0, 2);

        SetAffinityRequirement(Affinity.Dark, 2);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        SetHaste(true);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddFreezing();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.PURPLE, Color.LIGHT_GRAY, Color.VIOLET, Color.BLUE).duration * 0.6f);
        GameActions.Bottom.ApplyFreezing(p, m, magicNumber);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.GainCorruption(1, true);
        }
        else
        {
            GameActions.Bottom.StackPower(new DelayedDamagePower(p, secondaryValue));
        }
    }
}