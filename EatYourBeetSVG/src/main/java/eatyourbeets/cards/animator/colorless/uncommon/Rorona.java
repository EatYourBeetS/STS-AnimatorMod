package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.PermanentlyUpgrade;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnBattleEndSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Rorona extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rorona.class)
            .SetMaxCopies(1)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Atelier);
    public static final int POTION_GOLD_COST = 30;
    public static final int MAX_POTIONS_PER_COMBAT = 2;

    public Rorona()
    {
        super(DATA);

        Initialize(0, 0, 40, POTION_GOLD_COST);
        SetUpgrade(0, 0, 20);

        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1, 0, 0);

        SetObtainableInCombat(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new RoronaPower(p, this, this.magicNumber));
    }

    public static class RoronaPower extends AnimatorClickablePower implements OnBattleEndSubscriber
    {
        private final AbstractCard card;

        public RoronaPower(AbstractPlayer owner, Rorona card, int amount)
        {
            super(owner, Rorona.DATA, PowerTriggerConditionType.Gold, Rorona.POTION_GOLD_COST);

            this.card = card;
            this.maxAmount = 100;
            this.triggerCondition.SetUses(1, false, false);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onBattleEnd.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onBattleEnd.Unsubscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, amount);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.Add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(false)));
        }

        @Override
        public void OnBattleEnd()
        {
            if ((card.misc + amount) >= rng.random(100))
            {
                GameActions.Instant.Add(new PermanentlyUpgrade(player.masterDeck, 1).SetFilter(c -> !c.cardID.equals(Rorona.DATA.ID)));
                card.misc = 0;
                flash();
            }
            else
            {
                card.misc += 8;
            }

            final AbstractCard permanentCard = GameUtilities.GetMasterDeckInstance(card.uuid);
            if (permanentCard != null)
            {
                permanentCard.misc = card.misc;
            }
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            if (this.triggerCondition.uses == 0)
            {
                int uses = CombatStats.GetCombatData(ID, 1);
                if (uses < Rorona.MAX_POTIONS_PER_COMBAT)
                {
                    CombatStats.SetCombatData(ID, uses + 1);
                    this.triggerCondition.AddUses(1);
                }
            }
        }
    }
}