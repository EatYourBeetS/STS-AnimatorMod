package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.EYBCard;
import eatyourbeets.cards.animator.ShikizakiKiki;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.Resources_Animator_Strings;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;
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

        GameActionsHelper.ChooseFromPile(1, false, group, this::OnSelection, 1,
                CardRewardScreen.TEXT[1] + " (" + name + ")", true);
    }

    public void OnSelection(Object state, ArrayList<AbstractCard> cards)
    {
        if (state.equals(1) && cards != null && cards.size() > 0)
        {
            AbstractCard c = cards.get(0);
            GameActionsHelper.GainForce(c.cost);
            GameActionsHelper.MakeCardInHand(c, 1, false);
        }
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
