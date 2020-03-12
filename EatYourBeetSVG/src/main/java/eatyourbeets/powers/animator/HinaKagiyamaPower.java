package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.special.HinaKagiyama;
import eatyourbeets.cards.animator.special.HinaKagiyama_Miracle;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HinaKagiyamaPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HinaKagiyamaPower.class.getSimpleName());
    public static final int CARD_DRAW_AMOUNT = 2;

    private int baseAmount;

    public HinaKagiyamaPower(AbstractCreature owner, int amount)
    {
        super(owner, HinaKagiyama.DATA);

        this.amount = amount;
        this.baseAmount = amount;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        this.baseAmount += stackAmount;
        updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        this.amount = baseAmount;
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        GameActions.Bottom.SelectFromPile(name, baseAmount, player.exhaustPile)
        .SetOptions(false, true)
        .SetMessage(FormatDescription(1, baseAmount))
        .SetFilter(GameUtilities::IsCurseOrStatus)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                if (player.exhaustPile.group.remove(card))
                {
                    GameActions.Bottom.MakeCardInHand(new HinaKagiyama_Miracle());
                }
            }
        });
    }

    @Override
    public void onCardDraw(AbstractCard c)
    {
        if (c.type == AbstractCard.CardType.CURSE && this.amount > 0)
        {
            GameActions.Bottom.Draw(CARD_DRAW_AMOUNT);
            this.amount--;
            this.flash();
            updateDescription();
        }
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, CARD_DRAW_AMOUNT);
        this.enabled = (amount > 0);
    }
}

