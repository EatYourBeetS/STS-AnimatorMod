package pinacolada.cards.pcl.ultrarare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;

public class ShikizakiKiki extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(ShikizakiKiki.class)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Katanagatari);
    public static final int COST = 25;
    public static final int GAIN = 20;

    public ShikizakiKiki()
    {
        super(DATA);

        Initialize(0, 0, COST, GAIN);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new ShikizakiKikiPower(p, 1));
    }

    public static class ShikizakiKikiPower extends PCLClickablePower implements OnAfterCardDiscardedSubscriber
    {
        private int stacks;

        public ShikizakiKikiPower(AbstractCreature owner, int amount)
        {
            super(owner, ShikizakiKiki.DATA, PowerTriggerConditionType.Special, COST);
            this.triggerCondition.SetCheckCondition(__ -> stacks >= COST);
            this.triggerCondition.SetPayCost(__ -> stacks -= COST);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, COST, GAIN);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard card)
        {
            super.onAfterCardPlayed(card);

            if (card.type == CardType.ATTACK)
            {
                PCLActions.Bottom.Cycle(name, amount);
                this.flash();
            }
        }

        @Override
        public void onExhaust(AbstractCard card) {
            super.onExhaust(card);
            stacks += 1;
        }

        @Override
        public void OnAfterCardDiscarded() {
            stacks += 1;
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryStrength, GAIN);
            PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryDexterity, GAIN);

            PCLActions.Bottom.SFX(SFX.ATTACK_FIRE, 0.5f, 0.6f).SetDuration(0.25f, true);
            PCLActions.Bottom.SFX(SFX.ATTACK_AXE, 0.5f, 0.6f).SetDuration(0.25f, true);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(stacks, Color.WHITE, c.a);
        }
    }
}