package eatyourbeets.powers.deprecated;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class EntouJyuuPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EntouJyuuPower.class);

    private int stacks;

    public EntouJyuuPower(AbstractCreature owner, int stacks)
    {
        super(owner, POWER_ID);

        this.amount = stacks;
        this.stacks = stacks;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        this.description = desc[0] + amount + desc[1];
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (amount >= 0)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        }
        else
        {
            super.renderAmount(sb, x, y, c);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();
        this.amount = stacks;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.stacks += stackAmount;
        super.stackPower(stackAmount);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        if (amount > 0)
        {
            if (usedCard.type == AbstractCard.CardType.ATTACK)
            {
                GameActions.Bottom.Draw(1);
                GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), 1);

                this.flash();

                amount -= 1;
                updateDescription();
            }
        }
    }
}