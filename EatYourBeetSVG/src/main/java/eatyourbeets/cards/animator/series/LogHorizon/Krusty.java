package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Krusty extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Krusty.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Normal)
            .SetSeriesFromClassPackage();

    public Krusty()
    {
        super(DATA);

        Initialize(28, 0, 3, 2);
        SetUpgrade(1, 0, 1, 0);

        SetAutoplay(true);

        SetAffinity_Red(2, 0, 1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.RaiseFireLevel(secondaryValue);
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.ShakeScreen(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH);
        GameActions.Bottom.ModifyAllInstances(uuid, c ->
        {
            ((EYBCard) c).AddScaling(Affinity.Fire, magicNumber);
            c.flash();
        });
    }
}