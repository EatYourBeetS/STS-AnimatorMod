package eatyourbeets.resources.animatorClassic;

import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.resources.GR;

public class AnimatorClassicStrings
{
    public void Initialize()
    {

    }

    private static UIStrings GetUIStrings(String id)
    {
        return GR.GetUIStrings(GR.CreateID(AnimatorClassicResources.ID, id));
    }
}