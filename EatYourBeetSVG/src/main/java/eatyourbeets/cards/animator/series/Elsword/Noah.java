package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
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
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Noah()
    {
        super(DATA);

        Initialize(17, 0, 1, 7);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Dark(2, 0, 1);

        SetAffinityRequirement(Affinity.Dark, 3);
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
        if (m != null)
        {
            GameUtilities.GetIntent(m).AddBlinded();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.PURPLE, Color.LIGHT_GRAY, Color.VIOLET, Color.BLUE).duration * 0.6f);
        GameActions.Bottom.GainCorruption(1, true);
        GameActions.Bottom.ApplyBlinded(p, m, magicNumber);

        if (!CheckAffinity(Affinity.Dark))
        {
            GameActions.Bottom.StackPower(new DelayedDamagePower(p, secondaryValue));
        }
    }
}