package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MetalBat extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MetalBat.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public MetalBat()
    {
        super(DATA);

        Initialize(7, 0, 2, 3);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1, 1, 2);
        SetAffinity_Light(1);

        SetObtainableInCombat(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Delayed.Callback(() ->
        {
            GameActions.Bottom.GainForce(1);
            GameActions.Bottom.LoseHP(secondaryValue, AttackEffects.BLUNT_LIGHT).CanKill(false).IgnoreTempHP(true);
            GameActions.Bottom.Flash(this);
        });
    }

    @Override
    protected float GetInitialDamage()
    {
        int multiplier = (int)(10 * (1 - GameUtilities.GetHealthPercentage(player)));
        if (player.currentHealth == 1)
        {
            multiplier += 1;
        }

        return super.GetInitialDamage() + (magicNumber * multiplier);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
    }
}