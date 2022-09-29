package eatyourbeets.relics.animator;

import eatyourbeets.effects.SFX;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.InputManager;

public abstract class OddDevice extends AnimatorRelic
{
    public OddDevice(String id, RelicTier tier, LandingSound landingSound)
    {
        super(id, tier, landingSound);

        SetEffectIndex(1);
    }

    @Override
    public void update()
    {
        super.update();

        if (hb != null && hb.hovered && InputManager.Control.IsJustPressed() && !GameUtilities.InBattle())
        {
            SFX.Play(SFX.UI_CLICK_1, 0.9f, 1.1f);
            SwitchEffect();
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        if (counter <= 0)
        {
            return FormatDescription(0);
        }
        else
        {
            return GetMainDescription(counter) + " NL NL " + FormatDescription(0);
        }
    }

    protected void SwitchEffect()
    {
        final int newIndex = GetEffectIndex() + 1;
        SetEffectIndex(newIndex > GetEffectsCount() ? 1 : newIndex);
    }

    protected String GetMainDescription(int effectIndex)
    {
        return FormatDescription(effectIndex);
    }

    public void SetEffectIndex(int index)
    {
        SetCounter(index);
        updateDescription();
    }

    public int GetEffectIndex()
    {
        return counter;
    }

    public int GetEffectsCount()
    {
        return 3;
    }
}