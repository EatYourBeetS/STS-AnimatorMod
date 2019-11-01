package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper;

public class YukoShionjiPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(YukoShionjiPower.class.getSimpleName());

    private final AbstractPlayer p;

    private int leftClicks;
    private int rightClicks;
    private AbstractCard card;

    public YukoShionjiPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;
        leftClicks = amount;
        rightClicks = amount;

        this.p = (AbstractPlayer) owner;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0] + this.leftClicks + powerStrings.DESCRIPTIONS[1] + this.rightClicks + powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        leftClicks += stackAmount;
        rightClicks += stackAmount;
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        leftClicks = amount;
        rightClicks = amount;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        leftClicks = 0;
        rightClicks = 0;
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        if (p.drawPile.size() > 0)
        {
            AbstractCard temp = p.drawPile.getTopCard();
            if (temp != card)
            {
                card = temp;
                card.setAngle(0.0F, true);
                card.lighten(true);
                card.unfadeOut();
                card.untip();
                card.unhover();
            }

            card.current_x = 150.0F * Settings.scale;
            card.current_y = 450.0F * Settings.scale;
            card.hb.move(card.current_x, card.current_y);
            card.hb.update();

            if (card.hb.hovered)
            {
                card.drawScale = 0.70f;
                if (leftClicks > 0 && InputHelper.justReleasedClickLeft)
                {
                    GameActionsHelper.DrawCard(p,1);
                    leftClicks -= 1;
                    updateDescription();
                }
                else if (rightClicks > 0 && InputHelper.justReleasedClickRight)
                {
                    GameActionsHelper.AddToBottom(new DiscardSpecificCardAction(card, p.drawPile));
                    rightClicks -= 1;
                    updateDescription();
                }
            }
            else
            {
                card.drawScale = 0.4f;
            }

            card.hb.resize(AbstractCard.IMG_WIDTH * card.drawScale, AbstractCard.IMG_HEIGHT * card.drawScale);
        }
        else
        {
            card = null;
        }
    }

    private final static Color green = Color.GREEN.cpy();
    private final static Color red = Color.RED.cpy();

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        super.renderIcons(sb, x, y, c);

        if (card != null)
        {
            card.render(sb);

            float offset = card.hb.width / 2f;

            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, String.valueOf(leftClicks), card.hb.cX - offset, card.hb.y - 10, green);
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, String.valueOf(rightClicks), card.hb.cX + offset, card.hb.y - 10, red);
        }
    }
}
