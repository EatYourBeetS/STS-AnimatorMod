package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class KoishiKomeiji extends PCLCard
{
    private final static RandomizedList<AbstractCard> touhouCards = new RandomizedList<>();
    public static final PCLCardData DATA = Register(KoishiKomeiji.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    public static final int CHOICES = 3;
    public static final int COST = 5;

    public KoishiKomeiji()
    {
        super(DATA);

        Initialize(0, 0, CHOICES, COST);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Star(1, 0 ,0);
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
        PCLActions.Bottom.StackPower(new KoishiPower(p, 1));
    }

    public static class KoishiPower extends PCLClickablePower
    {
        protected AbstractCard chosenCard;
        protected int turnsCounter = 0;
        protected int autoPlayCounter = 0;

        public KoishiPower(AbstractCreature owner, int amount)
        {
            super(owner, KoishiKomeiji.DATA, PowerTriggerConditionType.Affinity, COST);
            this.amount = amount;
            autoPlayCounter = amount; //Don't autoplay cards the turn you play this card

            this.triggerCondition.SetCheckCondition(__ -> turnsCounter >= CHOICES);
            updateDescription();
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            autoPlayCounter = 0;
            turnsCounter++;
        }

        @Override
        public void onCardDraw(AbstractCard card)
        {
            super.onCardDraw(card);

            if (autoPlayCounter < amount)
            {
                autoPlayCounter++;
                chosenCard = card;
                this.flash();
                PCLActions.Top.PlayCard(card, player.hand, null)
                .SpendEnergy(true)
                .AddCondition(AbstractCard::hasEnoughEnergy).AddCallback(() -> {
                    GameActions.Top.ApplyConstricted(TargetHelper.Player(), card.costForTurn);
                    GameActions.Top.GainEnergy(card.costForTurn);
                });
            }
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            if (touhouCards.Size() == 0) {
                for (AbstractCard c : CardLibrary.getAllCards()) {
                    if (c instanceof PCLCard && ((PCLCard) c).series != null && ((PCLCard) c).series.Equals(CardSeries.TouhouProject) &&
                            (c.rarity == AbstractCard.CardRarity.COMMON || c.rarity == AbstractCard.CardRarity.UNCOMMON || c.rarity == AbstractCard.CardRarity.RARE)) {
                        touhouCards.Add(c);
                    }
                }
            }

            final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final RandomizedList<AbstractCard> pool = new RandomizedList<AbstractCard>(touhouCards.GetInnerList());

            while (choice.size() < CHOICES && pool.Size() > 0)
            {
                AbstractCard ca = pool.Retrieve(rng).makeCopy();
                ca.upgrade();
                choice.addToTop(ca);
            }
            PCLActions.Bottom.SelectFromPile(null, 1, choice)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards != null && cards.size() > 0)
                        {
                            PCLActions.Bottom.MakeCardInHand(cards.get(0));
                        }
                    });

            RemovePower();
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, turnsCounter >= CHOICES ? PCLJUtils.Format(powerStrings.DESCRIPTIONS[1],COST,CHOICES) : "");
        }
    }
}

