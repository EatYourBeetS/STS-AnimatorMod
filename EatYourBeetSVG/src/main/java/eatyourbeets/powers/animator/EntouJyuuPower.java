package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;

public class EntouJyuuPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EntouJyuuPower.class.getSimpleName());

    private static final int baseAmount = 2;

    private int damageBonus;

    public EntouJyuuPower(AbstractCreature owner, int damageBonus)
    {
        super(owner, POWER_ID);

        this.amount = baseAmount;
        this.damageBonus = damageBonus;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        this.description = desc[0] + amount + desc[1] + damageBonus + desc[2];
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

        this.amount = baseAmount;

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.damageBonus += stackAmount;
        this.fontScale = 8.0F;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        if (amount > 0)
        {
            if (card.type == AbstractCard.CardType.ATTACK)
            {
                GameActionsHelper.AddToBottom(new ModifyDamageAction(card.uuid, this.damageBonus));
                GameActionsHelper.DrawCard(AbstractDungeon.player, 1);

                this.flash();

                amount -= 1;
                updateDescription();
            }
        }

        super.onPlayCard(card, m);
    }
}