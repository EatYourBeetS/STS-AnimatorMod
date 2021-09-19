package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.animator.beta.special.EarthCure;
import eatyourbeets.cards.animator.beta.special.EarthWall;
import eatyourbeets.cards.animator.beta.special.Exhume;
import eatyourbeets.cards.animator.beta.special.Stockpile;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class BalancePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(BalancePower.class);
    public RandomizedList<AnimatorCard> EarthCards = new RandomizedList<>();

    public BalancePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;

        Initialize(amount);

        EarthCards.Add(new EarthWall());
        EarthCards.Add(new EarthCure());
        EarthCards.Add(new Exhume());
        EarthCards.Add(new Stockpile());
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        Random rng = EYBCard.rng;
        if (rng == null)
        {
            JUtils.LogInfo(BalancePower.class, "EYBCard.rng was null");
            rng = new Random();
        }

        AnimatorCard newCard = EarthCards.Retrieve(rng);
        GameActions.Bottom.MakeCard(newCard.makeCopy(), player.hand);
    }
}
