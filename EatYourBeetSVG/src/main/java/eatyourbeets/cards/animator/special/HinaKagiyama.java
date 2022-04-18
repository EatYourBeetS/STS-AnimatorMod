package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnAfterCardDrawnSubscriber;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class HinaKagiyama extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HinaKagiyama.class)
            .SetPower(0, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject)
            .SetMaxCopies(1)
            .PostInitialize(data -> data.AddPreview(new Special_Miracle(), false));
    public static final int MAX_BLESSING_PER_TURN = 3;

    public HinaKagiyama()
    {
        super(DATA);

        Initialize(0, 0, MAX_BLESSING_PER_TURN);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HinaKagiyamaPower(p, 1));
    }

    public static class HinaKagiyamaPower extends AnimatorPower implements OnAfterCardDrawnSubscriber, OnCardCreatedSubscriber
    {
        public HinaKagiyamaPower(AbstractCreature owner, int amount)
        {
            super(owner, HinaKagiyama.DATA);

            this.canBeZero = true;

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
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
        }

        @Override
        public void onExhaust(AbstractCard card)
        {
            super.onExhaust(card);

            if (card.type == CardType.CURSE)
            {
                GameActions.Bottom.MakeCardInHand(new Special_Miracle());
                flash();
            }
        }

        @Override
        public void OnAfterCardDrawn(AbstractCard card)
        {
            Activate(card);
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle)
        {
            Activate(card);
        }

        public void Activate(AbstractCard c)
        {
            if (enabled && c.type == AbstractCard.CardType.CURSE)
            {
                GameActions.Bottom.GainBlessing(1);
                reducePower(1);
                SetEnabled(amount > 0);
                flashWithoutSound();
            }
        }
    }
}