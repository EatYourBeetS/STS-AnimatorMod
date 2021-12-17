package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;

public class Hitei extends PCLCard
{
    public static final PCLCardData DATA = Register(Hitei.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    public static final int POWER_ACTIVATION_COST = 5;
    public static final int EXHAUST_SELECTION_SIZE = 2;
    public static final int CARD_DRAW = 2;

    public Hitei()
    {
        super(DATA);

        Initialize(0, 0, EXHAUST_SELECTION_SIZE, POWER_ACTIVATION_COST);
        SetCostUpgrade(-1);

        SetAffinity_Orange(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainVitality(1);
        PCLActions.Bottom.StackPower(new HiteiPower(p, 1));
    }

    public static class HiteiPower extends PCLClickablePower
    {
        public HiteiPower(AbstractPlayer owner, int amount)
        {
            super(owner, Hitei.DATA, PowerTriggerConditionType.TakeDamage, POWER_ACTIVATION_COST);

            this.triggerCondition.SetOneUsePerPower(true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, CARD_DRAW, EXHAUST_SELECTION_SIZE, amount);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.Draw(CARD_DRAW);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            PCLActions.Bottom.Callback(() ->
            {
                if (player.drawPile.size() < EXHAUST_SELECTION_SIZE)
                {
                    PCLActions.Top.Callback(new EmptyDeckShuffleAction(), this::ExhaustCards);
                }
                else
                {
                    ExhaustCards();
                }
            });

            this.flash();
        }

        private void ExhaustCards()
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final RandomizedList<AbstractCard> pile = new RandomizedList<>(player.drawPile.group);
            while (group.size() < EXHAUST_SELECTION_SIZE && pile.Size() > 0)
            {
                group.addToTop(pile.Retrieve(rng));
            }

            if (group.size() >= EXHAUST_SELECTION_SIZE)
            {
                PCLActions.Top.ExhaustFromPile(name, 1, group)
                .SetOptions(false, true).AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        PCLActions.Bottom.GainTemporaryHP(1);
                        if (c instanceof PCLCard) {
                            PCLCombatStats.MatchingSystem.AddAffinities(((PCLCard) c).affinities);
                            PCLCombatStats.MatchingSystem.AddAffinities(((PCLCard) c).affinities);
                        }
                    }
                });
            }
        }
    }
}