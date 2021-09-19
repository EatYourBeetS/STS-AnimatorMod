package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class EmpowerPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(EmpowerPower.class);

    public EmpowerPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.maxAmount = 12;

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, maxAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        GameActions.Last.Callback(() -> {
            Random rng = EYBCard.rng;
            if (rng == null)
            {
                JUtils.LogInfo(EmpowerPower.class, "EYBCard.rng was null");
                rng = new Random();
            }

            RandomizedList<AnimatorCard> cardsInHand = new RandomizedList<>();
            AnimatorCard cardToChange = null;

            for (AbstractCard card : player.hand.group)
            {
                if (card instanceof AnimatorCard)
                {
                    AnimatorCard animatorCard = (AnimatorCard) card;
                    if (animatorCard.type.equals(AbstractCard.CardType.ATTACK))
                    {
                        cardsInHand.Add(animatorCard);
                    }
                    else if (animatorCard.baseBlock > 0)
                    {
                        cardsInHand.Add(animatorCard);
                    }
                }
            }

            if (cardsInHand.Size() > 0)
            {
                cardToChange = cardsInHand.Retrieve(rng);
            }

            if (cardToChange != null) {
                ((EYBCard)cardToChange).SetScaling(Affinity.Light, amount);
            }
        });
    }
}
