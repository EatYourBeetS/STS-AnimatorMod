package eatyourbeets.resources.unnamed;

import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;

public class UnnamedStrings
{
    private static final String Prefix = "unnamed:";

    public UIStrings EnergyPanel;

    public void Initialize()
    {
        EnergyPanel = GetUIStrings(Prefix + "EnergyPanel");
    }

    private static UIStrings GetUIStrings(String id)
    {
        return GR.GetUIStrings(GR.CreateID(UnnamedResources.ID, id));
    }
}