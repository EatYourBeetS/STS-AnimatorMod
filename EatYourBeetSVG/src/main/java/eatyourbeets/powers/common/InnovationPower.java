package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class InnovationPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(InnovationPower.class);
    protected static final int BASE_PERCENT = 150;

    public InnovationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount * BASE_PERCENT);
        if (amount > 0)
        {
            this.type = PowerType.BUFF;
        }
        else {
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
    }

    @Override
    public void onRemove()
    {
        this.amount = 0;
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Last.Callback( () -> {
            if (player.hand.size() <= 0)
            {
                return;
            }

            RandomizedList<AnimatorCard> cardsInHand = new RandomizedList<>();

            for (AbstractCard card : player.hand.group)
            {
                if (card instanceof AnimatorCard && CardHasAttackOrBlock(card))
                {
                    cardsInHand.Add((AnimatorCard) card);
                }
            }

            if (cardsInHand.Size() <= 0)
            {
                return;
            }

            AnimatorCard card = cardsInHand.Retrieve(rng);

            float multiplier = ((amount * BASE_PERCENT) / 100f);

            if (card.baseDamage > 0) {
                int damageToIncrease = (int) Math.floor(card.baseDamage * multiplier);
                DamageModifiers.For(card).Add(damageToIncrease);
            }
            if (card.baseBlock > 0) {
                int blockToIncrease = (int) Math.floor(card.baseBlock * multiplier);
                BlockModifiers.For(card).Add(blockToIncrease);
            }

            CostModifiers.For(card).Add(1);
        });
    }

    private boolean CardHasAttackOrBlock(AbstractCard card)
    {
        return card.baseDamage > 0 || card.baseBlock > 0;
    }
}
