package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Laby extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Laby.class).SetPower(2, CardRarity.UNCOMMON);

    public Laby()
    {
        super(DATA);

        Initialize(0, 0, 33, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, magicNumber));
        GameActions.Bottom.StackPower(new LabyPower(p, secondaryValue, upgraded));
    }

    public static class LabyPower extends AnimatorPower
    {
        protected int upgradedAmount = 0;

        public LabyPower(AbstractCreature owner, int amount, boolean upgraded)
        {
            super(owner, Laby.DATA);

            this.amount = amount;

            if (upgraded)
            {
                this.upgradedAmount = amount;
            }

            updateDescription();
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            if (ID.equals(power.ID) && target == owner)
            {
                this.upgradedAmount += power.amount;
            }

            super.onApplyPower(power, target, source);
        }

        @Override
        public void updateDescription()
        {
            if (upgradedAmount > 0)
            {
                this.description = FormatDescription(1, amount, upgradedAmount);
            }
            else
            {
                this.description = FormatDescription(0, amount);
            }
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            GameActions.Bottom.ApplyConstricted(owner, owner, amount);

            if (upgradedAmount > 0)
            {
                for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
                {
                    GameActions.Bottom.ApplyConstricted(owner, enemy, upgradedAmount);
                }
            }
        }
    }
}