package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import eatyourbeets.utilities.JUtils;
import patches.cardStrings.CardStringPatches;

public abstract class AnimatorCard_BranchingUpgradable extends AnimatorCard implements BranchingUpgradesCard //TODO
{
    protected AnimatorCard_BranchingUpgradable(EYBCardData data)
    {
        super(data);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        if (isBranchUpgrade()) {
            String[] alternateDescriptions = CardStringPatches.ALTERNATE_DESCRIPTION.get(cardData.Strings);
            if (alternateDescriptions != null && alternateDescriptions.length > 0) {
                return upgraded && alternateDescriptions.length > 1
                        ? JUtils.Format(alternateDescriptions[1], args)
                        : JUtils.Format(alternateDescriptions[0], args);
            }
        }
        return super.GetRawDescription(args);
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb)
    {
        AnimatorCard_BranchingUpgradable upgrade = JUtils.SafeCast(cardData.tempCard,AnimatorCard_BranchingUpgradable.class);

        if (upgrade == null || upgrade.uuid != this.uuid || (upgrade.timesUpgraded != (timesUpgraded + 1)))
        {
            cardData.tempCard = upgrade = (AnimatorCard_BranchingUpgradable) this.makeSameInstanceOf();
            upgrade.isPreview = true;
            upgrade.upgrade();
            upgrade.displayUpgrades();
        }

        upgrade.current_x = this.current_x;
        upgrade.current_y = this.current_y;
        upgrade.drawScale = this.drawScale;
        upgrade.render(sb, false);
    }
}