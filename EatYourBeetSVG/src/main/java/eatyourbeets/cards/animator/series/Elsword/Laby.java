package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Laby extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Laby.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetMaxCopies(3)
            .SetSeriesFromClassPackage();

    public Laby()
    {
        super(DATA);

        Initialize(0, 0, 3, 35);

        SetAffinity_Light(2);
        SetAffinity_Dark(2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddEnchantedArmor(secondaryValue);
        }
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.StackPower(new EnchantedArmorPower(p, secondaryValue));
        GameActions.Bottom.StackPower(new LabyPower(p, 1, upgraded));
    }

    public static class LabyPower extends AnimatorPower
    {
        protected int upgradedAmount = 0;

        public LabyPower(AbstractCreature owner, int amount, boolean upgraded)
        {
            super(owner, Laby.DATA);

            if (upgraded)
            {
                this.upgradedAmount = amount;
            }

            Initialize(amount);
        }

        @Override
        public void OnSamePowerApplied(AbstractPower power)
        {
            this.upgradedAmount += ((LabyPower)power).upgradedAmount;
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(upgradedAmount > 0 ? 1 : 0, amount, upgradedAmount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            GameActions.Bottom.ApplyConstricted(owner, owner, amount);

            if (upgradedAmount > 0)
            {
                GameActions.Bottom.ApplyConstricted(TargetHelper.Enemies(), upgradedAmount)
                .ShowEffect(false, true);
            }

            this.flashWithoutSound();
        }
    }
}