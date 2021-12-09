package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.EYBClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.animator.combat.EYBAffinityMeter;
import eatyourbeets.utilities.GameActions;

public class RerollAffinityPower extends EYBClickablePower
{
    public static final String POWER_ID = GR.Common.CreateID(RerollAffinityPower.class.getSimpleName());
    public boolean canChoose;

    public RerollAffinityPower(int amount)
    {
        super(null, POWER_ID, PowerTriggerConditionType.None, 0);

        this.triggerCondition.SetOneUsePerPower(true);
        this.hideAmount = true;
        this.img = GR.Common.Images.Affinities.General.Texture();

        Initialize(amount);
    }

    @Override
    public String GetUpdatedDescription()
    {
        return FormatDescription(0,
                CombatStats.Affinities.AffinityMeter.GetCurrentAffinity().GetAffinitySymbol(),
                CombatStats.Affinities.AffinityMeter.GetNextAffinity().GetAffinitySymbol());
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
    }

    @Override
    public void updateDescription()
    {
        tooltip.description = description = GetUpdatedDescription();

        int uses = triggerCondition.uses;
        if (uses >= 0 && GR.IsLoaded)
        {
            tooltip.subText.color = uses == 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR;
            tooltip.subText.text = uses + "/" + triggerCondition.baseUses + " " + GR.Animator.Strings.Combat.Rerolls;
        }
    }

    @Override
    public void OnClick() {
        OnClick(EYBAffinityMeter.Target.CurrentAffinity);
    }

    public void OnClick(EYBAffinityMeter.Target target)
    {
        if (triggerCondition.CanUse())
        {
            GameActions.Bottom.RerollAffinity(target).SetOptions(!canChoose, true);
            this.triggerCondition.uses -= 1;
            this.triggerCondition.Refresh(false);
            updateDescription();
        }
    }

    public RerollAffinityPower SetCanChoose(boolean canChoose) {
        this.canChoose = canChoose;
        return this;
    }

}