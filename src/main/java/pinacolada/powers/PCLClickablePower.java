package pinacolada.powers;

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
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import pinacolada.actions.special.SelectCreature;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.effects.SFX;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public abstract class PCLClickablePower extends PCLPower
{
    public PCLPowerTriggerCondition triggerCondition;
    public PCLCardTooltip tooltip;
    public boolean clickable;

    private GameActionManager.Phase currentPhase;
    protected Hitbox hb;

    private PCLClickablePower(AbstractCreature owner, PCLCardData cardData, PCLRelic relic)
    {
        this(owner, cardData, relic, null);
    }

    private PCLClickablePower(AbstractCreature owner, PCLCardData cardData, PCLRelic relic, String originalID)
    {
        super(owner, cardData, relic, originalID);

        priority = PCLCombatStats.PRIORITY + 1;
        tooltip = new PCLCardTooltip(name, description);
        tooltip.subText = new ColoredString();
        tooltip.icon = powerIcon;

        final float size = ICON_SIZE * Settings.scale * 1.5f;
        hb = new Hitbox(size, size);
    }

    public PCLClickablePower(AbstractCreature owner, PCLRelic relic, PCLPowerTriggerCondition condition)
    {
        this(owner, null, relic);

        triggerCondition = condition;
    }

    public PCLClickablePower(AbstractCreature owner, PCLRelic relic, PowerTriggerConditionType type, int requiredAmount)
    {
        this(owner, null, relic);

        triggerCondition = new PCLPowerTriggerCondition(this, type, requiredAmount);
    }

    public PCLClickablePower(AbstractCreature owner, PCLRelic relic, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(owner, null, relic);

        triggerCondition = new PCLPowerTriggerCondition(this, requiredAmount, checkCondition, payCost);
    }

    public PCLClickablePower(AbstractCreature owner, PCLRelic relic, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost, PCLAffinity... affinities)
    {
        this(owner, null, relic);

        triggerCondition = new PCLPowerTriggerCondition(this, type, requiredAmount, checkCondition, payCost, affinities);
    }

    public PCLClickablePower(AbstractCreature owner, PCLCardData cardData, PowerTriggerConditionType type, int requiredAmount)
    {
        this(owner, cardData, null);

        triggerCondition = new PCLPowerTriggerCondition(this, type, requiredAmount);
    }

    public PCLClickablePower(AbstractCreature owner, PCLCardData cardData, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(owner, cardData, null);

        triggerCondition = new PCLPowerTriggerCondition(this, type, requiredAmount, checkCondition, payCost);
    }

    public PCLClickablePower(AbstractCreature owner, PCLCardData cardData, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost, PCLAffinity... affinities)
    {
        this(owner, cardData, null);

        triggerCondition = new PCLPowerTriggerCondition(this, type, requiredAmount, checkCondition, payCost, affinities);
    }

    public PCLClickablePower(AbstractCreature owner, String originalID, PowerTriggerConditionType type, int requiredAmount)
    {
        this(owner, null, null, originalID);

        triggerCondition = new PCLPowerTriggerCondition(this, type, requiredAmount);
    }

    public PCLClickablePower(AbstractCreature owner, String originalID, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(owner, null, null, originalID);

        triggerCondition = new PCLPowerTriggerCondition(this, type, requiredAmount, checkCondition, payCost);
    }

    public PCLClickablePower(AbstractCreature owner, String originalID, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost, PCLAffinity... affinities)
    {
        this(owner, null, null, originalID);

        triggerCondition = new PCLPowerTriggerCondition(this, type, requiredAmount, checkCondition, payCost, affinities);
    }

    public String GetUpdatedDescription()
    {
        return FormatDescription(0, amount);
    }

    @Override
    public void updateDescription()
    {
        tooltip.description = description = GetUpdatedDescription();

        int uses = triggerCondition.uses;
        if (uses >= 0 && GR.IsLoaded)
        {
            tooltip.subText.color = uses == 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR;
            tooltip.subText.text = uses + "/" + triggerCondition.baseUses + " " + GR.PCL.Strings.Combat.Uses;
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (triggerCondition.stackAutomatically && power.ID.equals(ID) && target == owner)
        {
            triggerCondition.AddUses(1);
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

        clickable = (currentPhase == GameActionManager.Phase.WAITING_ON_USER && PCLGameUtilities.IsPlayerTurn()) && triggerCondition.CanUse();
        hb.update();

        if (hb.hovered)
        {
            if (triggerCondition.CanUse() && hb.justHovered)
            {
                SFX.Play(SFX.UI_HOVER);
            }

            PCLCardTooltip.QueueTooltip(tooltip, InputHelper.mX + hb.width, InputHelper.mY + (hb.height * 0.5f));

            if (clickable)
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
        Color borderColor = (enabled && triggerCondition.CanUse()) ? c : disabledColor;
        Color imageColor = enabled ? c : disabledColor;

        renderIconsImpl(sb, x, y, borderColor, imageColor);
    }

    protected void renderIconsImpl(SpriteBatch sb, float x, float y, Color borderColor, Color imageColor) {
        if (hb.cX != x || hb.cY != y)
        {
            hb.move(x, y);
        }

        pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, borderColor, GR.PCL.Images.SquaredButton_EmptyCenter.Texture(), x, y, ICON_SIZE, ICON_SIZE, 1.5f, 0);
        if (this.powerIcon != null) {
            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, imageColor, this.powerIcon, x, y, ICON_SIZE, ICON_SIZE, 0.75f, 0);
        }
        else {
            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, imageColor, this.img, x, y, ICON_SIZE, ICON_SIZE, 0.75f, 0);
        }

        if (enabled && hb.hovered && clickable)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, Colors.White(0.3f), GR.PCL.Images.SquaredButton.Texture(), x, y, ICON_SIZE, ICON_SIZE, 1.5f, 0);
        }

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x, y);
        }
    }

    public void OnClick()
    {
        final ArrayList<AbstractMonster> enemies = PCLGameUtilities.GetEnemies(true);
        if (enemies.size() == 1)
        {
            this.triggerCondition.Use(enemies.get(0));
        }
        else if (triggerCondition.requiresTarget)
        {
            PCLActions.Bottom.SelectCreature(SelectCreature.Targeting.Enemy, name)
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

    public void OnUse(AbstractMonster m, int cost)
    {

    }
}