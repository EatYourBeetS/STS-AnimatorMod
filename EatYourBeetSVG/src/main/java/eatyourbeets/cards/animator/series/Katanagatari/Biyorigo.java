package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class Biyorigo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Biyorigo.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    public static final int MAX_METALLICIZE = 4;

    public Biyorigo()
    {
        super(DATA);

        Initialize(0, 3, 1, 1);
        SetUpgrade(0, 2, 0, 1);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new BiyorigoPower(p, magicNumber, secondaryValue));
    }

    public static class BiyorigoPower extends AnimatorClickablePower
    {
        public BiyorigoPower(AbstractCreature owner, int amount, int uses)
        {
            super(owner, Biyorigo.DATA, PowerTriggerConditionType.Energy, 1);

            this.maxAmount = MAX_METALLICIZE;
            this.triggerCondition.SetUses(uses, false, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.uses, amount, MAX_METALLICIZE);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.GainArtifact(1);
        }

        @Override
        public AbstractPower makeCopy()
        {
            return new BiyorigoPower(owner, amount, triggerCondition.uses);
        }
    }
}