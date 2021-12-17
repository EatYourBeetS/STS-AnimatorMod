package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.interfaces.subscribers.OnPurgeSubscriber;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;

public class YachiyoNanami extends PCLCard
{
    public static final PCLCardData DATA = Register(YachiyoNanami.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    public static final int DISCARD_AMOUNT = 6;

    private static final CardEffectChoice choices = new CardEffectChoice();

    public YachiyoNanami()
    {
        super(DATA);

        Initialize(0, 0, 1, 5);
        SetEthereal(true);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);
        SetAffinity_Orange(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new YachiyoNanamiPower(p, magicNumber, secondaryValue));
    }

    public static class YachiyoNanamiPower extends PCLClickablePower implements OnPurgeSubscriber
    {
        private int secondaryAmount;

        public YachiyoNanamiPower(AbstractPlayer owner, int amount, int secondaryAmount)
        {
            super(owner, YachiyoNanami.DATA, PowerTriggerConditionType.Affinity, DISCARD_AMOUNT);
            this.amount = amount;
            this.secondaryAmount = secondaryAmount;
            this.triggerCondition.SetOneUsePerPower(true);

            updateDescription();
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, DISCARD_AMOUNT, secondaryAmount, amount);
        }
        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.GainBlock(secondaryAmount);
            PCLActions.Bottom.CreateGriefSeeds(1);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card,m);
            invokeGrief(card);
        }

        @Override
        public void onExhaust(AbstractCard card)
        {
            super.onExhaust(card);
            invokeGrief(card);
        }

        private void invokeGrief(AbstractCard card) {
            if (card != null && card.type.equals(CardType.CURSE)) {
                PCLActions.Bottom.AddAffinity(PCLAffinity.Blue, amount);
                PCLActions.Bottom.AddAffinity(PCLAffinity.Light, amount);
            }
        }

        @Override
        public void OnPurge(AbstractCard card, CardGroup source) {
            invokeGrief(card);
        }
    }
}