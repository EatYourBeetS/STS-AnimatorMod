package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnAfterCardDrawnSubscriber;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HiiragiKureto extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiiragiKureto.class)
            .SetPower(1, CardRarity.RARE)
            .SetSeries(CardSeries.OwariNoSeraph);
    public static final int DAMAGE_AMOUNT = 3;

    public HiiragiKureto()
    {
        super(DATA);

        Initialize(0, 0, DAMAGE_AMOUNT);
        SetCostUpgrade(-1);

        SetAffinity_Red(2);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HiiragiKuretoPower(p, 1));
    }

    public static class HiiragiKuretoPower extends AnimatorPower implements OnAfterCardDrawnSubscriber, OnCardCreatedSubscriber
    {
        public HiiragiKuretoPower(AbstractCreature owner, int amount)
        {
            super(owner, HiiragiKureto.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onAfterCardDrawn.Subscribe(this);
            CombatStats.onCardCreated.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onAfterCardDrawn.Unsubscribe(this);
            CombatStats.onCardCreated.Unsubscribe(this);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount * DAMAGE_AMOUNT, amount);
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle)
        {
            Activate(card);
        }

        @Override
        public void OnAfterCardDrawn(AbstractCard card)
        {
            Activate(card);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            SetEnabled(true);
        }

        public void Activate(AbstractCard card)
        {
            if (enabled && player.hand.contains(card) && GameUtilities.IsHindrance(card))
            {
                GameActions.Bottom.Draw(1);
                GameActions.Bottom.ChannelOrbs(Lightning::new, amount);
                GameActions.Bottom.TakeDamageAtEndOfTurn(DAMAGE_AMOUNT * amount);
                flashWithoutSound();
                SetEnabled(false);
            }
        }
    }
}