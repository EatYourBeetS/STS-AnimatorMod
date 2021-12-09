package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Zombieman extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Zombieman.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Zombieman()
    {
        super(DATA);

        Initialize(7, 0, 2, 4);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Dark(1);

        SetObtainableInCombat(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int[] damage = DamageInfo.createDamageMatrix(secondaryValue, true, true);
        GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.LoseHP(secondaryValue, AttackEffects.BLUNT_LIGHT).CanKill(false).IgnoreTempHP(true);
        GameActions.Bottom.Flash(this);
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
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
    }
}