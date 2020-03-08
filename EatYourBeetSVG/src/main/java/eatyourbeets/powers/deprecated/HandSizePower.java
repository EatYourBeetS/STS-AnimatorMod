package eatyourbeets.powers.deprecated;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.powers.AnimatorPower;

public class HandSizePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HandSizePower.class.getSimpleName());

    private final int BASE_HAND_SIZE;

    public HandSizePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        BASE_HAND_SIZE = BaseMod.MAX_HAND_SIZE;

        this.amount = amount;
        ModifyHandSize();
        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + BaseMod.MAX_HAND_SIZE + powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(BaseMod.MAX_HAND_SIZE), x, y, this.fontScale, c);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        RemovePower();
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        onRemove();
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        BaseMod.MAX_HAND_SIZE = BASE_HAND_SIZE;
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        ModifyHandSize();
    }

    private void ModifyHandSize()
    {
        BaseMod.MAX_HAND_SIZE = Math.max(0, BASE_HAND_SIZE + amount);

        if (amount >= BASE_HAND_SIZE)
        {
            this.type = PowerType.BUFF;
        }
        else
        {
            this.type = PowerType.DEBUFF;
        }
    }
}