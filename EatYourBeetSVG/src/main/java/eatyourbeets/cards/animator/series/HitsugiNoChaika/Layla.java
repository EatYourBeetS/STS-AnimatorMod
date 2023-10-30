package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Layla extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Layla.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    public static final int POISON_AMOUNT = 4;

    public Layla()
    {
        super(DATA);

        Initialize(7, 0, 2, POISON_AMOUNT);
        SetUpgrade(4, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(2, 0, 1);

        SetAffinityRequirement(Affinity.Blue, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.StackPower(new LaylaPower(m, p, magicNumber));

        if (CheckSpecialCondition(true))
        {
            for (AbstractMonster c : GameUtilities.GetEnemies(true))
            {
                if (c.hasPower(PoisonPower.POWER_ID))
                {
                    GameActions.Bottom.ApplyFreezing(p, c, 1);
                }
            }
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        boolean canApply = false;
        for (AbstractMonster c : GameUtilities.GetEnemies(true))
        {
            if (c.hasPower(PoisonPower.POWER_ID))
            {
                canApply = true;
            }
        }

        return canApply && super.CheckSpecialCondition(tryUse);
    }

    public static class LaylaPower extends AnimatorPower
    {
        public LaylaPower(AbstractCreature owner, AbstractCreature source, int amount)
        {
            super(owner, source, Layla.DATA);

            this.priority += 1;

            Initialize(amount, PowerType.DEBUFF, true);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, POISON_AMOUNT);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            GameActions.Bottom.ApplyPoison(source, owner, POISON_AMOUNT);
            ReducePower(1);
            flash();
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(POISON_AMOUNT, Colors.Green(c.a));
        }
    }
}