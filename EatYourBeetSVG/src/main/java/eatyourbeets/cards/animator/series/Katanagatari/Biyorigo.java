package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class Biyorigo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Biyorigo.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public Biyorigo()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 2, 0);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainThorns(magicNumber);
        GameActions.Bottom.GainArtifact(secondaryValue);
        GameActions.Bottom.StackPower(new BiyorigoPower(p, 1));
    }

    public static class BiyorigoPower extends AnimatorClickablePower
    {
        public static final int POWER_GAIN = 2;

        public BiyorigoPower(AbstractCreature owner, int amount)
        {
            super(owner, Biyorigo.DATA, PowerTriggerConditionType.Energy, 1);

            this.amount = amount;
            this.triggerCondition.SetOneUsePerPower(true);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.uses, POWER_GAIN);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.GainForce(POWER_GAIN);
            GameActions.Bottom.GainAgility(POWER_GAIN);
        }
    }
}