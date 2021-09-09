package eatyourbeets.cards.base;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import eatyourbeets.utilities.JUtils;
import patches.cardStrings.CardStringPatches;

public abstract class AnimatorCard_Multiform extends AnimatorCard implements CustomSavable<AnimatorCard_Multiform.MultiformData>, BranchingUpgradesCard
{
    public static class MultiformData {
        public Integer form;
        public boolean canBranch;

        public MultiformData(Integer form, boolean canBranch) {
            this.form = form;
            this.canBranch = canBranch;
        }
    }

    protected int form = 0;
    protected int totalForms = 2;
    protected boolean canBranch;

    protected AnimatorCard_Multiform(EYBCardData data) {
        this(data,0,true);
    }

    protected AnimatorCard_Multiform(EYBCardData data, Integer form, boolean canBranch)
    {
        super(data);
        this.canBranch = canBranch;
        ChangeForm(form);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        if (form > 0) {
            String[] alternateDescriptions = CardStringPatches.ALTERNATE_DESCRIPTION.get(cardData.Strings);
            if (alternateDescriptions != null && alternateDescriptions.length > form) {
                return JUtils.Format(alternateDescriptions[form], args);
            }
        }
        return super.GetRawDescription(args);
    }

    @Override
    public void renderUpgradePreview(SpriteBatch sb)
    {
        AnimatorCard_Multiform upgrade = JUtils.SafeCast(cardData.tempCard, AnimatorCard_Multiform.class);

        if (upgrade == null || upgrade.uuid != this.uuid || (upgrade.timesUpgraded != (timesUpgraded + 1)))
        {
            cardData.tempCard = upgrade = (AnimatorCard_Multiform) this.makeSameInstanceOf();
            upgrade.ChangeForm(form);
            upgrade.isPreview = true;
            upgrade.upgrade();
            upgrade.displayUpgrades();
        }

        upgrade.current_x = this.current_x;
        upgrade.current_y = this.current_y;
        upgrade.drawScale = this.drawScale;
        upgrade.render(sb, false);
    }

    @Override
    public MultiformData onSave()
    {
        return new MultiformData(form, canBranch);
    }

    @Override
    public void onLoad(MultiformData form)
    {
        this.canBranch = form.canBranch;
        ChangeForm(form.form);
    }

    public void ChangeForm(Integer form) {
        this.form = (form == null || form >= totalForms) ? 0 : form;
    };
}