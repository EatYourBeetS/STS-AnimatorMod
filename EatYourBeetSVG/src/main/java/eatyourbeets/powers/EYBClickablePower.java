package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.RenderHelpers;

public abstract class EYBClickablePower extends EYBPower
{
    public PowerTriggerCondition triggerCondition;
    public EYBCardTooltip tooltip;
    public boolean hovered;

    private AbstractGameAction currentAction;
    private float cX;
    private float cY;

    private EYBClickablePower(AbstractCreature owner, EYBCardData cardData)
    {
        super(owner, cardData);

        priority = CombatStats.Instance.priority + 1;
        tooltip = new EYBCardTooltip(name, description);
        tooltip.subText = new ColoredString();
        tooltip.icon = powerIcon;
    }

    public EYBClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount)
    {
        this(owner, cardData);

        triggerCondition = new PowerTriggerCondition(this, type, requiredAmount);
    }

    public EYBClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(owner, cardData);

        triggerCondition = new PowerTriggerCondition(this, requiredAmount, checkCondition, payCost);
    }

    public String GetUpdatedDescription()
    {
        return FormatDescription(0, amount);
    }

    @Override
    public final void updateDescription()
    {
        tooltip.description = description = GetUpdatedDescription();

        int uses = triggerCondition.uses;
        if (uses >= 0)
        {
            tooltip.subText.color = uses == 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR;
            tooltip.subText.text = uses == 1 ? "1 use remaining" : (uses + " uses remaining");
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        if (triggerCondition.usePowerAmount)
        {
            triggerCondition.AddUses(stackAmount);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (triggerCondition.refreshEachTurn)
        {
            triggerCondition.uses = triggerCondition.baseUses;
        }
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
        if (currentAction != action)
        {
            triggerCondition.Refresh();
            currentAction = action;
        }

        final float size = ICON_SIZE * Settings.scale;
        final float x = cX - (size * 0.5f);
        final float y = cY - (size * 0.5f);
        hovered = InputHelper.mX >= x && InputHelper.mX <= (x + size) && InputHelper.mY >= y && InputHelper.mY <= (y + size);
        if (hovered)
        {
            EYBCardTooltip.QueueTooltip(tooltip, InputHelper.mX + size, InputHelper.mY + (size * 0.5f));

            if (InputHelper.justClickedLeft && currentAction == null && triggerCondition.CanUse())
            {
                triggerCondition.Use();
            }
        }
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        this.cX = x;
        this.cY = y;

        Color borderColor = (enabled && triggerCondition.CanUse()) ? c : disabledColor;
        Color imageColor = enabled ? c : disabledColor;
        RenderHelpers.DrawCentered(sb, borderColor, GR.Common.Images.SquaredButton.Texture(), x, y, ICON_SIZE, ICON_SIZE, 1.5f, 0);
        RenderHelpers.DrawCentered(sb, imageColor, this.powerIcon, x, y, ICON_SIZE, ICON_SIZE, 0.75f, 0);
        if (enabled && hovered && triggerCondition.CanUse())
        {
            RenderHelpers.DrawCentered(sb, RenderHelpers.WhiteColor(0.3f), GR.Common.Images.SquaredButton.Texture(), x, y, ICON_SIZE, ICON_SIZE, 1.5f, 0);
        }

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x, y);
        }
    }
}