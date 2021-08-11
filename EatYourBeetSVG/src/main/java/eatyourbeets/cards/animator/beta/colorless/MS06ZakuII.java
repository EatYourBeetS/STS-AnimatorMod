package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class MS06ZakuII extends AnimatorCard //TODO
{
    public static final EYBCardData DATA = Register(MS06ZakuII.class).SetPower(3, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Gundam);

    public MS06ZakuII()
    {
        super(DATA);

        Initialize(0, 0, 2, 39);
        SetCostUpgrade(-1);
        SetRetain(true);

        SetAffinity_Light(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new MS06ZakuIIPower(p, this.magicNumber));
    }

    public static class MS06ZakuIIPower extends AnimatorPower
    {
        public MS06ZakuIIPower(AbstractPlayer owner, int amount)
        {
            super(owner, MS06ZakuII.DATA);

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