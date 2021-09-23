package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.interfaces.subscribers.OnAfterCardDrawnSubscriber;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HiiragiKureto extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(HiiragiKureto.class).SetPower(1, CardRarity.RARE).SetSeries(CardSeries.OwariNoSeraph);

    public HiiragiKureto() {
        super(DATA);
        this.Initialize(0, 0, 3, 3);
        this.SetAffinity_Red(1, 1, 0);
        this.SetAffinity_Dark(1, 1, 0);
    }

    protected void OnUpgrade() {
        this.SetRetainOnce(true);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new HiiragiKuretoPower(p, 1));
    }

    public static class HiiragiKuretoPower extends AnimatorPower implements OnAfterCardDrawnSubscriber, OnCardCreatedSubscriber {
        public HiiragiKuretoPower(AbstractCreature owner, int amount) {
            super(owner, HiiragiKureto.DATA);
            this.canBeZero = true;
            this.Initialize(amount);
        }

        @Override
        public void onInitialApplication() {
            super.onInitialApplication();
            CombatStats.onAfterCardDrawn.Subscribe(this);
            CombatStats.onCardCreated.Subscribe(this);
        }

        @Override
        public void onRemove() {
            super.onRemove();
            CombatStats.onAfterCardDrawn.Unsubscribe(this);
            CombatStats.onCardCreated.Unsubscribe(this);
        }

        @Override
        public void updateDescription() {
            this.description = this.FormatDescription(0, 3, this.baseAmount * 3);
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle) {
            if (player.hand.contains(card)) {
                this.OnAfterCardDrawn(card);
            }

        }

        @Override
        public void OnAfterCardDrawn(AbstractCard card) {
            if (this.enabled && GameUtilities.IsHindrance(card)) {
                GameActions.Bottom.Callback(card, (c, __) -> {
                    if (this.enabled) {
                        this.Activate(c);
                        this.flash();
                    }

                });
            }

        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            super.atEndOfTurn(isPlayer);
            this.amount = this.baseAmount;
            this.enabled = this.amount > 0;
            if (GameUtilities.GetPowerAmount(Affinity.Red) < 3) {
                GameActions.Delayed.TakeDamage(this.owner, this.baseAmount * 3, AttackEffects.LIGHTNING);
                this.flash();
            }

        }

        protected void Activate(AbstractCard card) {
            GameActions.Top.MoveCard(card, player.hand, player.discardPile).AddCallback((c) -> {
                if (c != null) {
                    GameActions.Bottom.Draw(1);
                    GameActions.Bottom.ChannelOrb(new Lightning());
                    --this.amount;
                    this.enabled = this.amount > 0;
                }

            });
        }
    }
}