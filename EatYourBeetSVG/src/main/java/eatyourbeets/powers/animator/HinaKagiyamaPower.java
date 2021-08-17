package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.beta.special.Miracle;
import eatyourbeets.cards.animator.special.HinaKagiyama;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HinaKagiyamaPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HinaKagiyamaPower.class);
    public static final int CARD_DRAW_AMOUNT = 2;

    public HinaKagiyamaPower(AbstractCreature owner, int amount)
    {
        super(owner, HinaKagiyama.DATA);

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, CARD_DRAW_AMOUNT);

        SetEnabled(amount > 0);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        ResetAmount();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.SelectFromPile(name, baseAmount, player.exhaustPile)
        .SetOptions(false, true)
        .SetMessage(FormatDescription(1, baseAmount))
        .SetFilter(GameUtilities::IsHindrance)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                if (player.exhaustPile.group.remove(card))
                {
                    GameActions.Bottom.MakeCardInHand(new Miracle());
                }
            }
        });
    }

    @Override
    public void onCardDraw(AbstractCard c)
    {
        super.onCardDraw(c);

        if (c.type == AbstractCard.CardType.CURSE && this.amount > 0)
        {
            GameActions.Bottom.Draw(CARD_DRAW_AMOUNT);
            this.amount -= 1;
            updateDescription();
            this.flash();
        }
    }
}

