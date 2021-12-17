package pinacolada.orbs;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.EYBOrb;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLImages;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public abstract class PCLOrb extends EYBOrb implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLImages.Orbs IMAGES = GR.PCL.Images.Orbs;
    public static final int IMAGE_SIZE = 96;
    public PCLCardTooltip tooltip;

    public static String CreateFullID(Class<? extends PCLOrb> type)
    {
        return GR.PCL.CreateID(type.getSimpleName());
    }

    public PCLOrb(String id, Timing passiveEffectTiming)
    {
        super(id, passiveEffectTiming);
        tooltip = new PCLCardTooltip(name, description);
        tooltip.subText = new ColoredString();
    }

    public static int GetFocus()
    {
        return PCLGameUtilities.GetPowerAmount(AbstractDungeon.player, FocusPower.POWER_ID);
    }

    public int GetBasePassiveAmount() {
        return this.basePassiveAmount;
    }

    public int GetBaseEvokeAmount() {
        return this.baseEvokeAmount;
    }

    public void IncreaseBasePassiveAmount(int amount) {
        this.basePassiveAmount += amount;
        applyFocus();
        this.updateDescription();
    }

    public void IncreaseBaseEvokeAmount(int amount) {
        this.baseEvokeAmount += amount;
        applyFocus();
        this.updateDescription();
    }

    protected String FormatDescription(int index, Object... args)
    {
        if (orbStrings.DESCRIPTION == null || orbStrings.DESCRIPTION.length <= index) {
            PCLJUtils.LogError(this, "orbStrings.Description does not exist, " + this.name);
            return "";
        }
        return PCLJUtils.Format(orbStrings.DESCRIPTION[index], args);
    }

    public String GetUpdatedDescription()
    {
        return FormatDescription(0);
    }

    @Override
    public void updateDescription()
    {
        this.applyFocus();
        tooltip.description = this.description = GetUpdatedDescription();
    }

    @Override
    public void update()
    {
        hb.update();
        if (hb.hovered)
        {
            PCLCardTooltip.QueueTooltip(tooltip, InputHelper.mX + hb.width, InputHelper.mY + (hb.height * 0.5f));
        }
        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }
}
