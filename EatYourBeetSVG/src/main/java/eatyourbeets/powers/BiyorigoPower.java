package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;

public class BiyorigoPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(BiyorigoPower.class.getSimpleName());

    private int dexterity;
    private int strength;

    public BiyorigoPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);
        this.strength = value;
        this.dexterity = 1;
        this.amount = 0;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + this.strength + powerStrings.DESCRIPTIONS[1] + this.dexterity + powerStrings.DESCRIPTIONS[2];
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
    public void stackPower(int stackAmount)
    {
        // done on onApplyPower
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);
        if (target == owner && power.ID.equals(this.ID))
        {
            BiyorigoPower p = Utilities.SafeCast(power, BiyorigoPower.class);
            if (p != null)
            {
                dexterity += p.dexterity;
                strength += p.strength;
                updateDescription();
            }
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            amount += 1;
            if (amount >= 4)
            {
                amount = 0;
                this.flash();

                AbstractPlayer p = AbstractDungeon.player;
                GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, strength), strength);
                GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, dexterity), dexterity);
            }
        }
    }
}
