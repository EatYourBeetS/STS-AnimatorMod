package eatyourbeets.powers.UnnamedReign;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;

import java.nio.Buffer;

public class UltimateCrystalPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UltimateCrystalPower.class.getSimpleName());

    private static final int STRENGTH_GAIN = 2;
    private static final int PLATED_ARMOR = 8;

    public UltimateCrystalPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        this.amount = 100;

        updateDescription();
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
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + STRENGTH_GAIN + desc[1] + amount + desc[2] + PLATED_ARMOR + desc[3];
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.amount = 100;
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (damageAmount > 0 && !this.owner.hasPower(BufferPower.POWER_ID))
        {
            ReduceAmount(damageAmount);
        }

        return damageAmount;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, STRENGTH_GAIN), STRENGTH_GAIN);
    }

    private void ReduceAmount(int damage)
    {
        if (this.amount > 0)
        {
            this.flash();
            this.amount -= damage;
            if (this.amount <= 0)
            {
                AbstractPlayer p = AbstractDungeon.player;
                GameActionsHelper.ApplyPower(owner, p, new PlatedArmorPower(p, PLATED_ARMOR), PLATED_ARMOR);
                this.amount = 0;
            }
        }

        this.updateDescription();
    }
}
