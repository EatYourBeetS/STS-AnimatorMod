package pinacolada.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.resources.GR;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;

public class RerollAffinityPower extends PCLClickablePower
{
    public static final String POWER_ID = GR.PCL.CreateID(RerollAffinityPower.class.getSimpleName());
    public boolean canChoose;

    public RerollAffinityPower(int amount)
    {
        super(null, POWER_ID, PowerTriggerConditionType.None, 0);

        this.triggerCondition.SetOneUsePerPower(true);
        this.hideAmount = true;
        this.img = GR.PCL.Images.Affinities.General.Texture();

        Initialize(amount);
    }

    @Override
    public String GetUpdatedDescription()
    {
        return FormatDescription(0,
                PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentAffinity().GetAffinitySymbol(),
                PCLCombatStats.MatchingSystem.AffinityMeter.GetNextAffinity().GetAffinitySymbol(),
                PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentMatchCombo());
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
            tooltip.subText.text = uses + "/" + triggerCondition.baseUses + " " + GR.PCL.Strings.Combat.Rerolls;
        }
    }

    @Override
    public void OnClick() {
        OnClick(PCLAffinityMeter.Target.CurrentAffinity);
    }

    public void OnClick(PCLAffinityMeter.Target target)
    {
        if (triggerCondition.CanUse())
        {
            PCLActions.Bottom.RerollAffinity(target).SetOptions(!canChoose, true);
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