package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.actions.special.SelectCreature;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public abstract class EYBClickablePower extends EYBPower
{
    public PowerTriggerCondition triggerCondition;
    public EYBCardTooltip tooltip;
    public boolean clickable;

    protected GameActionManager.Phase currentPhase;
    protected Hitbox hb;

    private EYBClickablePower(AbstractCreature owner, EYBCardData cardData, EYBRelic relic, String originalID)
    {
        super(owner, cardData, relic, originalID);

        priority = CombatStats.Instance.priority + 1;
        tooltip = new EYBCardTooltip(name, description);
        tooltip.subText = new ColoredString();
        tooltip.icon = powerIcon;

        final float size = ICON_SIZE * Settings.scale * 1.5f;
        hb = new Hitbox(size, size);
    }

    public EYBClickablePower(AbstractCreature owner, String id, PowerTriggerConditionType type, int requiredAmount)
    {
        this(owner, null, null, id);

        triggerCondition = new PowerTriggerCondition(this, type, requiredAmount);
    }

    public EYBClickablePower(AbstractCreature owner, String id, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(owner, null, null, id);

        triggerCondition = new PowerTriggerCondition(this, requiredAmount, checkCondition, payCost);
    }

    public EYBClickablePower(AbstractCreature owner, EYBRelic relic, PowerTriggerConditionType type, int requiredAmount)
    {
        this(owner, null, relic, null);

        triggerCondition = new PowerTriggerCondition(this, type, requiredAmount);
    }

    public EYBClickablePower(AbstractCreature owner, EYBRelic relic, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(owner, null, relic, null);

        triggerCondition = new PowerTriggerCondition(this, requiredAmount, checkCondition, payCost);
    }

    public EYBClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount)
    {
        this(owner, cardData, null, null);

        triggerCondition = new PowerTriggerCondition(this, type, requiredAmount);
    }

    public EYBClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(owner, cardData, null, null);

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
            tooltip.subText.text = uses + "/" + triggerCondition.baseUses + " uses";
        }
    }

    @Override
    protected void OnSamePowerApplied(AbstractPower power)
    {
        super.OnSamePowerApplied(power);

        if (triggerCondition.stackAutomatically)
        {
            triggerCondition.AddUses(((EYBClickablePower)power).triggerCondition.uses);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (triggerCondition.refreshEachTurn)
        {
            triggerCondition.Refresh(true);
        }
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        final GameActionManager.Phase phase = AbstractDungeon.actionManager.phase;
        if (currentPhase != phase || GR.UI.Elapsed50())
        {
            triggerCondition.Refresh(false);
            currentPhase = phase;
        }

        clickable = (currentPhase == GameActionManager.Phase.WAITING_ON_USER && GameUtilities.IsPlayerTurn(true)) && triggerCondition.CanUse();
        hb.update();

        if (hb.hovered)
        {
            if (triggerCondition.CanUse() && hb.justHovered)
            {
                SFX.Play(SFX.UI_HOVER);
            }

            EYBCardTooltip.QueueTooltip(tooltip, InputHelper.mX + hb.width, InputHelper.mY + (hb.height * 0.5f), false);

            if (enabled && clickable)
            {
                if (InputHelper.justClickedLeft)
                {
                    hb.clickStarted = true;
                    SFX.Play(SFX.UI_CLICK_1);
                }
                else if (hb.clicked)
                {
                    hb.clicked = false;
                    OnClick();
                }
            }
        }
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        if (hb.cX != x || hb.cY != y)
        {
            hb.move(x, y);
        }

        Color borderColor = (enabled && triggerCondition.CanUse()) ? c : disabledColor;
        Color imageColor = enabled ? c : disabledColor;
        RenderHelpers.DrawCentered(sb, borderColor, GR.Common.Images.SquaredButton_EmptyCenter.Texture(), x, y, ICON_SIZE, ICON_SIZE, 1.5f, 0);
        if (powerIcon != null)
        {
            RenderHelpers.DrawCentered(sb, imageColor, this.powerIcon, x, y, ICON_SIZE, ICON_SIZE, 0.75f, 0);
        }
        else
        {
            RenderHelpers.DrawCentered(sb, imageColor, this.img, x, y, ICON_SIZE, ICON_SIZE, 0.75f, 0);
        }

        if (enabled && hb.hovered && clickable)
        {
            RenderHelpers.DrawCentered(sb, Colors.White(0.3f), GR.Common.Images.SquaredButton.Texture(), x, y, ICON_SIZE, ICON_SIZE, 1.5f, 0);
        }

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x, y);
        }
    }

    public void OnClick()
    {
        final ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
        if (enemies.size() == 1)
        {
            this.triggerCondition.Use(enemies.get(0));
        }
        else if (triggerCondition.requiresTarget)
        {
            GameActions.Bottom.SelectCreature(SelectCreature.Targeting.Enemy, name)
            .AddCallback(c ->
            {
                if (c != null)
                {
                    this.triggerCondition.Use((AbstractMonster) c);
                }
            });
        }
        else
        {
            this.triggerCondition.Use(null);
        }
    }

    public void OnUse(AbstractMonster m)
    {

    }
}