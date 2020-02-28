package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.beta.MadokaMagica.YachiyoNanami;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class YachiyoNanamiPower extends AnimatorPower {
    public static final String POWER_ID = CreateFullID(YachiyoNanamiPower.class.getSimpleName());

    public YachiyoNanamiPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        GameActions.Bottom.DiscardFromHand(owner.name, 1, false)
                .SetOptions(false, true, true).AddCallback(cards -> {
                    if (cards.size() > 0)
                    {
                        if (cards.get(0).cost == 0)
                        {
                            GameActions.Bottom.GainAgility(2);
                        }

                        GameActions.Bottom.GainBlock(amount);
                    }
                });
    }
}
