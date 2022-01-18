package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.interfaces.subscribers.OnPurgeSubscriber;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import static pinacolada.resources.GR.Enums.CardTags.AFTERLIFE;

public class Godan extends PCLCard
{
    public static final PCLCardData DATA = Register(Godan.class).SetPower(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage().SetMultiformData(2);
    private static final int POWER_ENERGY_COST = 7;

    public Godan()
    {
        super(DATA);

        Initialize(0, 2, 2, 3);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Orange(1);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form != 1);
        }
        return super.SetForm(form, timesUpgraded);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.StackPower(new GodanPower(p, magicNumber, secondaryValue));
    }

    public static class GodanPower extends PCLClickablePower implements OnPurgeSubscriber
    {
        public int secondaryValue;

        public GodanPower(AbstractCreature owner, int amount, int secondaryValue)
        {
            super(owner, Godan.DATA, PowerTriggerConditionType.Affinity, POWER_ENERGY_COST);
            this.amount = amount;
            this.secondaryValue = secondaryValue;
            this.triggerCondition.SetOneUsePerPower(true);
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onPurge.Subscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, secondaryValue);
        }


        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onPurge.Subscribe(this);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            final CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            RandomizedList<AbstractCard> possiblePicks = new RandomizedList<>(PCLJUtils.Filter(AbstractDungeon.commonCardPool.group, c -> c.hasTag(AFTERLIFE)));
            for (int i = 0; i < secondaryValue; i++)
            {
                final AbstractCard card = possiblePicks.Retrieve(rng);
                if (card != null)
                {
                    choices.group.add(card);
                }
            }

            PCLActions.Bottom.SelectFromPile(name, 1, choices)
                    .SetMessage(GR.PCL.Strings.HandSelection.Obtain)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            AbstractCard card = cards.get(0);
                            PCLActions.Bottom.MakeCardInDrawPile(card).AddCallback(c -> PCLActions.Bottom.Motivate(c, 1));
                        }
                    });
        }

        @Override
        public void OnPurge(AbstractCard card, CardGroup source) {
            PCLActions.Bottom.GainBlock(amount);
        }
    }
}