package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.series.DateALive.ShidoItsuka;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class MioTakamiya extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(MioTakamiya.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.DateALive)
            .PostInitialize(data -> data.AddPreview(new ShidoItsuka(), false));

    public MioTakamiya()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetAffinity_Light(2, 0, 0);
        SetEthereal(true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new MioTakamiyaPower(p, magicNumber));
    }

    public static class MioTakamiyaPower extends AnimatorPower
    {
        public MioTakamiyaPower(AbstractPlayer owner, int amount)
        {
            super(owner, MioTakamiya.DATA);

            this.amount = amount;

            Initialize(amount);
        }

        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            GameActions.Bottom.GainFocus(2);
            GameActions.Bottom.GainStrength(-1);
            GameActions.Bottom.GainDexterity(-1);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (this.amount > 0) {
                final AnimatorCard card = JUtils.SafeCast(usedCard, AnimatorCard.class);
                if (card != null && card.series != null)
                {
                    for (AbstractCard c : player.drawPile.group) {
                        IncreaseSameSeriescard(card, c);
                    }
                    for (AbstractCard c : player.discardPile.group) {
                        IncreaseSameSeriescard(card, c);
                    }
                    for (AbstractCard c : player.hand.group) {
                        IncreaseSameSeriescard(card, c);
                    }
                    updateDescription();
                    flash();
                }
            }
        }

        private void IncreaseSameSeriescard(AnimatorCard playedCard, AbstractCard incoming) {
            if (GameUtilities.IsSameSeries(playedCard, incoming)) {
                if (incoming.baseBlock > 0)
                {
                    GameUtilities.IncreaseBlock(incoming, amount, false);
                }
                if (incoming.baseDamage > 0)
                {
                    GameUtilities.IncreaseDamage(incoming, amount, false);
                }
            }
        }
    }

}