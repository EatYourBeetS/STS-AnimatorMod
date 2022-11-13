package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Soujiro_Kawara extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Soujiro_Kawara.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Normal);

    public Soujiro_Kawara()
    {
        super(DATA);

        Initialize(5, 2, 3);
        SetUpgrade(2, 0, 1);

        this.series = CardSeries.LogHorizon;
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
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
    }
}