package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ChaikaKamaz extends AnimatorCard {
    public static final EYBCardData DATA = Register(ChaikaKamaz.class).SetPower(2, CardRarity.RARE).SetSeriesFromClassPackage();

    public ChaikaKamaz() {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Poison();
        SetAffinity_Mind(2);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        for (int i=0; i<secondaryValue; i++) {
            GameActions.Bottom.MakeCardInDrawPile(ThrowingKnife.GetRandomCard()).AddCallback(GameUtilities::GiveHaste).SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new ChaikaKamazPower(p, this.magicNumber));
    }

    public static class ChaikaKamazPower extends AnimatorPower {
        public ChaikaKamazPower(AbstractPlayer owner, int amount) {
            super(owner, ChaikaKamaz.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            this.flash();

            GameActions.Bottom.DiscardFromHand(name, 1, false)
                    .SetOptions(false, false, false);

            GameActions.Bottom.SelectFromPile(name, amount, player.discardPile)
            .SetOptions(false, true)
            .AddCallback(cards -> {
                for (AbstractCard card : cards)
                {
                    if (card != null) {
                        GameActions.Top.MoveCard(card, player.discardPile, player.drawPile);
                    }
                }
            });
        }

        @Override
        public void updateDescription() {
            description = FormatDescription(0, amount);
        }
    }
}