package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.subscribers.OnBattleEndSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.WeightedList;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;

public class Rorona extends PCLCard
{
    public static final PCLCardData DATA = Register(Rorona.class)
            .SetPower(2, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Atelier);
    public static final int MAX_POTIONS_PER_COMBAT = 2;
    public static final int UPGRADE_CARD_REQUIREMENT = 25;

    public Rorona()
    {
        super(DATA);

        Initialize(0, 0, 32, UPGRADE_CARD_REQUIREMENT);
        SetUpgrade(0, 0, -6);

        SetAffinity_Orange(1);
        SetAffinity_Light(1);

        SetObtainableInCombat(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new RoronaPower(p, this, 1));
    }

    public static class RoronaPower extends PCLClickablePower implements OnBattleEndSubscriber
    {
        private final WeightedList<AbstractCard> toUpgrade = new WeightedList<>();
        private final Rorona rorona;

        public RoronaPower(AbstractCreature owner, Rorona rorona, int amount)
        {
            super(owner, rorona.cardData, PowerTriggerConditionType.Gold, rorona.magicNumber);

            this.rorona = rorona;
            this.maxAmount = 2;
            this.triggerCondition.SetUses(1, false, false);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onBattleEnd.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onBattleEnd.Unsubscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, Rorona.UPGRADE_CARD_REQUIREMENT, amount);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);

            PCLActions.Bottom.Add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(false)));
        }

        @Override
        public void OnBattleEnd()
        {
            toUpgrade.Clear();
            if (player.masterDeck.size() < Rorona.UPGRADE_CARD_REQUIREMENT)
            {
                return;
            }

            PCLActions.Instant.UpgradeFromPile(player.masterDeck, amount, true)
            .SetFilter(c -> !c.cardID.equals(rorona.cardID))
            .SetSelection(CardSelection.Special(null, (list, index, remove) ->
            {
                if (toUpgrade.Size() == 0)
                {
                    for (AbstractCard c : list)
                    {
                        switch (c.rarity)
                        {
                            case BASIC:
                                toUpgrade.Add(c, 60);
                                break;
                            case COMMON:
                                toUpgrade.Add(c, 50);
                                break;
                            case UNCOMMON:
                                toUpgrade.Add(c, 25);
                                break;
                            case RARE:
                                toUpgrade.Add(c, 10);
                                break;
                            default:
                                toUpgrade.Add(c, 1);
                                break;
                        }
                    }
                }

                return toUpgrade.Retrieve(rng);
            }));

            flash();
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            if (this.triggerCondition.uses == 0)
            {
                final int uses = CombatStats.GetCombatData(ID, 1);
                if (uses < Rorona.MAX_POTIONS_PER_COMBAT)
                {
                    CombatStats.SetCombatData(ID, uses + 1);
                    this.triggerCondition.AddUses(1);
                }
            }
        }

        @Override
        public AbstractPower makeCopy()
        {
            return new RoronaPower(owner, rorona, amount);
        }
    }
}