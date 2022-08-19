package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KaijinPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(KaijinPower.class);

    public KaijinPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (isPlayer && !AbstractDungeon.player.hand.isEmpty())
        {
            GameActions.Bottom.SelectFromHand(name, 1, false)
            .SetOptions(true, true, true)
            .SetMessage(RetainCardsAction.TEXT[0])
            .SetFilter(c -> !c.isEthereal)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    AbstractCard card = cards.get(0);

                    if (card.baseBlock > 0)
                    {
                        card.baseBlock += amount;
                    }

                    if (card.baseDamage > 0)
                    {
                        card.baseDamage += amount;
                    }

                    GameUtilities.Retain(card);
                }
            });
        }
    }
}
