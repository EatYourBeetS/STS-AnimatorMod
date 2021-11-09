package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class YachiyoNanami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YachiyoNanami.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    public static final int DISCARD_AMOUNT = 8;

    private static final CardEffectChoice choices = new CardEffectChoice();

    public YachiyoNanami()
    {
        super(DATA);

        Initialize(0, 0, 4, DISCARD_AMOUNT);
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
        GameActions.Bottom.StackPower(new YachiyoNanamiPower(p, 1, magicNumber));
    }

    public static class YachiyoNanamiPower extends AnimatorClickablePower implements OnPurgeSubscriber
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
            return FormatDescription(0, DISCARD_AMOUNT, secondaryAmount);
        }
        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.GainBlock(secondaryAmount);
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
                GameActions.Bottom.AddAffinity(CombatStats.Affinities.GetAffinityLevel(Affinity.Blue,true) > CombatStats.Affinities.GetAffinityLevel(Affinity.Light,true) ? Affinity.Light : Affinity.Blue, 1);
            }
        }

        @Override
        public void OnPurge(AbstractCard card, CardGroup source) {
            invokeGrief(card);
        }
    }
}