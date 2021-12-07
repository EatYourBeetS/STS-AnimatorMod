package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.LogHorizon.Soujiro;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Soujiro_Kawara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro_Kawara.class)
            .SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Normal)
            .SetSeries(Soujiro.DATA.Series);

    public Soujiro_Kawara()
    {
        super(DATA);

        Initialize(5, 2, 3);
        SetUpgrade(2, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        if (enemy != null && GameUtilities.IsAttacking(enemy.intent))
        {
            return super.ModifyBlock(enemy, amount + magicNumber);
        }

        return super.ModifyBlock(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
    }
}