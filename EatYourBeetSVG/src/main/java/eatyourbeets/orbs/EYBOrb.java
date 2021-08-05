package eatyourbeets.orbs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.effects.vfx.OrbEvokeParticle;
import eatyourbeets.effects.vfx.megacritCopy.OrbFlareEffect2;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.lang.reflect.InvocationTargetException;

public abstract class EYBOrb extends AbstractOrb implements OnStartOfTurnPostDrawSubscriber
{
    public static final int IMAGE_SIZE = 96;

    public enum Timing
    {
        EndOfTurn,
        StartOfTurn,
        StartOfTurnPostDraw
    }

    protected Timing passiveEffectTiming;
    protected final OrbStrings orbStrings;

    public EYBOrb(String id, Timing passiveEffectTiming)
    {
        this.orbStrings = GR.GetOrbStrings(id);
        this.ID = id;
        this.name = orbStrings.NAME;
        this.passiveEffectTiming = passiveEffectTiming;
    }

    @Override
    public AbstractOrb makeCopy()
    {
        try
        {
            return getClass().getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static int GetFocus()
    {
        return GameUtilities.GetPowerAmount(AbstractDungeon.player, FocusPower.POWER_ID);
    }

    public void onChannel()
    {
        if (passiveEffectTiming == Timing.StartOfTurnPostDraw)
        {
            CombatStats.onStartOfTurnPostDraw.SubscribeOnce(this);
        }
    }

    @Override
    public void triggerEvokeAnimation()
    {
        for (int i = 0; i < 4; i++)
        {
            GameEffects.Queue.Add(new OrbEvokeParticle(this.cX, this.cY, Colors.Lerp(GetColor1(), GetColor2(), MathUtils.random(0, 0.5f))));
        }
    }

    @Override
    public void onEvoke()
    {
        Evoke();
    }

    @Override
    public void onEndOfTurn()
    {
        super.onEndOfTurn();

        if (passiveEffectTiming == Timing.EndOfTurn)
        {
            Passive();
        }
    }

    @Override
    public void onStartOfTurn()
    {
        super.onStartOfTurn();

        if (passiveEffectTiming == Timing.StartOfTurn)
        {
            Passive();
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (passiveEffectTiming == Timing.StartOfTurnPostDraw)
        {
            if (AbstractDungeon.player.orbs.contains(this))
            {
                CombatStats.onStartOfTurnPostDraw.SubscribeOnce(this);
                Passive();
            }
        }
    }

    public void Passive()
    {
        OrbFlareEffect2 effect = GetOrbFlareEffect();
        if (effect != null)
        {
            GameActions.Bottom.VFX(effect, Settings.FAST_MODE ? 0 : (0.6F / (float)AbstractDungeon.player.orbs.size()));
        }

        CombatStats.OnOrbPassiveEffect(this);
    }

    public void Evoke()
    {
        // Orb Evoke event is already broadcast
    }

    protected OrbFlareEffect2 GetOrbFlareEffect()
    {
        return new OrbFlareEffect2(this).SetColors(GetColor1(), GetColor2());
    }

    protected Color GetColor1()
    {
        return Color.WHITE;
    }

    protected Color GetColor2()
    {
        return Color.LIGHT_GRAY;
    }
}
