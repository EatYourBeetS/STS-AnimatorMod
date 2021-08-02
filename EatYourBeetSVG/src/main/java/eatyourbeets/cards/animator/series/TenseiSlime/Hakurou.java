package eatyourbeets.cards.animator.series.TenseiSlime;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class Hakurou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hakurou.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Hakurou()
    {
        super(DATA);

        Initialize(1, 0, 3, 4);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (ForceStance.IsActive())
        {
            GameActions.Bottom.GainBlock(secondaryValue)
            .SetVFX(true, true);
        }

        GameActions.Bottom.VFX(new DieDieDieEffect());

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        }

        if (isSynergizing)
        {
            GameActions.Bottom.GainAgility(1);
        }
    }
}