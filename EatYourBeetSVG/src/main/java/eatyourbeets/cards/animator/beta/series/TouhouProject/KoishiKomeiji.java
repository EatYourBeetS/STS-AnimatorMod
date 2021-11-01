package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.powers.common.GenesisPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class KoishiKomeiji extends AnimatorCard
{
    private final static RandomizedList<AbstractCard> touhouCards = new RandomizedList<>();
    public static final EYBCardData DATA = Register(KoishiKomeiji.class)
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
        SetAffinity_Star(1,1,0);
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
        GameActions.Bottom.StackPower(new GenesisPower(p, 1));
        GameActions.Bottom.StackPower(new KoishiPower(p, 1));
    }

    public static class KoishiPower extends AnimatorClickablePower
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
                GameActions.Top.PlayCard(card, player.hand, null)
                .SpendEnergy(true)
                .AddCondition(AbstractCard::hasEnoughEnergy);
            }
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.ReducePower(player, GenesisPower.POWER_ID, 1);
            RemovePower();

            if (touhouCards.Size() == 0) {
                for (AbstractCard c : CardLibrary.getAllCards()) {
                    if (c instanceof AnimatorCard && ((AnimatorCard) c).series != null && ((AnimatorCard) c).series.Equals(CardSeries.TouhouProject) &&
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
            GameActions.Bottom.SelectFromPile(null, 1, choice)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards != null && cards.size() > 0)
                        {
                            GameActions.Bottom.MakeCardInHand(cards.get(0));
                        }
                    });
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, turnsCounter >= CHOICES ? JUtils.Format(powerStrings.DESCRIPTIONS[1],COST,CHOICES) : "");
        }
    }
}

