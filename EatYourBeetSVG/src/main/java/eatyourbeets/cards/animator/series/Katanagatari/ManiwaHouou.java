package eatyourbeets.cards.animator.series.Katanagatari;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.common.PoisonPlayerPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class ManiwaHouou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ManiwaHouou.class)
            .SetPower(0, CardRarity.RARE)
            .SetSeries(CardSeries.Katanagatari);
    public static final int BASE_POISON = 1;

    public ManiwaHouou()
    {
        super(DATA);

        Initialize(0, 0, 3, BASE_POISON);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Dark(2);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new ManiwaHououPower(p, magicNumber, 1));
    }

    public static class ManiwaHououPower extends AnimatorClickablePower
    {
        private int poison;

        public ManiwaHououPower(AbstractCreature owner, int amount, int poison)
        {
            super(owner, ManiwaHouou.DATA, PowerTriggerConditionType.Energy, 1);

            this.triggerCondition.SetUses(-1, false, false);
            this.poison = ManiwaHouou.BASE_POISON;

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, amount, poison);
        }

        @Override
        protected void OnSamePowerApplied(AbstractPower power)
        {
            this.poison += ((ManiwaHououPower)power).poison;
        }

        @Override
        protected void onAmountChanged(int previousAmount, int difference)
        {
            GameActions.Bottom.GainStrength(difference).IgnoreArtifact(true);
            GameActions.Bottom.GainDexterity(difference).IgnoreArtifact(true);

            super.onAmountChanged(previousAmount, difference);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.StackPower(new PoisonPlayerPower(owner, owner, poison))
            .AddCallback(() -> this.poison += 1);
            this.flashWithoutSound();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.WaitRealtime(0.35f);
            RemovePower(GameActions.Last);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(poison, Settings.PURPLE_COLOR, c.a);
        }

        @Override
        public AbstractPower makeCopy()
        {
            return new ManiwaHououPower(owner, amount, poison);
        }
    }
}