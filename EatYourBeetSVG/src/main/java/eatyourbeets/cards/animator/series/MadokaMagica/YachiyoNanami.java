package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class YachiyoNanami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YachiyoNanami.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    public static final int DISCARD_AMOUNT = 1;
    public static final int GRIEF_REQUIREMENT = 4;

    private static final CardEffectChoice choices = new CardEffectChoice();

    public YachiyoNanami()
    {
        super(DATA);

        Initialize(0, 0, 3, GRIEF_REQUIREMENT);
        SetEthereal(true);

        SetAffinity_Water(2);
        SetAffinity_Light(1);
        SetAffinity_Earth(1);
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
        private int griefSeedsPlayed = 0;
        private int secondaryAmount;

        public YachiyoNanamiPower(AbstractPlayer owner, int amount, int secondaryAmount)
        {
            super(owner, YachiyoNanami.DATA, PowerTriggerConditionType.Discard, DISCARD_AMOUNT);
            this.amount = amount;
            this.secondaryAmount = secondaryAmount;
            this.triggerCondition.SetOneUsePerPower(true);

            updateDescription();
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, secondaryAmount, amount, griefSeedsPlayed, GRIEF_REQUIREMENT);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(griefSeedsPlayed, Color.WHITE, c.a);
        }

        @Override
        public void OnUse(AbstractMonster m)
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
                griefSeedsPlayed += amount;
            }
            if (griefSeedsPlayed >= GRIEF_REQUIREMENT) {
                CombatStats.Affinities.BonusAffinities.Add(CombatStats.Affinities.GetHandAffinityLevel(Affinity.Water, null) > CombatStats.Affinities.GetHandAffinityLevel(Affinity.Light, null) ? Affinity.Light : Affinity.Water, 1);
                griefSeedsPlayed -= GRIEF_REQUIREMENT;
            }

        }

        @Override
        public void OnPurge(AbstractCard card, CardGroup source) {
            invokeGrief(card);
        }
    }
}