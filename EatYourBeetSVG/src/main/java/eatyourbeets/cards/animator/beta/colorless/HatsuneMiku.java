package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class HatsuneMiku extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HatsuneMiku.class).SetPower(1, CardRarity.RARE).SetMaxCopies(1).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Vocaloid);

    public HatsuneMiku()
    {
        super(DATA);

        Initialize(0, 0, 2, 39);
        SetCostUpgrade(-1);
        SetRetain(true);
        SetHarmonic(true);

        SetAffinity_Light(2);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return (player.discardPile.size() + player.exhaustPile.size()) >= secondaryValue;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new HatsuneMikuPower(p, this.magicNumber));
    }

    public static class HatsuneMikuPower extends AnimatorPower
    {
        public HatsuneMikuPower(AbstractPlayer owner, int amount)
        {
            super(owner, HatsuneMiku.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            GameActions.Bottom.Draw(amount)
                    .AddCallback(null, (enemy, cards) -> {
                        for (AbstractCard card : cards)
                        {
                            if (card != null)
                            {
                                card.setCostForTurn(0);
                            }
                        }
                    });
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}