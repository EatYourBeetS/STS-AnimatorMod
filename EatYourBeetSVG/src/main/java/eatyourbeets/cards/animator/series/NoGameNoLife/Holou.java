package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BlueCandle;
import eatyourbeets.cards.animator.curse.Curse_Doubt;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.ArrayList;

public class Holou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Holou.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Holou()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 0, 0);

        SetEthereal(true);
        SetAffinity_Blue(2);
    }

    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HolouPower(p, magicNumber));
    }

    public static class HolouPower extends AnimatorPower implements OnShuffleSubscriber, OnCardCreatedSubscriber
    {
        public HolouPower(AbstractCreature owner, int amount)
        {
            super(owner, Holou.DATA);

            this.amount = amount;

            Initialize(amount);
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            final ArrayList<AbstractCard> cards = new ArrayList<>(player.drawPile.group);
            cards.addAll(player.hand.group);
            cards.addAll(player.discardPile.group);
            cards.addAll(player.exhaustPile.group);

            for (AbstractCard c : cards)
            {
                OnCardCreated(c, true);
            }
            CombatStats.onCardCreated.Subscribe(this);
            CombatStats.onShuffle.Subscribe(this);
            CombatStats.SetCombatData(BlueCandle.ID, true);
        }

        @Override
        public void onRemove()
        {
            CombatStats.onCardCreated.Unsubscribe(this);
            CombatStats.onShuffle.Unsubscribe(this);
            CombatStats.SetCombatData(BlueCandle.ID, false);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card,m);
            if (GameUtilities.IsHindrance(card)) {
                GameActions.Bottom.LoseHP(1, AbstractGameAction.AttackEffect.NONE);
                GameActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), amount);
                GameActions.Last.Exhaust(card);
                flash();
            }
        }


        @Override
        public void OnShuffle(boolean triggerRelics)
        {
            GameActions.Last.MakeCardInDrawPile(new Curse_Doubt());
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle) {
            if (GameUtilities.IsHindrance(card) && card instanceof EYBCard) {
                ((EYBCard) card).SetUnplayable(false);
            }
        }
    }
}