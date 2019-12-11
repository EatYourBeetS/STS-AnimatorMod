package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.animator.ultrarare.ShikizakiKiki;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class ShikizakiKikiPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ShikizakiKikiPower.class.getSimpleName());

    private static final RandomizedList<AbstractCard> attacks = new RandomizedList<>();

    private int upgradeStack;
    private int unupgradedStacks;

    public ShikizakiKikiPower(AbstractCreature owner, boolean upgraded)
    {
        super(owner, POWER_ID);

        if (upgraded)
        {
            this.upgradeStack += 1;
        }
        else
        {
            this.unupgradedStacks = 1;
        }

        this.amount = 1;

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        ShikizakiKikiPower other = JavaUtilities.SafeCast(power, ShikizakiKikiPower.class);
        if (other != null && power.owner == target)
        {
            this.unupgradedStacks += other.unupgradedStacks;
            this.upgradeStack += other.upgradeStack;
        }

        super.onApplyPower(power, target, source);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        if ((amount + stackAmount) > 9)
        {
            stackAmount = 9 - amount;
        }

        if (stackAmount > 0)
        {
            super.stackPower(stackAmount);
        }
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        for (int i = 0; i < unupgradedStacks; i++)
        {
            ExecuteAction(ShikizakiKiki.BASE_POWER_AMOUNT);
        }

        for (int i = 0; i < upgradeStack; i++)
        {
            ExecuteAction(ShikizakiKiki.UPGRADED_POWER_AMOUNT);
        }

        this.flash();
    }

    private void ExecuteAction(int amount)
    {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        AddRandomAttacks(amount, group.group);

        GameActions.Bottom.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .SetMessage(CardRewardScreen.TEXT[1])
        .AddCallback(cards ->
        {
            AbstractCard c = cards.get(0);
            if (c.cost > 0)
            {
                GameActions.Bottom.GainForce(c.cost);
            }

            GameActions.Bottom.MakeCardInHand(c);
        });
    }

    private static void AddRandomAttacks(int amount, ArrayList<AbstractCard> group)
    {
        if (attacks.Count() == 0)
        {
            for (AbstractCard card : CardLibrary.getAllCards())
            {
                if (card.type == AbstractCard.CardType.ATTACK && !card.tags.contains(AbstractCard.CardTags.HEALING))
                {
                    attacks.Add(card);
                }
            }
        }

        ArrayList<AbstractCard> temp = new ArrayList<>();
        for (int i = 0; i < amount; i++)
        {
            temp.add(attacks.Retrieve(AbstractDungeon.cardRandomRng, true));
        }

        for (AbstractCard c : temp)
        {
            group.add(c.makeCopy());
            attacks.Add(c);
        }
    }
}